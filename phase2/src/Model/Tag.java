package Model;

import java.io.IOException;
import java.util.*;
import java.io.*;

/**
 * Represents a Tag object.
 */
public class Tag {

    private final String content;
    private static Set<Tag> tagSet = new HashSet<>();
    private static String defaultPath = "";
    private static final File tagFile = new File(System.getProperty("user.dir"), "tag.txt");

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
    public static void addTagToSet(Tag newTag) throws IOException{
        if (!Tag.tagSet.contains(newTag)) {
            Tag.tagSet.add(newTag);
            writeTagFile();
        }
    }

    /**
     * Adds the elements of newTagList to this Tag's tagSet, if they are not already contained in it.
     *
     * @param newTagList an Array of Tags, whose elements are to be added to this Tag's tagSet.
     */
    public static void addTagToSet(Tag[] newTagList) throws IOException{
        for (Tag element : newTagList) {
            if (!Tag.tagSet.contains(element)) {
                Tag.tagSet.add(element);
                writeTagFile();
            }
        }
    }

    public static void writeTagFile() throws IOException {
        FileOutputStream tagFOS = new FileOutputStream(tagFile);
        BufferedWriter tagBW = new BufferedWriter(new OutputStreamWriter(tagFOS));
        for (Tag tag : tagSet) {
            tagBW.write(tag.getContent());
            tagBW.newLine();
        }
        tagBW.write(defaultPath);
        tagBW.close();
        tagFOS.close();
    }

    public static void readTagFile() throws IOException {
        BufferedReader tagBR = new BufferedReader(new FileReader(tagFile));
        String line = tagBR.readLine();
        while (line != null) {
            tagSet.add(new Tag(line));
            line = tagBR.readLine();
        }
        tagBR.close();
    }

    public static void createTagFile() throws IOException {
        tagFile.createNewFile();
    }

    /**
     * Returns if it already exists a tagFile.
     *
     * @return true if there exists a tagFile
     */
    public static boolean hasTagFile() {
        return tagFile.isFile();
    }

    /**
     * Removes the elements of tagSet that are in an Array of Tags, namely tagArray.
     * Precondition: All the elements of tagArray must be contained in tagSet.
     *
     * @param tagArray an Array of Tags, whose elements are to be removed from tagSet.
     */
    public static void removeTagsFromTagSet(Tag[] tagArray) {
        for (Tag tag : tagArray) {
            Tag.tagSet.remove(tag);
        }
    }

    @Override
    public int hashCode() {
        return this.content.hashCode();
    }
}
