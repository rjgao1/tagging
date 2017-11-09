package Model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.nio.file.*;


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
        if (!logFileExists()) {
            try {
                Files.write(logFilePath, tagLogsStrings, Charset.forName("UTF-8"));
            }
            catch (IOException e){
                System.err.format("error occurred when creating log file: %s%n", e);
            }
        }
    }

    public void writeLogFile() {

    }

    public void renameLogFile(String newName) throws IOException {
        Files.move(logFilePath, logFilePath.resolveSibling(newName));


    }

    public void addTagInfo(TagInfo tagInfo) {
        tagLogs.add(tagInfo);
    }

    public void registerObserver(Observer observer) {

    }

    public void deleteObserver(Observer observer) {

    }


}
