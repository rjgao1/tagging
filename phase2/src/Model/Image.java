package Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Image extends Observable implements Observer {

    private LogManager logManager;
//    private ArrayList<Observer> observers;
    private File file;

    public Image(String pathname) throws IOException{
        logManager = new LogManager(pathname);
        logManager.registerObserver(this);
        file = new File(pathname);
    }

    public void rename(String newPathname) throws IOException{
        Files.move(file.toPath(), Paths.get(newPathname));
        file = new File(newPathname);
        for (Observer observer : observers) {
            observer.update();
        }
    }

//    public void registerObserver(Observer observer) {
//        observers.add(observer);
//    }
//
//    public void deleteObserver(Observer observer) {
//        observers.remove(observer);
//    }

    public boolean hasObserver(Observer observer) {
        return observers.contains(observer);
    }

    public LogManager getLogManager() {
        return logManager;
    }

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

    public File getFile() {
        return this.file;
    }

    @Override
    public void update() throws IOException{
        String newFileName = file.getAbsolutePath();
        int index;
        index = newFileName.indexOf(" @", newFileName.lastIndexOf(System.getProperty("file.separator")) - 1);
        String postfix = newFileName.substring(newFileName.lastIndexOf("."));
        newFileName = newFileName.substring(0, newFileName.lastIndexOf("."));
        if (index != -1) {
            newFileName = newFileName.substring(0, index);
        }
        for (Tag tag: logManager.getTagInfos().get(logManager.getTagInfos().size() - 1).getTagList()) {
            newFileName = newFileName + " @" + tag.getContent();
        }
        newFileName = newFileName + postfix;
        rename(newFileName);
    }
}
