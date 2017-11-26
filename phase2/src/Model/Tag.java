package Model;

import java.io.IOException;
import java.util.*;
import java.io.*;

/**
 * Represents a Tag object.
 */
public class Tag {

    private final String content;
    private static Set<Tag> tagSet;
    private static String defaultPath = "";
    private static final File tagFile = new File(System.getProperty("user.dir"), "config.txt");

    /**
     * Creates a Tag object with content.
     *
     * @param content this Tag's content.
     */
    public Tag(String content) {
        this.content = content;
    }

    /**
     * Returns this Tag's content.
     *
     * @return a string representing this Tag's content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Compares the specified Object obj with this Tag to check if they are equal.
     *
     * @param obj the object to be compared.
     * @return    true if this object is a Tag with the same content as obj's.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag && this.content.equals(((Tag) obj).content);
    }

    /**
     * Returns this Tag's tagSet.
     *
     * @return a set of Tags representing this Tag's tagSet.
     */
    public static Set<Tag> getTagSet(){
        return tagSet;
    }

    /**
     * Sets this Tag's tagSet to newTagSet.
     *
     * @param newTagSet a new set of Tags.
     */
    public static void setTagSet(Set<Tag> newTagSet){
        Tag.tagSet = newTagSet;
    }

    /**
     * Adds Tag newTag to this Tag's tagSet, if it is not already contained in it.
     *
     * @param newTag a Tag to be added to this Tag's tagSet.
     */
    public static void addTagToSet(Tag newTag){
        if (!Tag.tagSet.contains(newTag)) {
            Tag.tagSet.add(newTag);
        }
    }

    /**
     * Adds the elements of newTagList to this Tag's tagSet, if they are not already contained in it.
     *
     * @param newTagList an ArrayList of Tags, whose elements are to be added to this Tag's tagSet.
     */
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
