package FrontEnd;

import Model.Config;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {

    private static int mainWindowCount = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (!Config.hasConfigFile()) {
            primaryStage = FXMLLoader.load(getClass().getResource("ConfigPage.fxml"));
            primaryStage.show();
        } else {
            primaryStage = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        }
    }

    public static int getMainWindowCount() {
        return mainWindowCount;
    }

    public static void setMainWindowCount(int newCount) {
        mainWindowCount = newCount;
    }

    public static void incrementMainWindowCount() {
        mainWindowCount++;
    }

    public static void decreaseMainWindowCount() {
        mainWindowCount--;
        if (mainWindowCount == 0) {
            Platform.exit();
        }
    }


}
