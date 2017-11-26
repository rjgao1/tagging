package Model;

import java.io.IOException;
import java.util.*;
import java.io.*;

public class Tag {

    private final String content;
    private static Set<Tag> tagSet;
    private static String defaultPath = "";
    private static final File tagFile = new File(System.getProperty("user.dir"), "config.txt");

    public Tag(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag && this.content.equals(((Tag) obj).content);
    }

    public static Set<Tag> getTagSet(){
        return tagSet;
    }

    public static void setTagSet(Set<Tag> newTagSet){
        Tag.tagSet = newTagSet;
    }

    public static void addTagToSet(Tag newTag){
        if (!Tag.tagSet.contains(newTag)) {
            Tag.tagSet.add(newTag);
        }
    }

    public static void addTagToSet(Tag[] newTagList){
        for (Tag element : newTagList) {
            if (!Tag.tagSet.contains(element)) {
                Tag.tagSet.add(element);
            }
        }
    }

    public static void writeTagFile() throws IOException {
        FileOutputStream tagFOS = new FileOutputStream(tagFile);
        BufferedWriter tagBW = new BufferedWriter(new OutputStreamWriter(tagFOS));
        tagBW.write(defaultPath);
        tagBW.close();
        tagFOS.close();
    }

    public static void readTagFile() throws IOException {
        BufferedReader tagBR = new BufferedReader(new FileReader(tagFile));
        defaultPath = tagBR.readLine();
        File defaultPathFile = new File(defaultPath);
        if (!defaultPathFile.isDirectory()) {
            throw new ClassCastException();
        }
        tagBR.close();
    }

    public static void createTagFile() throws IOException {
        tagFile.createNewFile();
    }
}
