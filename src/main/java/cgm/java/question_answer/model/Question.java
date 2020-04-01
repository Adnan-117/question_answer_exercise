package cgm.java.question_answer.model;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Question")
public class Question implements Serializable {

  //Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "question_id")
  private Long question_id;

  @NotNull
  @Size(max = 255)
  @Column(name = "question_text", unique = true)
  private String question_text;

  @OneToMany(cascade = {CascadeType.ALL }, mappedBy = "question")
  public Set<Answers> answersList = new HashSet<>();

  //Default Constructor
  public Question() {
  }

  // Constructor overloading
  public Question(String question_text) {
    this.question_text = question_text;
  }

  public Question(String question_text, Set<Answers> answersList) {
    this.question_text = question_text;
    this.answersList = answersList;
  }
  public void addQuestionToAnswer(Answers answer) {
    answersList.add(answer);
    answer.setQuestion(this);
  }
  //setters and getter Encapsulation

  public Long getIdQuestion() { return  question_id; }

  public String getQuestionText() {
    return question_text;
  }

  public void setQuestionText(String question) {
    this.question_text = question;
  }

  public Set<Answers> getAnswersList() { return  answersList;}

  public void setAnswersList(Set<Answers> answersList) { this.answersList = answersList ;}

  //hash code and equals
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Question question = (Question) o;
    return getQuestionText().equals(question.getQuestionText()) &&
        getAnswersList().equals(question.getAnswersList());
  }

  @Override
  public int hashCode() {
    return Objects.hash(question_text);
  }

}
