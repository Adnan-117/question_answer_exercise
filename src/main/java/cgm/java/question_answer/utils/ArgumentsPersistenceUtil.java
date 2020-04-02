package cgm.java.question_answer.utils;

import cgm.java.question_answer.dao.QuestionAnswerDao;
import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArgumentsPersistenceUtil {

  private static StringBuilder argQuestion = new StringBuilder();
  private static List<String> argAnswers = new ArrayList<>();
  private static boolean flagQuestionAppended = false;
  final static String defaultAnswer = " \"the answer to life, universe and everything is 42\" according to \"The hitchhikers guide to the Galaxy\" ";

  public static void extractArgumentsAndAppend(String[] args) {
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
  }

  private static void persistQuestionsOrFetchAnswers() {
    int totalAnswerArguments = argAnswers.size();
    Set<Answers> answers = ConverterUtil.convertListArgumentToSetAnswers(argAnswers);

    if (totalAnswerArguments == 0) {
      fetchAnswersAndPrint();
    } else {
      Question alreadyExistsQuestion = QuestionAnswerDao.getQuestionByText(new Question(argQuestion.toString()));
      if (alreadyExistsQuestion != null) {
        System.out.println("This Question already exist please find the answers");
        fetchAnswersAndPrint();
      } else {
        persistQuestion(answers);
      }
    }
  }

  private static void fetchAnswersAndPrint() {
    Question questionAsked = new Question(argQuestion.toString());
    Set<Answers> answers = QuestionAnswerDao.getAnswers(questionAsked);
    //print answers fetched otherwise default answer since the question is not stored yet
    if (answers.isEmpty()) {
      System.out.println(argQuestion.toString());
      System.out.println(defaultAnswer);
    } else {
      System.out.println(argQuestion.toString());
      answers.forEach(answer -> System.out.println(answer.getAnswerText()));
    }
  }

  private static void persistQuestion(Set<Answers> answers) {
    Question questionWithAnswers = new Question(argQuestion.toString());
    answers.forEach(questionWithAnswers::addQuestionToAnswer);
    //save questions to db with answers
    boolean isPersisted = QuestionAnswerDao.saveQuestion(questionWithAnswers);
    if (isPersisted) {
      System.out.println("Questions Successfully added with answers");
      System.out.println(questionWithAnswers.getQuestionText());
      questionWithAnswers.getAnswersList().forEach(answer -> System.out.println(answer.getAnswerText()));
    }
  }

}
