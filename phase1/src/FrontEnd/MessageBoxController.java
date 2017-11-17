package FrontEnd;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessageBoxController {

    private String title;
    @FXML
    private String message;
    @FXML
    private Label messageLabel;

    public MessageBoxController(String message, String title) {
        this.message = message;
        this.title = title;
    }

    @FXML
    public void initialize() {
        ((Stage) messageLabel.getScene().getWindow()).setTitle(title);
        ((Stage) messageLabel.getScene().getWindow()).setResizable(false);
        ((Stage) messageLabel.getScene().getWindow()).initStyle(StageStyle.UNDECORATED);
    }

    public void okButtonClicked(){
        ((Stage) messageLabel.getScene().getWindow()).close();
    }

}
