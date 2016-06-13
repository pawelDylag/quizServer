import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by paweldylag on 13/06/16.
 */
public class Question {

    public String text;
    public List<String> possibleAnswers;
    public Answer properAnswer;

    public Question(String text, String a, String b, String c , String d) {
        this.text = text;
        this.possibleAnswers = new LinkedList<String>();
        possibleAnswers.add(a);
        possibleAnswers.add(b);
        possibleAnswers.add(c);
        possibleAnswers.add(d);
    }

    public void addPossibleAnswer(String a) {
        possibleAnswers.add(a);
    }

    public void setProperAnswer(Answer properAnswer) {
        this.properAnswer = properAnswer;
    }

    public boolean checkAnswer(Answer a) {
        return a.equals(properAnswer);
    }

    public String getText() {
        return text;
    }

    public String getAnswerAtIndex(int index) {
        return possibleAnswers.get(index);
    }

    public String getQuestionWithPossibleAnswers() {
        StringBuilder sb = new StringBuilder();
        sb.append(text);
        sb.append("\n");
        sb.append("A. ");
        sb.append(possibleAnswers.get(0));
        sb.append("\n");
        sb.append("B. ");
        sb.append(possibleAnswers.get(1));
        sb.append("\n");
        sb.append("C. ");
        sb.append(possibleAnswers.get(2));
        sb.append("\n");
        sb.append("D. ");
        sb.append(possibleAnswers.get(3));
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", possibleAnswers=" + Arrays.toString(possibleAnswers.toArray()) +
                ", properAnswer=" + properAnswer +
                '}';
    }
}
