package Model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.nio.file.*;


public class LogManager {

    //    private File logFile;
    private Path logDirPath;
    private Path logFilePath;
    private ArrayList<TagInfo> tagInfos;
    private ArrayList<String> tagInfosStrings;
    private ArrayList<Observer> observers;

    public LogManager(String pathname) throws IOException {
//        String userDirString = System.getProperty("user.dir");
////        String imageFileName = pathname.substring(pathname.lastIndexOf(System.getProperty(File.separator)));
//        String logDirString = userDirString + System.getProperty("file.separator") + "Logs";
//        logDirPath = Paths.get(logDirString);
        constructLogFilePath(pathname);
        constructLogDirPath();
        if (!Files.exists(logDirPath)) {
            Files.createDirectory(logDirPath);
        }
//        File logFile = new File(pathname);

//        logFilePath = Paths.get(logFilePath.toString(), "Pathname: " + pathname);
        for (TagInfo element : tagInfos) {
            tagInfosStrings.add(element.toString());
        }


    }

    private void constructLogDirPath() {
        String userDirString = System.getProperty("user.dir");
        String logDirString = userDirString + System.getProperty("file.separator") + "Logs";
        logDirPath = Paths.get(logDirString);
    }

    private void constructLogFilePath(String imagePath) {
        String modifiedImagePath;
        modifiedImagePath = imagePath.replaceAll(System.getProperty("file.separator"), ":");
        logFilePath = Paths.get(logFilePath.toString(), "Pathname: " + modifiedImagePath);
    }

    private boolean logFileExists() {
        return Files.exists(logFilePath);
    }

    public void createLogFile() throws IOException {
        if (!logFileExists()) {
            Files.write(logFilePath, tagInfosStrings, Charset.forName("UTF-8"));
        }
    }

    public void writeLogFile() throws IOException {
        if (!logFileExists()) {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(logFilePath, Charset.forName("UTF-8"));
            bufferedWriter.write(tagInfosStrings.get(tagInfosStrings.size() - 1));
        }
    }

    public void renameLogFile(String newName) throws IOException {
        Files.move(logFilePath, logFilePath.resolveSibling(newName));


    }

    public void addTagInfo(TagInfo tagInfo) {
        tagInfos.add(tagInfo);
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    public ArrayList<TagInfo> getTagInfos() {
        return tagInfos;
    }


}
