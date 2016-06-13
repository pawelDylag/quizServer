import java.util.Scanner;

/**
 * Created by paweldylag on 13/06/16.
 */
public class StartQuizServer {
    public static void main(String[] args) {
        QuizServer quizServer = null;
        try {
            System.out.println("Starting quiz server.");
            quizServer = new QuizServer();
            quizServer.init();

            boolean stop = false;
            Scanner s = new Scanner(System.in);
            while (s.hasNext() || stop) {
                if (s.next().equals("q")) {
                    quizServer.close();
                    stop = true;
                }
            }
        } finally {
            if (quizServer != null) {
                quizServer.close();
            }
        }

    }



}
