package gui;

public enum Status {
    added("successfully added"),
    deleted("successfully deleted"),
    loaded("successfully loaded"),
    saved("successfully saved");

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String formatMessage(String string) {
        return string + " " + message;
    }
}
