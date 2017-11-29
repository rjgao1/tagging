package Model;

import java.io.File;
import java.util.ArrayList;

public class FileManager {

    /* The directory to be managed */
    private File directory;
    /* The list of all image files under the directory */
    private ArrayList<File> images;
    /* The suffixes of all image files */
    private static final String[] IMAGE_EXTENSION = {".png", ".jpg", ".img", "jpeg"};

    /**
     * Initiates fileManager with the directory to be managed and stores all the image files.
     *
     * @param directoryRoute the directory to be managed
     */
    public FileManager(String directoryRoute) {
        this.directory = new File(directoryRoute);
        images = getAllImages(directory);
    }

    /**
     * Returns the directory path.
     *
     * @return the path of the directory
     */
    public String getDirectoryAbsolutePath() {
        return directory.getAbsolutePath();
    }

    /**
     * Returns the list of all images under the directory.
     *
     * @return the list of all images under the directory
     */
    public ArrayList<File> getImages() {
        return images;
    }

    /**
     * Recursive helper method to get all image files under the directory.
     *
     * @param dir the directory to get the image files from
     * @return the list of all image files under the input directory
     */
    private ArrayList<File> getAllImages(File dir) {
        ArrayList<File> ret = new ArrayList<>(0);
        if (dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                if (isImage(file)) {
                    ret.add(file);
                } else if (file.isDirectory()) {
                    ret.addAll(getAllImages(file));
                }
            }
        }
        return ret;
    }

    /**
     * Returns true if the input file is a image file, i.e. the suffix is in the IMAGE_EXTENSION.
     *
     * @param file the file to check
     * @return whether the input file is a image file
     */
    private boolean isImage(File file) {
        for (String imageExtension : IMAGE_EXTENSION) {
            if (file.getPath().endsWith(imageExtension)) {
                return true;
            }
        }
        return false;
    }
}
