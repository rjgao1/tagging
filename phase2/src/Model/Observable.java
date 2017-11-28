package Model;
import java.util.ArrayList;

public class Observable {
    ArrayList<Observer> observers;
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }
}
