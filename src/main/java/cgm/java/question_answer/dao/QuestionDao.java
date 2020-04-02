package cgm.java.question_answer.dao;

import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;

public class QuestionDao {

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
        System.out.println("Questions persistence failed" + "\n");
        e.printStackTrace();
      } finally {
        session.close();
      }
    return isPersisted;
  }

  public static Question getQuestionByText(Question question) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Question resultantQuestion = null;
    try {
      resultantQuestion = session.createQuery("select q from Question q  where q.question_text = :question_text", Question.class)
                                                     .setParameter("question_text", question.getQuestionText())
                                                     .getSingleResult();
    } catch (HibernateException e) {
      System.out.println("Question fetching failed" + "\n");
      e.printStackTrace();
    } catch (NoResultException nre) {
      System.out.println("Question doesn't exist" + "\n");
    }
    return resultantQuestion;
  }

}
