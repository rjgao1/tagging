package Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.nio.file.*;


public class LogManager {

    //    private File logFile;
    private Path logFilePath;
    private ArrayList<TagInfo> tagLogs;
    private ArrayList<String> tagLogsStrings;
    private ArrayList<Observer> observers;

    public LogManager(String pathname) {
        File logFile = new File(pathname);
        logFilePath = logFile.toPath();
        for (TagInfo element : tagLogs) {
            tagLogsStrings.add(element.toString());
        }
    }

    private boolean logFileExists() {
        return Files.exists(logFilePath);
    }

    public void createLogFile() throws IOException {
        if (!logFileExists()) {
            Files.write(logFilePath, tagLogsStrings, Charset.forName("UTF-8"));
        }
    }

    public void writeLogFile() throws IOException {
        if (!logFileExists()) {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(logFilePath, Charset.forName("UTF-8"));
            bufferedWriter.write(tagLogsStrings.get(tagLogsStrings.size() - 1));
        }
    }

    public void renameLogFile(String newName) throws IOException {
        Files.move(logFilePath, logFilePath.resolveSibling(newName));


    }

    public void addTagInfo(TagInfo tagInfo) {
        tagLogs.add(tagInfo);
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }


}
