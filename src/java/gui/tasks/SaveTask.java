package gui.tasks;

import gui.Message;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import logic.investmentBook.InvestmentBook;
import logic.investmentBook.InvestmentBookData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static gui.DialogWindow.displayError;
import static gui.UserInterfaceController.LOAD_DURATION;
import static gui.UserInterfaceController.SHOW_DURATION;

public class SaveTask extends Task<Void> {

    private static final int HUNDRED = 100;
    private final File file;
    private final InvestmentBook investmentBook;
    private final ProgressBar progressBar;
    private final Label message;
    private final SequentialTransition st = new SequentialTransition();

    public SaveTask(File file, InvestmentBook investmentBook, ProgressBar progressBar,
                    Label message) {
        this.file = file;
        this.investmentBook = investmentBook;
        this.progressBar = progressBar;
        this.message = message;
    }

    @Override
    protected Void call() {
        message.setVisible(true);
        progressBar.setVisible(true);
        progressBar.progressProperty().bind(this.progressProperty());
        List<Animation> children = st.getChildren();


        for (int i = 1; i <= HUNDRED; i++) {
            PauseTransition pt =
                    new PauseTransition(LOAD_DURATION);
            int finalI = i;
            pt.setOnFinished(actionEvent -> updateProgress(finalI, HUNDRED));
            children.add(pt);
        }
        InvestmentBookData investmentBookData = new InvestmentBookData(investmentBook);
        try {
            investmentBookData.toJson(file);
        } catch (IOException e) {
            displayError(e);
        }
        return null;
    }

    @Override
    protected void succeeded() {
        st.setOnFinished(actionEvent -> {
            message.setText(Message.saved.formatMessage(file.getName()));
            PauseTransition pt = new PauseTransition(SHOW_DURATION);
            pt.setOnFinished(actionEvent2 -> {
                message.setText("");
                message.setVisible(false);
                progressBar.setVisible(false);
                progressBar.progressProperty().unbind();
            });
            pt.play();
        });
        st.play();
    }
}
