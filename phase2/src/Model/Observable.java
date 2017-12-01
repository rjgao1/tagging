package Model;

import java.util.ArrayList;
import java.io.*;

/**
 * An abstract Observable class extended by LogManager and Image
 * The super class can not be instantiated
 */
abstract public class Observable {
    /* Observer's observing the observable */
    private ArrayList<Observer> observers = new ArrayList<>(0);

    /**
     * Registers the observer to the observers ArrayList
     *
     * @param observer the Observer to be registered
     */
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Deletes the observer from the observers ArrayList
     *
     * @param observer the Observer to be deleted
     */
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all Observer's in observers ArrayList
     *
     * @throws IOException if the Observer's update methods throws an IOException
     */
    public void notifyObserver() throws IOException {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
