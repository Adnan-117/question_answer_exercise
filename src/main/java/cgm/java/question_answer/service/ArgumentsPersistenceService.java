package cgm.java.question_answer.service;

import cgm.java.question_answer.dao.AnswerDao;
import cgm.java.question_answer.dao.QuestionDao;
import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.utils.ConverterUtil;
import cgm.java.question_answer.utils.PrintOutputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArgumentsPersistenceService {

  private static StringBuilder argQuestion = new StringBuilder();
  private static List<String> argAnswers = new ArrayList<>();
  private static boolean flagQuestionAppended = false;

  public static void extractArgumentsAndAppend(String[] args) {
    if (args.length > 0) {
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
      PrintOutputUtil.printNoArgumentsPresent();
    }
  }

  public static void persistQuestionsOrFetchAnswers() {
    int totalAnswerArguments = argAnswers.size();

    if (totalAnswerArguments == 0) {
      fetchAnswersForQuestionAsked();
    } else {
      Question questionAsked = new Question(argQuestion.toString());

      if (verifyIfQuestionExists(questionAsked)) {
        getAnswers(questionAsked);
      } else {
        persistQuestionWithAnswers();
      }
    }
  }

  public static void fetchAnswersForQuestionAsked() {
    Question questionAsked = new Question(argQuestion.toString());

    if (verifyIfQuestionExists(questionAsked)) {
      getAnswers(questionAsked);
    } else {
      PrintOutputUtil.printDefaultAnswer(argQuestion);
    }
  }

  public static void getAnswers(Question questionAsked) {
    Set<Answers> answersFetched = AnswerDao.getAnswers(questionAsked);
    PrintOutputUtil.printAnswersFetched(argQuestion, answersFetched);
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

  public static boolean verifyIfQuestionExists(Question questionAsked) {
    Question alreadyExistsQuestion = QuestionDao.getQuestionByText(questionAsked);
    return alreadyExistsQuestion != null;
  }

}
