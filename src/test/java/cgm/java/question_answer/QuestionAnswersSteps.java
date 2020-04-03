package cgm.java.question_answer;

import cgm.java.question_answer.dao.AnswerDao;
import cgm.java.question_answer.dao.QuestionDao;
import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.service.ArgumentsPersistenceService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class QuestionAnswersSteps {

  private Question questionAskedNotStoredYet = null;
  private Question questionAskedAlreadyStored = null;
  private Question questionToAdd = null;
  private Question exceededQuestion = null;
  private Question exceededQuestionWithAnswer = null;
  final static String defaultAnswer = " \"the answer to life, universe and everything is 42\" according to \"The hitchhikers guide to the Galaxy\" ";


  @Given("the user asks a question {string}")
  public void theUserAsksQuestion(String question) {
    questionAskedNotStoredYet = new Question(question);
  }

  @Then("verify this question doesn't exist")
  public void verifyQuestionNotExist() {
    boolean retrievedExistFlag = ArgumentsPersistenceService.verifyIfQuestionExists(questionAskedNotStoredYet);
    Assert.assertFalse(retrievedExistFlag);
  }

  @Then("the program prints the default answer")
  public void printDefaultAnswer() {
    System.out.println(defaultAnswer);
  }

  @Given("the user adds question exceeding maximum characters")
  public void theUserAddsQuestionExceedingMaximumCharacters() {
    StringBuilder question = getAlphaNumericString(256);
    question.append(" adn?");
    Set<Answers> answersSet = getSetOfAnswersFromString("Pizza");

    exceededQuestion = new Question(question.toString(), answersSet);
    answersSet.forEach(exceededQuestion::addQuestionToAnswer);
  }

  @Then("the question is not stored in the database")
  public void theQuestionIsNotStoredInTheDatabase() {
    boolean addedQuestionFlag = QuestionDao.saveQuestion(exceededQuestion);
    Assert.assertFalse(addedQuestionFlag);
  }

  @Given("the user adds question with answers exceeding maximum characters")
  public void theUserAddsQuestionWithAnswersExceedingMaximumCharacters() {
    StringBuilder answer = getAlphaNumericString(256);
    Set<Answers> answersSet = getSetOfAnswersFromString(answer.toString());

    String askedQuestion  = "What is Peter's favourite food?";
    exceededQuestionWithAnswer = new Question(askedQuestion, answersSet);
    answersSet.forEach(exceededQuestionWithAnswer::addQuestionToAnswer);
  }

  @Then("the question with answer is not stored in the database")
  public void theQuestionWithAnswerIsNotStoredInTheDatabase() {
    boolean addedQuestionFlag = QuestionDao.saveQuestion(exceededQuestionWithAnswer);
    Assert.assertFalse(addedQuestionFlag);
  }

  @Given("the user adds the question {string} with answers {string}")
  public void theUserAddsTheQuestionWithAnswers(String question, String answers) {
    Set<Answers> answersSet = getSetOfAnswersFromString(answers);
    questionToAdd = new Question(question, answersSet);
    answersSet.forEach(questionToAdd::addQuestionToAnswer);
  }

  @Then("verify this question doesn't exist with answers")
  public void verifyThisQuestionDoesnTExistWithAnswers() {
    boolean retrievedExistFlag = ArgumentsPersistenceService.verifyIfQuestionExists(questionToAdd);
    Assert.assertFalse(retrievedExistFlag);
  }

  @And("the question includes atleast one answer")
  public void theQuestionIncludesAtleastOnAnswer() {
    long expectedAnswerSetSize = 1;
    long actualAnswerSetSize = (long) questionToAdd.getAnswersList().size();

    Assert.assertTrue(actualAnswerSetSize >= expectedAnswerSetSize);
  }

  @And("both question {string} and answers {string} doesn't exceed the maximum character space of {int}")
  public void bothQuestionQuestionAndAnswersAnswersNotExceedTheMaximumCharacterSpaceOf(String question, String answers, int maxSize) {
    Assert.assertTrue(question.length() < maxSize);
    Assert.assertTrue(answers.length() < maxSize);
  }

  @And("the program stores the question into the database")
  public void theProgramStoresTheQuestionIntoTheDatabase() {
    boolean addedQuestionFlag = QuestionDao.saveQuestion(questionToAdd);
    Assert.assertTrue(addedQuestionFlag);
  }

  @Given("the user asks a question {string} already stored")
  public void theUserAsksAQuestionAlreadyStored(String question) {
    questionAskedAlreadyStored = new Question(question);
  }

  @Then("verify this question exists")
  public void verifyThisQuestionExists() {
    boolean retrievedExistFlag = ArgumentsPersistenceService.verifyIfQuestionExists(questionAskedAlreadyStored);
    Assert.assertTrue(retrievedExistFlag);
  }

  @And("the program must fetch the answers {string}")
  public void theProgramMustFetchTheAnswers(String answers) {
    Set<String> expectedAnswers = getSetOfStringFromString(answers);
    Set<Answers> resultantAnswers = AnswerDao.getAnswers(questionAskedAlreadyStored);

    Set<String> resultantAnswerText = new HashSet<>();
    resultantAnswers.forEach(answers1 -> resultantAnswerText.add(answers1.getAnswerText()));

    Assert.assertEquals(expectedAnswers, resultantAnswerText);
  }


  private Set<Answers> getSetOfAnswersFromString(String answers) {
    return Stream.of(answers.split(","))
                 .map(String::trim)
                 .map(Answers::new)
                 .collect(Collectors.toSet());
  }

  private Set<String> getSetOfStringFromString(String answers) {
    return Stream.of(answers.split(","))
                 .map(String::trim)
                 .collect(Collectors.toSet());
  }

  // function to generate a random string of length n
  static StringBuilder getAlphaNumericString(int n) {

    // chose a Character random from this String
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "0123456789"
        + "abcdefghijklmnopqrstuvxyz";

    // create StringBuffer size of AlphaNumericString
    StringBuilder sb = new StringBuilder(n);

    for (int i = 0; i < n; i++) {

      // generate a random number between
      // 0 to AlphaNumericString variable length
      int index
          = (int) (AlphaNumericString.length()
          * Math.random());

      // add Character one by one in end of sb
      sb.append(AlphaNumericString
                    .charAt(index));
    }

    return sb;
  }

}
