package cgm.java.question_answer.service;

import cgm.java.question_answer.dao.AnswerDao;
import cgm.java.question_answer.dao.QuestionDao;
import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.utils.ConverterUtil;
import cgm.java.question_answer.utils.PrintOutputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArgumentsPersistenceService {

  private static StringBuilder argQuestion = new StringBuilder();
  private static List<String> argAnswers = new ArrayList<>();
  private static boolean flagQuestionAppended = false;
  private static final Logger logger = LogManager.getLogger(ArgumentsPersistenceService.class);

  //the arguments passed as program arguments for example
  //What is Peters Favourite sports? "Badminton" "Tennis"
  //the following method extract arguments to identify question and answers from the above argument
  public static void extractArgumentsAndAppend(String... args) {
    if (args.length > 0) {
      //      logger.debug("Arguments are present");
      for (String arg : args) {
        if (!arg.contains("?") && !flagQuestionAppended) {
          argQuestion.append(arg);
          argQuestion.append(" ");
        } else if (arg.contains("?")) {
          argQuestion.append(arg);
          flagQuestionAppended = true;
        } else if (flagQuestionAppended) {
          argAnswers.add(arg);
        }
      }
      persistQuestionsOrFetchAnswers();
    } else {
      //      logger.debug("Arguments are not present");
      PrintOutputUtil.printNoArgumentsPresent();
    }
  }

  public static void persistQuestionsOrFetchAnswers() {
    int totalAnswerArguments = argAnswers.size();

    if (totalAnswerArguments == 0) {
      //      logger.debug("Answers are not present fetch answers");
      fetchAnswersForQuestionAsked();
    } else {
      //      logger.debug("Answers are present");
      Question questionAsked = new Question(argQuestion.toString());

      if (verifyIfQuestionExists(questionAsked)) {
        //      logger.debug("Question already stored get answers");
        getAnswers(questionAsked);
      } else {
        //      logger.debug("Question not stored ");
        persistQuestionWithAnswers();
      }
    }
  }

  public static void fetchAnswersForQuestionAsked() {
    Question questionAsked = new Question(argQuestion.toString());

    if (verifyIfQuestionExists(questionAsked)) {
      //      logger.debug("Question already stored get answers");
      getAnswers(questionAsked);
    } else {
      //      logger.debug("Question not stored but has no answers therefore logging default answer");
      PrintOutputUtil.printDefaultAnswer(argQuestion);
    }
  }

  public static boolean verifyIfQuestionExists(Question questionAsked) {
    try {
      Question alreadyExistsQuestion = QuestionDao.getQuestionByText(questionAsked);
      return alreadyExistsQuestion != null;
    } catch (NullPointerException e) {
      logger.error("Question does not exist");
      return false;
    }
  }


  public static void persistQuestionWithAnswers() {
    Question questionWithAnswers = new Question(argQuestion.toString());

    //convert list of arg string answers to Set of Answer objects
    Set<Answers> answers = ConverterUtil.convertListArgumentToSetAnswers(argAnswers);

    // add question to the child i.e answer and vice versa
    answers.forEach(questionWithAnswers::addQuestionToAnswer);

    //save questions to db with answers
    boolean isPersisted = QuestionDao.saveQuestion(questionWithAnswers);

    if (isPersisted) {
      PrintOutputUtil.printPersistedQuestionWithAnswers(questionWithAnswers);
    }
  }

  public static void getAnswers(Question questionAsked) {
    try {
      Set<Answers> answersFetched = AnswerDao.getAnswers(questionAsked);
      PrintOutputUtil.printAnswersFetched(argQuestion, answersFetched);
      //      logger.debug("answers fetched");
    } catch (NullPointerException e) {
      logger.error("Answers does not exist");
    }
  }

}
