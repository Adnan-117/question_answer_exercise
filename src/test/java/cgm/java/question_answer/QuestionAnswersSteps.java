package cgm.java.question_answer;

import cgm.java.question_answer.dao.AnswerDao;
import cgm.java.question_answer.dao.QuestionDao;
import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.service.ArgumentsPersistenceService;
import cgm.java.question_answer.utils.ConverterUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.HashSet;
import java.util.List;
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
  private static final Logger logger = LogManager.getLogger(QuestionAnswersSteps.class);


  @Given("the user asks a question {string}")
  public void theUserAsksQuestion(String question) {
    questionAskedNotStoredYet = new Question(question);
  }

  @Then("verify this question doesn't exist")
  public void verifyQuestionNotExist() {
    boolean retrievedExistFlag = ArgumentsPersistenceService.verifyIfQuestionExists(questionAskedNotStoredYet);
    Assert.assertFalse(retrievedExistFlag);
    logger.info("Question asked, not stored yet: " + questionAskedNotStoredYet.getQuestionText());
  }

  @Then("the program prints the default answer")
  public void printDefaultAnswer() {
    logger.info(defaultAnswer);
  }

  @Given("the user adds question exceeding maximum characters")
  public void theUserAddsQuestionExceedingMaximumCharacters() {
    StringBuilder question = getAlphaNumericString(256);
    question.append(" adn?");
    Set<Answers> answersSet = getSetOfAnswersFromString("Pizza");

    exceededQuestion = new Question(question.toString(), answersSet);
    answersSet.forEach(exceededQuestion::addQuestionToAnswer);
    logger.info("Question exceeding max character " + exceededQuestion.getQuestionText());
  }

  @Then("the question is not stored in the database")
  public void theQuestionIsNotStoredInTheDatabase() {
    boolean addedQuestionFlag = QuestionDao.saveQuestion(exceededQuestion);
    Assert.assertFalse(addedQuestionFlag);
    logger.info("Question exceeding not stored in the database");
  }

  @Given("the user adds question with answers exceeding maximum characters")
  public void theUserAddsQuestionWithAnswersExceedingMaximumCharacters() {
    StringBuilder answer = getAlphaNumericString(256);
    Set<Answers> answersSet = getSetOfAnswersFromString(answer.toString());

    String askedQuestion = "What is Peter's favourite food?";
    exceededQuestionWithAnswer = new Question(askedQuestion, answersSet);
    answersSet.forEach(exceededQuestionWithAnswer::addQuestionToAnswer);
    logger.info("Answer exceeding max character " + answer.toString());
  }

  @Then("the question with answer is not stored in the database")
  public void theQuestionWithAnswerIsNotStoredInTheDatabase() {
    boolean addedQuestionFlag = QuestionDao.saveQuestion(exceededQuestionWithAnswer);
    Assert.assertFalse(addedQuestionFlag);
    logger.info("Question exceeding with answers not stored in the database");
  }

  @Given("the user adds the question {string} with answers")
  public void theUserAddsTheQuestionWithAnswers(String question, List<String> answers) {
    Set<Answers> answersSet = ConverterUtil.convertListArgumentToSetAnswers(answers);
    questionToAdd = new Question(question, answersSet);
    answersSet.forEach(questionToAdd::addQuestionToAnswer);
    logger.info("question added with answers " + question + " " + answers);
  }

  @Then("verify this question doesn't exist with answers")
  public void verifyThisQuestionDoesntExistWithAnswers() {
    boolean retrievedExistFlag = ArgumentsPersistenceService.verifyIfQuestionExists(questionToAdd);
    Assert.assertFalse(retrievedExistFlag);
  }

  @And("the question includes atleast one answer")
  public void theQuestionIncludesAtleastOneAnswer() {
    long expectedAnswerSetSize = 1;
    long actualAnswerSetSize = (long) questionToAdd.getAnswersList().size();

    Assert.assertTrue(actualAnswerSetSize >= expectedAnswerSetSize);
  }

  @And("both question and answers doesn't exceed the maximum character space of {int}")
  public void bothQuestionAndAnswersNotExceedTheMaximumCharacterSpace(int maxSize) {
    Assert.assertTrue(questionToAdd.getQuestionText().length() < maxSize);
    questionToAdd.getAnswersList().forEach(answers -> Assert.assertTrue(answers.getAnswerText().length() < maxSize));
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

  @And("the program must fetch the following answers")
  public void theProgramMustFetchTheFollowingAnswers(List<String> answers) {
    Set<String> expectedAnswers = new HashSet<>(answers);
    Set<Answers> resultantAnswers = AnswerDao.getAnswers(questionAskedAlreadyStored);

    Set<String> resultantAnswerText = new HashSet<>();
    resultantAnswers.forEach(answersFetched -> resultantAnswerText.add(answersFetched.getAnswerText()));

    Assert.assertEquals(expectedAnswers, resultantAnswerText);
    logger.info("Question asked: " + " " + questionAskedAlreadyStored.getQuestionText());
    resultantAnswerText.forEach(logger::info);
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
