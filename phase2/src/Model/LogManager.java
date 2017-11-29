package Model;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.nio.file.*;


public class LogManager extends Observable {

    //    private File logFile;
    private final Path logDirPath;
    private Path logFilePath;
    private ArrayList<TagInfo> tagInfos;
//    private ArrayList<String> tagInfosStrings;
//    private ArrayList<Observer> observers;
    private String modifiedImagePathString;

    public LogManager(String pathname) throws IOException {
//        String userDirString = System.getProperty("user.dir");
////        String imageFileName = pathname.substring(pathname.lastIndexOf(System.getProperty(File.separator)));
//        String logDirString = userDirString + System.getProperty("file.separator") + "Logs";
//        logDirPath = Paths.get(logDirString);
//        constructLogDirPath();
//        private void constructLogDirPath() {
        String userDirString = System.getProperty("user.dir");
        String logDirString = userDirString + System.getProperty("file.separator") + "Logs";
        logDirPath = Paths.get(logDirString);
//        }
        logFilePath = constructLogFilePath(pathname);
        if (!Files.exists(logDirPath)) {
            Files.createDirectory(logDirPath);
        }
//        File logFile = new File(pathname);

//        logFilePath = Paths.get(logFilePath.toString(), "Pathname: " + pathname);
        tagInfos = new ArrayList<>(0);
        if (!Files.exists(logFilePath)) {
            createLogFile();
        } else {
            readLogFile();
        }


    }

//    private void constructLogDirPath() {
//        String userDirString = System.getProperty("user.dir");
//        String logDirString = userDirString + System.getProperty("file.separator") + "Logs";
//        logDirPath = Paths.get(logDirString);
//    }

    private Path constructLogFilePath(String imagePath) {
        String withoutFirstSeparator = imagePath.substring(System.getProperty("file.separator").length());
        modifiedImagePathString = withoutFirstSeparator.replaceAll(System.getProperty("file.separator"), ":");
        return Paths.get(logDirPath.toString(), modifiedImagePathString + ".txt");
    }

    private boolean logFileExists() {
        return Files.exists(logFilePath);
    }

    public void createLogFile() throws IOException {
        if (!logFileExists()) {
            Files.createFile(logFilePath);
//            Files.write(logFilePath, tagInfosStrings, Charset.forName("UTF-8"));
        }
    }

    public void writeLogFile() throws IOException {
//        BufferedWriter bufferedWriter = Files.newBufferedWriter(logFilePath, Charset.forName("UTF-8"));
//        for (TagInfo tagInfo : tagInfos) {
//            bufferedWriter.write(tagInfo.toString());
//            bufferedWriter.newLine();
//        }
////        bufferedWriter.write(tagInfosStrings.get(tagInfosStrings.size() - 1));
        File logFile = logFilePath.toFile();
        FileOutputStream logFileFOS = new FileOutputStream(logFile);
        BufferedWriter logFileBW = new BufferedWriter(new OutputStreamWriter(logFileFOS));

        for (TagInfo tagInfo : tagInfos) {
            logFileBW.write(tagInfo.toString());
            logFileBW.newLine();
        }
        logFileBW.close();
        logFileFOS.close();

    }

    public void readLogFile() throws  IOException {
        BufferedReader logBR = new BufferedReader(new FileReader(logFilePath.toString()));

        String line = logBR.readLine();
        while (line != null) {
            tagInfos.add(TagInfo.stringToTagInfo(line));
            line = logBR.readLine();
        }
        logBR.close();
    }

    public void renameLogFile(String newPath, boolean isImage) throws IOException {
        /**
         * String substring is of of all the tags of the image whose log is to be renamed,
         * i.e. everything between the image label and the suffix.
         */
        if (isImage) {
            Path newLogFilePath = constructLogFilePath(newPath);
            Files.move(logFilePath, newLogFilePath);
            logFilePath = newLogFilePath;
        }
        else {
//            Files.move(logFilePath, logFilePath.resolveSibling(modifiedImagePathString));
            Path result = Paths.get(newPath);
            Files.move(logFilePath, result);
            logFilePath = result;
        }
    }

    private String tagListStringToPathString (String newTagListString) {
        int index = modifiedImagePathString.indexOf(" @", modifiedImagePathString.lastIndexOf(":"));
        String substring;
        if (index != -1) {
            substring = modifiedImagePathString.substring(index, modifiedImagePathString.lastIndexOf("."));
            modifiedImagePathString = modifiedImagePathString.replaceAll(substring, newTagListString);
        } else {
            substring = modifiedImagePathString.substring(modifiedImagePathString.lastIndexOf(":"),
                    modifiedImagePathString.lastIndexOf("."));
            modifiedImagePathString = modifiedImagePathString.replaceAll(substring,
                    substring + newTagListString);
        }

        String result = logFilePath.resolveSibling(modifiedImagePathString).toString() + ".txt";

        return result;
    }

    public void addTagInfo(TagInfo tagInfo) throws IOException {
        tagInfos.add(tagInfo);
        String tagListString = " " + tagInfo.getTagListString();
        tagListString = tagListString.substring(0, tagListString.length() - 1);
        writeLogFile();
        renameLogFile(tagListStringToPathString(tagListString), false);
        for (Observer observer: observers) {
            observer.update();
        }
    }

//    public void registerObserver(Observer observer) {
//        observers.add(observer);
//    }
//
//    public void deleteObserver(Observer observer) {
//        observers.remove(observer);
//    }

    public ArrayList<TagInfo> getTagInfos() {
        return tagInfos;
    }


}
