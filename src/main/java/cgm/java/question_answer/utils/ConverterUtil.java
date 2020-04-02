package cgm.java.question_answer.utils;

import cgm.java.question_answer.model.Answers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConverterUtil {

  public static Set<Answers> convertListArgumentToSetAnswers(List<String> argAnswers) {
    Set<Answers> answers = argAnswers.stream()
                                     .map(String::trim)
                                     .map(Answers::new)
                                     .collect(Collectors.toSet());
    return answers;
  }

}
