package cgm.java.question_answer.dao;

import cgm.java.question_answer.model.Answers;
import cgm.java.question_answer.model.Question;
import cgm.java.question_answer.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnswerDao {

  public static Set<Answers> getAnswers(Question question) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Set<Answers> answers = new HashSet<>();
    try {
      List<Answers>
          answersList = session.createQuery("select a from Question q join Answers a on q.question_id = a.question.question_id where a.question.question_text = :question_text", Answers.class)
                               .setParameter("question_text", question.getQuestionText())
                               .getResultList();

      answers = new HashSet<>(answersList);
    } catch (HibernateException e) {
      System.out.println("Answers fetching failed");
      e.printStackTrace();
    }
    return answers;
  }

}
