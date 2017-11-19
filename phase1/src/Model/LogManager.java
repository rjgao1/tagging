package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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
    private String modifiedImagePathString;

    public LogManager(String pathname) throws IOException {
//        String userDirString = System.getProperty("user.dir");
////        String imageFileName = pathname.substring(pathname.lastIndexOf(System.getProperty(File.separator)));
//        String logDirString = userDirString + System.getProperty("file.separator") + "Logs";
//        logDirPath = Paths.get(logDirString);
        constructLogDirPath();
        constructLogFilePath(pathname);
        if (!Files.exists(logDirPath)) {
            Files.createDirectory(logDirPath);
        }
//        File logFile = new File(pathname);

//        logFilePath = Paths.get(logFilePath.toString(), "Pathname: " + pathname);
        if (!Files.exists(logFilePath)) {
            createLogFile();
        }
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
        modifiedImagePathString = imagePath.replaceAll(System.getProperty("file.separator"), ":") + ".txt";
        logFilePath = Paths.get(logDirPath.toString(), "Pathname: " + modifiedImagePathString);
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
        BufferedWriter bufferedWriter = Files.newBufferedWriter(logFilePath, Charset.forName("UTF-8"));
        bufferedWriter.write(tagInfosStrings.get(tagInfosStrings.size() - 1));
    }

    public void readLogFile() throws  IOException {
        BufferedReader logBR = new BufferedReader(new FileReader(logFilePath.toString()));

        String line = logBR.readLine();
        while (line != null) {
            tagInfos.add(TagInfo.stringToTagInfo(line));
        }
    }

    public void renameLogFile(String newTagListString) throws IOException {
        /**
         * String substring is of of all the tags of the image whose log is to be renamed,
         * i.e. everything between the image label and the suffix.
         */
        int index = modifiedImagePathString.indexOf(" @", modifiedImagePathString.lastIndexOf(":"));
        String substring;
        if (index != -1) {
            substring = modifiedImagePathString.substring(index, modifiedImagePathString.lastIndexOf("."));
            modifiedImagePathString = modifiedImagePathString.replaceAll(substring, newTagListString);
        } else {
            substring = modifiedImagePathString.substring(modifiedImagePathString.lastIndexOf(":"),
                    modifiedImagePathString.lastIndexOf("."));
            modifiedImagePathString = modifiedImagePathString.replaceAll(substring, newTagListString);
        }
        Files.move(logFilePath, logFilePath.resolveSibling(modifiedImagePathString));
    }

    public void addTagInfo(TagInfo tagInfo) throws IOException {
        String tagListString = tagInfo.getTagListString();
        tagInfos.add(tagInfo);
        writeLogFile();
        renameLogFile(tagListString);
        for (Observer observer: observers) {
            observer.update();
        }
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
