package cgm.java.question_answer.dao;

import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

public class QuestionDao {

  private static final Logger logger = LogManager.getLogger(QuestionDao.class);

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
      e.printStackTrace();
    } catch (PersistenceException pre) {
      logger.error("\n" + "Questions persistence failed, Data too long for column" + "\n");
      //      pre.printStackTrace();
      // the parent is persisted i.e question so need to delete it a workaround
      deleteQuestionByText(question);
    } finally {
      session.close();
    }
    return isPersisted;
  }

  public static Question getQuestionByText(Question question) {
    Question resultantQuestion = null;

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      resultantQuestion = session.createQuery("select q from Question q  where q.question_text = :question_text", Question.class)
                                 .setParameter("question_text", question.getQuestionText())
                                 .getSingleResult();
    } catch (HibernateException e) {
      logger.error("\n" + "Question fetching failed" + "\n");
      //      e.printStackTrace();
    } catch (NoResultException nre) {
      logger.error("\n" + "Question doesn't exist" + "\n");
      //      nre.printStackTrace();
    }
    return resultantQuestion;
  }

  public static void deleteQuestionByText(Question question) {
    Transaction transaction = null;
    Session session = HibernateUtil.getSessionFactory().openSession();
    try {
      transaction = session.beginTransaction();
      session.createQuery("delete Question q where q.question_text = :question_text")
             .setParameter("question_text", question.getQuestionText()).executeUpdate();
      transaction.commit();
    } catch (HibernateException e) {
      if (transaction != null) {
        transaction.rollback();
      }
      logger.error("\n" + "Question deletion failed" + "\n");
      //      e.printStackTrace();
    } catch (NoResultException nre) {
      logger.error("\n" + "Question doesn't exist" + "\n");
      //      nre.printStackTrace();
    }
  }

}
