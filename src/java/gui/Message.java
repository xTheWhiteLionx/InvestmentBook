package gui;

public enum Message {
    added("successfully added"),
    deleted("successfully deleted"),
    loaded("successfully loaded"),
    saved("successfully saved");

    private final String text;

    Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String formatMessage(String string) {
        return string + " " + text;
    }
}
