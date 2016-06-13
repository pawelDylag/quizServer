import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by paweldylag on 13/06/16.
 */
public class Session {

    public enum State {DEFAULT,WAITING_FOR_ANSWER, FINISHED}

    public List<Question> availableQuestions = new LinkedList<Question>();
    public int good = 0;
    public int bad = 0;
    public State currentState;
    public Question currentQuestion;

    public Session(List<Question> availableQuestions) {
        this.availableQuestions = availableQuestions;
    }

    public Question nextQuestion() {
        System.out.println("Next question");
        this.currentQuestion = randomQuestion();
        if(currentQuestion != null) {
            this.currentState = State.WAITING_FOR_ANSWER;
        } else {
            this.currentState = State.FINISHED;
        }
        return this.currentQuestion;
    }

    public void onAnswer(Answer a) {
        if (currentQuestion.checkAnswer(a)) {
            good++;
        } else {
            bad++;
        }

    }

    private Question randomQuestion() {
        Random r = new Random();
        if (availableQuestions.isEmpty()) {
            return null;
        }
        int index = r.nextInt(availableQuestions.size());
        Question q = availableQuestions.get(index);
        availableQuestions.remove(index);
        return q;
    }

    public void finish(){
        this.currentState = State.FINISHED;
        currentQuestion = null;
    }

    public String getScore() {
        return "Your score: " + good + " good, " + bad + " bad";
    }

}
