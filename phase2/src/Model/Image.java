package Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Image extends Observable implements Observer {

    /* The logManager of the image file */
    private LogManager logManager;
    /* The path of the image file */
    private File file;

    /**
     * Initiates the Image with its path and register to its logManager.
     *
     * @param pathname the path of the image file
     * @throws IOException if there is not file on the input path
     */
    public Image(String pathname) throws IOException {
        logManager = new LogManager(pathname);
        logManager.registerObserver(this);
        file = new File(pathname);
    }

    /**
     * Renames the image file to the input path.
     *
     * @param newPathname the new path of the image file
     * @throws IOException if there is another file on the input path
     */
    public void rename(String newPathname) throws IOException {
        Files.move(file.toPath(), Paths.get(newPathname));
        file = new File(newPathname);
        notifyObserver();
    }

    /**
     * Returns the logManager of the image file.
     *
     * @return the logManager of the image file.
     */
    public LogManager getLogManager() {
        return logManager;
    }

    /**
     * Returns the tags of the file.
     *
     * @param filename name of the file
     * @return the tags in the file name
     */
    public static Tag[] getTagsFromName(String filename) {
        Tag[] tags;
        filename = filename.substring(filename.lastIndexOf(System.getProperty("file.separator")) + 1);
        filename = filename.substring(0, filename.lastIndexOf("."));
        String[] array = filename.split(" @");
        tags = new Tag[array.length - 1];
        for (int i = 1; i < array.length; i++) {
            tags[i - 1] = new Tag(array[i]);
        }
        return tags;
    }

    /**
     * Returns the image file.
     *
     * @return the image file.
     */
    public File getFile() {
        return this.file;
    }

    @Override
    /**
     * Renames image file to the new name according to the latest tags in the logManager.
     */
    public void update() throws IOException {
        String newFileName = file.getAbsolutePath();
        int index;
        index = newFileName.indexOf(" @", newFileName.lastIndexOf(System.getProperty("file.separator")) - 1);
        String postfix = newFileName.substring(newFileName.lastIndexOf("."));
        newFileName = newFileName.substring(0, newFileName.lastIndexOf("."));
        if (index != -1) {
            newFileName = newFileName.substring(0, index);
        }
        for (Tag tag : logManager.getTagInfos().get(logManager.getTagInfos().size() - 1).getTagList()) {
            newFileName = newFileName + " @" + tag.getContent();
        }
        newFileName = newFileName + postfix;
        rename(newFileName);
    }
}
