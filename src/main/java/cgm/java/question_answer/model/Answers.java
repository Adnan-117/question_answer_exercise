package cgm.java.question_answer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;


@Entity
@Table(name = "Answers")
public class Answers {

  //Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "answer_id")
  private Long answer_id;

  @NotNull
  @Size(max = 255)
  @Column(name = "answer_text")
  private String answer_text;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id" , nullable = false)
  private Question question;

  public Answers() {
  }

  public Answers(String answer_text) {
    this.answer_text = answer_text;
  }

  public Answers(String answer_text, Question question) {
    this.answer_text = answer_text;
    this.question = question;
  }

  public  Long getAnswerId() { return answer_id; }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public String getAnswerText() {
    return answer_text;
  }

  public void setAnswer_text(String answer_text) {
    this.answer_text = answer_text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Answers answers = (Answers) o;
    return getAnswerText().equals(answers.getAnswerText()) &&
        getQuestion().equals(answers.getQuestion());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getAnswerText(), getQuestion());
  }
}
