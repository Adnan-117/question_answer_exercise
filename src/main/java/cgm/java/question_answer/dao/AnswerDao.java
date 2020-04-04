package cgm.java.question_answer.dao;

import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnswerDao {

  private static final Logger logger = LogManager.getLogger(AnswerDao.class);

  public static Set<Answers> getAnswers(Question question) {
    Set<Answers> answers = new HashSet<>();

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      List<Answers>
          answersList = session.createQuery(
          "select a from Question q join Answers a on q.question_id = a.question.question_id where a.question.question_text = :question_text", Answers.class)
                               .setParameter("question_text", question.getQuestionText())
                               .getResultList();

      answers = new HashSet<>(answersList);
    } catch (HibernateException e) {
      logger.error("\n" + "Answers fetching failed" + "\n");
      //      e.printStackTrace();
    } catch (NoResultException nre) {
      logger.error("\n" + "Answer doesn't exist" + "\n");
      //      nre.printStackTrace();
    }
    return answers;
  }

}
