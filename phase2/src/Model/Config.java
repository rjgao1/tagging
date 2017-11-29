package Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Config {

    /* The root directory path. */
    private static String defaultPath = "";
    /* The path of the config file. */
    private static final File configFile = new File(System.getProperty("user.dir"), "config.txt");

    /**
     * Returns true if the config file is in the project directory, false otherwise.
     *
     * @return whether the config file is in the project directory
     */
    public static boolean hasConfigFile() {
        return configFile.isFile();
    }

    /**
     * Reads the config file to get the root directory path.
     * Behaviour is undefine if the config file is empty or the first line is not a valid directory path
     *
     * @throws IOException if the config file is empty
     */
    public static void readConfigFile() throws IOException {
        BufferedReader configBR = new BufferedReader(new FileReader(configFile));

        defaultPath = configBR.readLine();
        File defaultPathFile = new File(defaultPath);
        if (!defaultPathFile.isDirectory()) {
            throw new ClassCastException();
        }

        configBR.close();
    }

    /**
     * Deletes the config file if there is one.
     */
    public static void deleteConfigFile() {
        configFile.delete();
    }

    /**
     * Creates the config file.
     *
     * @throws IOException if there is an existing config file
     */
    public static void createConfigFile() throws IOException {
        configFile.createNewFile();
    }

    /**
     * Writes the root directory path to the file.
     *
     * @throws IOException if config file does not exist
     */
    public static void writeConfigFile() throws IOException {
        FileOutputStream configFOS = new FileOutputStream(configFile);
        BufferedWriter configBW = new BufferedWriter(new OutputStreamWriter(configFOS));

        configBW.write(defaultPath);

        configBW.close();
        configFOS.close();
    }

    /**
     * Sets root directory to the input path.
     *
     * @param defaultPath the path root directory to be set to
     */
    public static void setDefaultPath(String defaultPath) {
        Config.defaultPath = defaultPath;
    }

    /**
     * Returns the root directory.
     *
     * @return the root directory
     */
    public static String getDefaultPath() {
        return defaultPath;
    }
}
