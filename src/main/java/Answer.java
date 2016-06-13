/**
 * Created by paweldylag on 13/06/16.
 */
public enum Answer {
    A("A", 0),
    B("B", 1),
    C("C", 2),
    D("D", 3),
    UNKNOWN("unknown", -1);

    private String text;
    private int index;

    Answer(String text, int index) {
        this.text = text;
        this.index = index;
    }

    public String getText() {
        return this.text;
    }

    public int getIndex() {
        return this.index;
    }

    public static Answer fromString(String text) {
        if (text != null) {
            for (Answer b : Answer.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return Answer.UNKNOWN;
    }
}
