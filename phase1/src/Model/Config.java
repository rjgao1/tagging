package Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Config {

    private static boolean viewTags;
    private static String defaultPath;
    private static File configFile;

    public static boolean hasConfigFile() {
        String dir = System.getProperty("user.dir");
        configFile = new File(dir, "config.txt");
        return configFile.isFile();
    }

    public static void readConfigFile() throws IOException{
        BufferedReader configBR = new BufferedReader(new FileReader(configFile));

        viewTags = Boolean.getBoolean(configBR.readLine());
        defaultPath = configBR.readLine();

        configBR.close();
    }

    public static void writeConfigFile() throws IOException{
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
