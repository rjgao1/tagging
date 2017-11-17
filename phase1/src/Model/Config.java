package Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;

public class Config {

    private static boolean viewTags;
    private static String defaultPath;
    private static final File configFile = new File(System.getProperty("user.dir"), "config.txt");

    public static boolean hasConfigFile() {
        return configFile.isFile();
    }

    public static void readConfigFile() throws IOException, ClassCastException {
        BufferedReader configBR = new BufferedReader(new FileReader(configFile));

        String line = configBR.readLine();
        if (!(line.equals("true") || line.equals("false"))) {
            throw new ClassCastException();
        }
        viewTags = Boolean.getBoolean(line);
        defaultPath = configBR.readLine();
        File defaultPathFile = new File(defaultPath);
        if (!defaultPathFile.isDirectory()) {
            throw new ClassCastException();
        }

        configBR.close();
    }

    public static void deleteConfigFile() {
        configFile.delete();
    }

    public static void createConfigFile() {
        try {
            configFile.createNewFile();
        } catch (IOException e) {

        }
    }


    public static void writeConfigFile() throws IOException {
        FileOutputStream configFOS = new FileOutputStream(configFile);
        BufferedWriter configBW = new BufferedWriter(new OutputStreamWriter(configFOS));

        configBW.write(String.valueOf(viewTags));
        configBW.newLine();
        configBW.write(defaultPath);

        configBW.close();
        configFOS.close();
    }

    public static void setViewTags(boolean viewTags) {
        Config.viewTags = viewTags;
    }

    public static boolean getViewTags() {
        return Config.viewTags;
    }

    public static void setDefaultPath(String defaultPath) {
        Config.defaultPath = defaultPath;
    }

    public static String getDefaultPath() {
        return defaultPath;
    }
}
