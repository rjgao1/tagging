package Model;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.nio.file.*;
import java.nio.charset.*;

public class LogManager {

    private File logFile;
    private Path logFilePath;
    private ArrayList<TagInfo> tagLogs;
    private ArrayList<String> tagLogsStrings;
    private ArrayList<Observer> observers;

    public LogManager(String pathname) {
        logFile = new File(pathname);
        logFilePath = logFile.toPath();
        for (TagInfo element : tagLogs) {
            tagLogsStrings.add(element.toString());
        }
    }

    private boolean logFileExists() {
        return Files.exists(logFilePath);
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
