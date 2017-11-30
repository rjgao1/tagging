package Model;

import java.util.ArrayList;
import java.io.*;

abstract public class Observable {
    ArrayList<Observer> observers = new ArrayList<>(0);

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObserver() throws IOException {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
