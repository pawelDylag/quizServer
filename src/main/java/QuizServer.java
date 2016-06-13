import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by paweldylag on 13/06/16.
 */
public class QuizServer {

    private static final String USERNAME = "Quiz";
    private static final String PASSWORD = "quiz123";

    private static final String OP_LOGIN = "login";
    private static final String OP_LOGOUT = "logout";
    private static final String OP_REGISTER = "register";
    private static final String OP_MESSAGE = "message";
    private static final String OP_MESSAGE_HISTORY = "message_history";
    private static final String OP_GET_CONTACTS = "get_contacts";
    private static final String OP_ADD_CONTACT = "add_contact";
    private static final String OP_GET_ALL_USERS = "get_users";
    private static final String OP_CHANGE_DESCRIPTION = "change_description";

    private Socket socket;
    public List<Question> allQuestions = new LinkedList<Question>();
    public HashMap<String, Session> currentSessions = new HashMap<String, Session>();
    private Gson gson = new GsonBuilder().create();

    public void init() {
        try {
            initQuestions();
            socket = IO.socket("http://10.131.249.253:3000");
            registerMessageListener();
            socket.connect();
        } catch (URISyntaxException e ) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        loginToMessageServer();
    }

    public void close() {
        socket.emit(OP_LOGOUT);
        socket.close();
    }

    private void initQuestions() throws FileNotFoundException {
        File questionsFile = new File("/Users/paweldylag/IdeaProjects/quizServer/src/main/resources/questions.txt");
        File answersFile = new File("/Users/paweldylag/IdeaProjects/quizServer/src/main/resources/answers.txt");
        Scanner questionsScanner = new Scanner(questionsFile).useDelimiter("[\n]");
        Scanner answersScanner = new Scanner(answersFile).useDelimiter("[\n]");
        while (questionsScanner.hasNext() && answersScanner.hasNext()) {
            String questionRaw = questionsScanner.next();
            String properAnswer = answersScanner.next();
            System.out.println("String : " + questionRaw);
            String[] q = questionRaw.split("[@]");
            Question question = new Question(q[0], q[1], q[2], q[3], q[4] );
            question.setProperAnswer(Answer.fromString(properAnswer));
            allQuestions.add(question);
        }
        answersScanner.close();
        questionsScanner.close();
    }

    private void loginToMessageServer() {
        System.out.println("loginToMessageServer");
        socket.emit(OP_LOGIN, USERNAME, PASSWORD);
    }

    private void registerMessageListener() {
        socket.on(OP_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                final Message m = gson.fromJson(data.toString(), Message.class);
                System.out.println("New message from " + m.from + " : " + m.text );
                // create new session
                if (!currentSessions.containsKey(m.from)) {
                    if (!m.text.equalsIgnoreCase("start")){
                        System.out.println("Wrong start command");
                        sendToUser(m.from, "Type /start/ for a new quiz.");
                        return;
                    } else {
                        System.out.println("Creating new session for : " + m.from);
                        Session session =  new Session(new LinkedList<Question>(allQuestions));
                        currentSessions.put(m.from,session);
                        Question newQuestion = session.nextQuestion();
                        if (newQuestion != null) {
                            sendToUser(m.from, newQuestion.getQuestionWithPossibleAnswers());
                            return;
                        }
                    }
                }
                Session session = currentSessions.get(m.from);
                if (session.currentState == Session.State.FINISHED) {
                    currentSessions.remove(m.from);
                } else if (session.currentState == Session.State.WAITING_FOR_ANSWER) {
                    Answer answer = Answer.fromString(m.text.trim());
                    if (answer == Answer.UNKNOWN) {
                        session.finish();
                        sendToUser(m.from, session.getScore());
                        currentSessions.remove(m.from);
                    } else {
                        // got answer, send new question
                        session.onAnswer(answer);
                        Question newQuestion = session.nextQuestion();
                        if (newQuestion != null) {
                            sendToUser(m.from, newQuestion.getQuestionWithPossibleAnswers());
                        } else {
                            sendToUser(m.from, session.getScore());
                        }
                    }
                }



            }
        });
    }

    private void sendToUser(String to, String text) {
        socket.emit(OP_MESSAGE, USERNAME, to, text);
    }



}
