package Model;

import java.io.IOException;
import java.util.*;
import java.io.*;
import java.nio.file.*;

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
        return obj instanceof Tag && this.content.equals(((Tag) obj).content) && compareTagSets(tagSet, ((Tag) obj).tagSet);
    }

    private boolean compareTagSets(Set<Tag> set1, Set<Tag> set2) {
        return set1 != null && set2 != null && set1.size() == set2.size() && set1.containsAll(set2);
    }

    public static Set getTagSet(){
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
        FileOutputStream TagFOS = new FileOutputStream(tagFile);
        BufferedWriter TagBW = new BufferedWriter(new OutputStreamWriter(TagFOS));
        TagBW.write(defaultPath);
        TagBW.close();
        TagFOS.close();
    }

    public static void readTagFile() throws IOException {
    }

    public static void createTagFile() throws IOException {
        tagFile.createNewFile();
    }
}
