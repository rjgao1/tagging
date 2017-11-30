package Model;

import java.io.*;
import java.util.ArrayList;
import java.nio.file.*;


public class LogManager extends Observable {

    /* The path of log directory */
    private final Path logDirPath;
    /* The path of the logFile */
    private Path logFilePath;
    /* The list of TagInfos of this LogManager */
    private ArrayList<TagInfo> tagInfos;
    /* The string representing the path of the modified Image */
    private String modifiedImagePathString;

    public LogManager(String pathname) throws IOException {

        String userDirString = System.getProperty("user.dir");
        String logDirString = userDirString + System.getProperty("file.separator") + "Logs";
        logDirPath = Paths.get(logDirString);

        logFilePath = constructLogFilePath(pathname);
        if (!Files.exists(logDirPath)) {
            Files.createDirectory(logDirPath);
        }

        tagInfos = new ArrayList<>(0);
        if (!Files.exists(logFilePath)) {
            createLogFile();
        } else {
            readLogFile();
        }


    }


    private Path constructLogFilePath(String imagePath) {
        String withoutFirstSeparator = imagePath.substring(System.getProperty("file.separator").length());
        modifiedImagePathString = withoutFirstSeparator.replaceAll(System.getProperty("file.separator"), ":");
        return Paths.get(logDirPath.toString(), modifiedImagePathString + ".txt");
    }

    private boolean logFileExists() {
        return Files.exists(logFilePath);
    }

    private void createLogFile() throws IOException {
        if (!logFileExists()) {
            Files.createFile(logFilePath);
        }
    }

    private void writeLogFile() throws IOException {
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

    private void readLogFile() throws IOException {
        BufferedReader logBR = new BufferedReader(new FileReader(logFilePath.toString()));

        String line = logBR.readLine();
        while (line != null) {
            tagInfos.add(TagInfo.stringToTagInfo(line));
            line = logBR.readLine();
        }
        logBR.close();
    }

    public void renameLogFile(String newPath, boolean isImage) throws IOException {

/*        String substring is of of all the tags of the image whose log is to be renamed,
        i.e. everything between the image label and the suffix.*/

        if (isImage) {
            Path newLogFilePath = constructLogFilePath(newPath);
            Files.move(logFilePath, newLogFilePath);
            logFilePath = newLogFilePath;
        } else {
            Path result = Paths.get(newPath);
            Files.move(logFilePath, result);
            logFilePath = result;
        }
    }

    private String tagListStringToPathString(String newTagListString) {
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

        return logFilePath.resolveSibling(modifiedImagePathString).toString() + ".txt";
    }

    public void addTagInfo(TagInfo tagInfo) throws IOException {
        tagInfos.add(tagInfo);
        String tagListString = " " + tagInfo.getTagListString();
        tagListString = tagListString.substring(0, tagListString.length() - 1);
        writeLogFile();
        renameLogFile(tagListStringToPathString(tagListString), false);
        notifyObserver();
    }



    public ArrayList<TagInfo> getTagInfos() {
        return tagInfos;
    }


}
