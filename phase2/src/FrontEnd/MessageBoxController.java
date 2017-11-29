package FrontEnd;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MessageBoxController {

    @FXML
    private Label messageLabel;
    @FXML
    private Stage stage;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public Stage getStage() {
        return stage;
    }

    public void okButtonClicked() {
        stage.close();
    }

}
