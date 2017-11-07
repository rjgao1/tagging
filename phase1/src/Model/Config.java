package Model;

import java.io.File;

public class Config {

    private static boolean viewTags;
    private static String defaultPath;

    public static boolean hasConfigFile() {
        String dir = System.getProperty("user.dir");
        File configFile = new File(dir, "config.txt");
        return configFile.isFile();
    }

    public static void readConfigFile() {

    }

    public static void writeConfigFile() {

    }

    public static void setViewTags(boolean viewTags) {
        Config.viewTags = viewTags;
    }

    public static boolean getViewTags() {
        return Config.viewTags;
    }

    public static String getDefaultPath() {
        return defaultPath;
    }
}
