package Model;

import java.io.File;
import java.util.ArrayList;
import java.nio.file.*;

public class LogManager {

    private File logFile;
    private ArrayList<TagInfo> tagLogs;
    private ArrayList<Observer> observers;

    public LogManager(String pathname) {
        logFile = new File(pathname);
    }

    private boolean logFileExists() {
        Path filePath = logFile.toPath();
        if (Files.exists(filePath)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void createLogFile() {

    }

    public void writeLogFile() {

    }

    public void renameLogFile() {

    }

    public void addTagInfo(TagInfo tagInfo) {
        tagLogs.add(tagInfo);
    }

    public void registerObserver(Observer observer) {

    }

    public void deleteObserver(Observer observer) {

    }


}
