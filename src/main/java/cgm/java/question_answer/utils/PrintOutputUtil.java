package cgm.java.question_answer.utils;

import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class PrintOutputUtil {

  final static String defaultAnswer = " \"the answer to life, universe and everything is 42\" according to \"The hitchhikers guide to the Galaxy\" ";
  private static final Logger logger = LogManager.getLogger(PrintOutputUtil.class);

  public static void printNoArgumentsPresent() {

    logger.warn("• " + "Please ask question or add a question with answers no arguments present");

  }


  public static void printDefaultAnswer(StringBuilder argQuestion) {

    logger.info("• " + argQuestion.toString() + "\n");
    logger.info(LeftPaddingToStringUtil.addLeftPadding(2, "• " + defaultAnswer));

  }

  public static void printAnswersFetched(StringBuilder argQuestion, Set<Answers> answersFetched) {

    logger.info("\n" + "This Question exists in the program" + "\n");
    logger.info("• " + argQuestion.toString() + "\n");

    answersFetched.forEach(answer -> logger.info(LeftPaddingToStringUtil.addLeftPadding(2, "• " + answer.getAnswerText() + "\n")));
  }

  public static void printPersistedQuestionWithAnswers(Question questionWithAnswers) {

    logger.info("Questions Successfully added with answers" + "\n");
    logger.info("• " + questionWithAnswers.getQuestionText() + "\n");

    questionWithAnswers.getAnswersList()
                       .forEach(answer -> logger.info(LeftPaddingToStringUtil.addLeftPadding(2, "• " + answer.getAnswerText() + "\n")));
  }

}
