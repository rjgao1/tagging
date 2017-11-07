package Model;

import java.util.ArrayList;

public class FileManager {

    private String directoryRoute;
    private ArrayList<String> imageFileNames;
    private ArrayList<String> directoryNames;

    FileManager(String directoryRoute) {
        this.directoryRoute = directoryRoute;
    }

    public ArrayList<String> getImageFileNames() {
        return imageFileNames;
    }

    public ArrayList<String> getDirectoryNames() {
        return directoryNames;
    }
}
