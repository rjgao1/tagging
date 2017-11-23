package Model;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    private File directory;
    private ArrayList<File> images;
    private static final String[] IMAGE_EXTENSION= {".png", ".jpg",".img", "jpeg"};

    public FileManager(String directoryRoute) {
        this.directory = new File(directoryRoute);
        images = getAllImages(directory);
    }

    public String getDirectoryAbsolutePath() {
        return directory.getAbsolutePath();
    }

    public ArrayList<File> getImages() {
        return images;
    }

    public ArrayList<File> getAllImages(File dir) {
        ArrayList<File> ret = new ArrayList<>(0);
        for (File file: dir.listFiles()) {
            if (isImage(file)) {
                ret.add(file);
            } else if (file.isDirectory()) {
                ret.addAll(getAllImages(file));
            }
        }
        return ret;
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
