package Model;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    private File directory;
    private ArrayList<String> imageFileNames;
    private ArrayList<String> directoryNames;
    private static final String[] IMAGE_EXTENSION= {".png", ".jpg",".img"};

    public FileManager(String directoryRoute) {
        this.directory = new File(directoryRoute);
        imageFileNames = new ArrayList<>(0);
        directoryNames = new ArrayList<>(0);

    }

    public ArrayList<String> getImageFileNames() {
        return imageFileNames;
    }

    public ArrayList<String> getDirectoryNames() {
        return directoryNames;
    }

    public String getDirectoryAbsolutePath() {
        return directory.getAbsolutePath();
    }

    public void renewDirectory(){
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    directoryNames.add(file.getPath());
                } else if (isImage(file)) {
                    imageFileNames.add(file.getPath());
                }

            }
        }
    }

    private boolean isImage(File file) {
        for (String imageExtension: IMAGE_EXTENSION) {
            if (file.getPath().endsWith(imageExtension)) {
                return true;
            }
        }
        return false;
    }
}
