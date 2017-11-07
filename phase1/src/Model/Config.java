package Model;

public class Config {

    private static boolean viewTags;

    public static boolean hasConfigFile() {
        return true;
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

}
