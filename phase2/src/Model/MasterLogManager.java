package Model;

import java.io.*;
import java.util.ArrayList;
import java.nio.file.*;

/**
 * A class that only contains static members and methods. It maintains the Master Log.txt file which keeps
 * track of all renamings that all images have ever done.
 */
public class MasterLogManager {
    /* A static ArrayList containing all master logs in the form of yyyy/MM/dd HH:mm:ss|oldName -> newName */
    private static ArrayList<String> masterLogs = new ArrayList<>(0);
    /* The Path of the master log file which is under user.dir/Logs */
    private static final Path masterLogPath = Paths.get(System.getProperty("user.dir"), "Logs", "Master Log.txt");

    /**
     * Private declaration of the no-argument constructor so this utility class cannot be instantiated by another class
     */
    private MasterLogManager() {
    }

    /**
     * @return whether or not the master log file exists
     */
    public static boolean masterLogExists() {
        return Files.exists(masterLogPath);
    }

    /**
     * Reads the master log file and populates ArrayList masterLogs
     *
     * @throws IOException if the file/directory represented by Path masterLogPath does not exist
     */
    public static void readMasterLogs() throws IOException {
        BufferedReader masterLogBR = new BufferedReader(new FileReader(masterLogPath.toString()));

        String line = masterLogBR.readLine();
        while (line != null) {
            masterLogs.add(line);
            line = masterLogBR.readLine();
        }
        masterLogBR.close();
    }

    /**
     * Creates an empty master log file with Path masterLogPath if the file does not already exist
     *
     * @throws IOException if the file/directory represented by Path masterLogPath already exists
     */
    public static void createMaterLogs() throws IOException {
        if (!masterLogExists()) {
            Files.createFile(masterLogPath);
        }
    }

    /**
     * Writes a new piece of master log to the master log file and add the master log to the masterLogs ArrayList
     *
     * @param time    the time at which the renaming is done
     * @param oldName old pathname of the Image file
     * @param newName new pathname of the Image file
     * @throws IOException if the file/directory represented by Path masterLogPath does not exist
     */
    static void writeMasterLogs(String time, String oldName, String newName) throws IOException {
        String newMasterLog = time + "|" + oldName + " -> " + newName;

        File masterLogFile = masterLogPath.toFile();
        FileWriter masterLogFW = new FileWriter(masterLogFile, true);
        BufferedWriter masterLogBW = new BufferedWriter(masterLogFW);

        masterLogBW.write(newMasterLog);
        masterLogs.add(newMasterLog);
        masterLogBW.newLine();

        masterLogBW.close();
        masterLogFW.close();
    }

    /**
     * @return the ArrayList of String masterLogs
     */
    public static ArrayList getMasterLogs() {
        return masterLogs;
    }

}
