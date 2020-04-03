package cgm.java.question_answer;

import cgm.java.question_answer.dao.AnswerDao;
import cgm.java.question_answer.dao.QuestionDao;
import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.utils.ArgumentsPersistenceUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class QuestionAnswersSteps {

  public Question questionAskedNotStoredYet = null;
  public Question questionAskedAlreadyStored = null;
  public Question questionToAdd = null;
  final static String defaultAnswer = " \"the answer to life, universe and everything is 42\" according to \"The hitchhikers guide to the Galaxy\" ";


  @Given("the user asks a question {string}")
  public void theUserAsksQuestion(String question) {
    questionAskedNotStoredYet = new Question(question);
  }

  @Then("verify this question doesn't exist")
  public void verifyQuestionNotExist() {
    boolean retrievedExistFlag = ArgumentsPersistenceUtil.verifyIfQuestionExists(questionAskedNotStoredYet);
    Assert.assertFalse(retrievedExistFlag);
  }

  @Then("the program prints the default answer")
  public void printDefaultAnswer() {
    System.out.println(defaultAnswer);
  }


  @Given("the user adds the question {string} with answers {string}")
  public void theUserAddsTheQuestionWithAnswers(String question, String answers) {
    Set<Answers> answersSet = getSetOfAnswersFromString(answers);
    questionToAdd = new Question(question, answersSet);
    answersSet.forEach(questionToAdd::addQuestionToAnswer);
  }

  @Then("verify this question doesn't exist with answers")
  public void verifyThisQuestionDoesnTExistWithAnswers() {
    boolean retrievedExistFlag = ArgumentsPersistenceUtil.verifyIfQuestionExists(questionToAdd);
    Assert.assertFalse(retrievedExistFlag);
  }

  @And("the question includes atleast one answer")
  public void theQuestionIncludesAtleastOnAnswer() {
    long expectedAnswerSetSize = 1;
    long actualAnswerSetSize = (long) questionToAdd.getAnswersList().size();

    Assert.assertTrue( actualAnswerSetSize >= expectedAnswerSetSize);
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
    boolean retrievedExistFlag = ArgumentsPersistenceUtil.verifyIfQuestionExists(questionAskedAlreadyStored);
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

}
