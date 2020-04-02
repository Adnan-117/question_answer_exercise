package cgm.java.question_answer.utils;

import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;

import java.util.Set;

public class PrintOutputUtil {

  final static String defaultAnswer = " \"the answer to life, universe and everything is 42\" according to \"The hitchhikers guide to the Galaxy\" ";

  public static void printNoArgumentsPresent() {

    System.out.println("• " + "Please ask question or add a question with answers no arguments present");

  }


  public static void printDefaultAnswer(StringBuilder argQuestion) {

    System.out.println("• " + argQuestion.toString());
    System.out.println(LeftPaddingToStringUtil.addLeftPadding(2, "• " + defaultAnswer));

  }

  public static void printAnswersFetched(StringBuilder argQuestion, Set<Answers> answersFetched) {

    System.out.println("This Question exists in the program" + "\n");
    System.out.println("• " + argQuestion.toString());

    answersFetched.forEach(answer -> System.out.println(LeftPaddingToStringUtil.addLeftPadding(2, "•" + answer.getAnswerText())));
  }

  public static void printPersistedQuestionWithAnswers(Question questionWithAnswers) {

    System.out.println("Questions Successfully added with answers" + "\n");
    System.out.println("• " + questionWithAnswers.getQuestionText());

    questionWithAnswers.getAnswersList()
                       .forEach(answer -> System.out.println(LeftPaddingToStringUtil.addLeftPadding(2, "•" + answer.getAnswerText())));
  }

}
