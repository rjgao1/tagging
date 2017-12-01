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
    /* The string representing the Image's modified pathname used in the log file's naming */
    private String modifiedImagePathString;

    /**
     * Constructs a LogManager for an Image with pathname
     *
     * @param pathname pathname of the Image for which the LogManager is constructed for.
     * @throws IOException if directory/file represented by pathname does not exist or conflicted directory/file.
     */
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

    /**
     * Returns a Path to the log file for the image represented by imagePath
     *
     * @param imagePath pathname String of the image
     * @return a Path to the log file for the image represented by imagePath
     */
    private Path constructLogFilePath(String imagePath) {
        String withoutFirstSeparator = imagePath.substring(System.getProperty("file.separator").length());
        modifiedImagePathString = withoutFirstSeparator.replaceAll(System.getProperty("file.separator"), ":");
        return Paths.get(logDirPath.toString(), modifiedImagePathString + ".txt");
    }

    /**
     * Returns whether or not the log file of the LogManager exist
     *
     * @return whether or not the log file of the LogManager exist
     */
    private boolean logFileExists() {
        return Files.exists(logFilePath);
    }

    /**
     * Creates an empty log file if it does already not exist
     *
     * @throws IOException if the file/director represented by logFilePath already exists
     */
    private void createLogFile() throws IOException {
        if (!logFileExists()) {
            Files.createFile(logFilePath);
        }
    }

    /**
     * Writes TagInfo's in tagInfos to the log file if it already exists
     *
     * @throws IOException if the log file does not exist
     */
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

    /**
     * Reads the log file, collects TagInfo's from it, and populates tagInfos
     *
     * @throws IOException if directory/file represented by logFilePath does not exist
     */
    private void readLogFile() throws IOException {
        BufferedReader logBR = new BufferedReader(new FileReader(logFilePath.toString()));

        String line = logBR.readLine();
        while (line != null) {
            tagInfos.add(TagInfo.stringToTagInfo(line));
            line = logBR.readLine();
        }
        logBR.close();
    }

    /**
     * Renames the log file based on the newPath of the Image file, if the file represented by newPath isImage
     * Renames the log file directly to the newPath, if the newPath !isImage
     *
     * @param newPath the new pathname String the renaming is based on
     * @param isImage whether or not newPath represents the new pathname of an Image file
     * @throws IOException if file/directory represented by newPath already exists
     */
    public void renameLogFile(String newPath, boolean isImage) throws IOException {
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

    /**
     * Returns a new logFilePath to reflect the current set of tags of the Image file
     * associated with the log file
     *
     * @param newTagListString String of the current tags of the Image file
     * @return a String representation of the pathname of the log file to reflects the current tags if its Image file
     */
    private String tagListStringToPathString(String newTagListString) {
        int index = modifiedImagePathString.indexOf(" @", modifiedImagePathString.lastIndexOf(":"));

/*        String substring is of of all the tags of the image whose log is to be renamed,
        i.e. everything between the image label and the suffix.*/
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

    /**
     * Adds a TagInfo object to the list TagInfos, writes and renames the logFile accordingly and notifies the
     * Observer's.
     *
     * @param tagInfo a TagInfo object to be added to tagInfos
     * @throws IOException if the logFile does not exist or attempt to rename the log file to a conflicted pathname
     */
    public void addTagInfo(TagInfo tagInfo) throws IOException {
        tagInfos.add(tagInfo);
        String tagListString = " " + tagInfo.getTagListString();
        tagListString = tagListString.substring(0, tagListString.length() - 1);
        writeLogFile();
        renameLogFile(tagListStringToPathString(tagListString), false);
        notifyObserver();
    }

    /**
     * Returns a list of all TagInfos in the log file created by the LogManager.
     *
     * @return this LogManager's tagInfos
     */
    public ArrayList<TagInfo> getTagInfos() {
        return tagInfos;
    }


}
