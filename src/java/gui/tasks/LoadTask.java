package gui.tasks;

import gui.Message;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import logic.investmentBook.InvestmentBookData;

import java.io.File;
import java.util.List;

import static gui.UserInterfaceController.LOAD_DURATION;
import static gui.UserInterfaceController.SHOW_DURATION;

public class LoadTask extends Task<InvestmentBookData> {
    private static final int HUNDRED = 100;
    private final File file;
    private final ProgressBar progressBar;
    private final Label message;
    private final SequentialTransition st = new SequentialTransition();

    public LoadTask(File file, ProgressBar progressBar, Label message) {
        this.file = file;
        this.progressBar = progressBar;
        this.message = message;
    }

    @Override
    protected InvestmentBookData call() throws Exception {
        message.setVisible(true);
        progressBar.setVisible(true);
        progressBar.progressProperty().bind(this.progressProperty());
        List<Animation> children = st.getChildren();

        for (int i = 1; i <= HUNDRED; i++) {
            PauseTransition pt = new PauseTransition(LOAD_DURATION);
            int finalI = i;
            pt.setOnFinished(actionEvent -> updateProgress(finalI, HUNDRED));
            children.add(pt);
        }
        return InvestmentBookData.fromJson(file);
    }

    @Override
    protected void succeeded() {
        st.setOnFinished(actionEvent -> {
            message.setText(Message.loaded.formatMessage(file.getName()));
            message.setVisible(true);
            PauseTransition pt = new PauseTransition(SHOW_DURATION);
            pt.setOnFinished(actionEvent2 -> {
                progressBar.progressProperty().unbind();
                progressBar.setVisible(false);
                message.setVisible(false);
                message.setText("");
            });
            pt.play();
        });
        st.play();
    }
}
