package Model;

import java.io.File;
import java.util.ArrayList;

public class Image {

    private LogManager logManager;
    private ArrayList<Observer> observers;
    private File file;

    public Image(String pathname) {
        logManager = new LogManager(pathname);
        observers = new ArrayList<>(0);
        file = new File(pathname);
    }

    public void rename(String newPathname) {
        file.renameTo(new File(newPathname));
        for (Observer observer: observers) {
            observer.update();
        }
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    public boolean hasObserver(Observer observer) {
        return observers.contains(observer);
    }

    public LogManager getLogManager() {
        return logManager;
    }

    public static Tag[] getTagsFromName(String filename) {
        Tag[] tags;
        filename = filename.substring(filename.lastIndexOf(System.getProperty("file.separator")));
        String[] array = filename.split(" @");
        tags = new Tag[array.length - 1];
        for (int i = 1; i < array.length; i++) {
            tags[i - 1] = new Tag(array[i]);
        }
        return tags;
    }

    public File getFile() {
        return this.file;
    }
}
