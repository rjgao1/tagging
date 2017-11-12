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
}
