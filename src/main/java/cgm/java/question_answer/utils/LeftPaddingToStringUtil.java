package cgm.java.question_answer.utils;

public class LeftPaddingToStringUtil {

  public static String addLeftPadding(int i, String str) {
    StringBuilder paddedString = new StringBuilder();
    for (int j = 0; j < i; j++) {
      paddedString.append(" ");
    }
    paddedString.append(str);
    return paddedString.toString();
  }

}
