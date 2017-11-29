package FrontEnd;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MessageBoxController {

    @FXML
    /* The label to show message */
    private Label messageLabel;
    @FXML
    /* The stage of this window */
    private Stage stage;

    /**
     * Sets the message label.
     *
     * @param message the message to set to the label
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    /**
     * Closes the window when OK button is clicked.
     */
    public void okButtonClicked() {
        stage.close();
    }

}
