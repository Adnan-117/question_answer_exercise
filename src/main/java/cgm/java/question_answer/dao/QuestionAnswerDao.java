package cgm.java.question_answer.dao;

import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionAnswerDao {

  public static boolean saveQuestion(Question question) {
    boolean isPersisted = false;
      Transaction transaction = null;
      Session session = HibernateUtil.getSessionFactory().openSession();

      try {
        // start a transaction
        transaction = session.beginTransaction();
        // save the Question object includes answer/s
        session.save(question);
        // commit transaction
        transaction.commit();
        isPersisted = true;
      } catch (HibernateException e) {
        if (transaction != null) {
          transaction.rollback();
        }
        System.out.println("Questions persistence failed");
        e.printStackTrace();
      } finally {
        session.close();
      }
    return isPersisted;
  }

  public static Set<Answers> getAnswers(Question question) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Set<Answers> answers = new HashSet<>();
    List<Answers> answersList;
    try {
      answersList = session.createQuery("select a from Question q join Answers a on q.question_id = a.question.question_id where a.question.question_text = :question_text")
                                         .setParameter("question_text", question.getQuestionText())
                                         .getResultList();
      answers = new HashSet<>(answersList);
    } catch (HibernateException e) {
      System.out.println("Answers fetching failed");
      e.printStackTrace();
    }
    return answers;
  }

  public static Question getQuestionByText(Question question) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Question resultantQuestion = null;
    try {
      resultantQuestion = (Question) session.createQuery("select q from Question q  where q.question_text = :question_text")
                                                     .setParameter("question_text", question.getQuestionText())
                                                     .getSingleResult();
    } catch (HibernateException e) {
      System.out.println("Question fetching failed");
      e.printStackTrace();
    } catch (NoResultException nre) {
      System.out.println("Question doesn't exist");
    }
    return resultantQuestion;
  }

//  public static boolean updateQuestionWithAnswers(Question question) {
//    boolean isUpdated = false;
//    Transaction transaction = null;
//    Session session = HibernateUtil.getSessionFactory().openSession();
//
//    try {
//      // start a transaction
//      transaction = session.beginTransaction();
//      // update the Question object includes answer/s
//      session.merge(question);
//      // commit transaction
//      transaction.commit();
//      isUpdated = true;
//    } catch (HibernateException e) {
//      if (transaction != null) {
//        transaction.rollback();
//      }
//      System.out.println("Questions update failed");
//      e.printStackTrace();
//    } finally {
//      session.close();
//    }
//    return isUpdated;
//  }

}
