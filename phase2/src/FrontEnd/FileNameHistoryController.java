package FrontEnd;

import Model.MasterLogManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class FileNameHistoryController {

    @FXML
            /* The list of all logs in master log*/
            ListView<String> masterLogListView;
    @FXML
            /* The stage of this window */
            Stage stage;

    @FXML
    /**
     * Loads the data from MasterLogManager.
     */
    public void initialize() {
        ObservableList<String> masterLogList = FXCollections.observableList(MasterLogManager.getMasterLogs());
        masterLogListView.setItems(masterLogList);
    }

    /**
     * Closes this window.
     */
    public void closeWindow() {
        stage.close();
    }

}
