package Model;

import java.io.IOException;
import java.util.*;
import java.io.*;

/**
 * Represents a Tag object.
 */
public class Tag {

    /* The content of this Tag.*/
    private final String content;
    /* The set of all Tags.*/
    private static Set<Tag> tagSet = new HashSet<>();
    /* The path of the tag file. */
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
     * @return true if this object is a Tag with the same content as obj's.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag && this.content.equals(((Tag) obj).content);
    }

    /**
     * Returns a set of Tags representing this Tag's tagSet.
     *
     * @return this Tag's tagSet.
     */
    public static Set<Tag> getTagSet() {
        return tagSet;
    }

    /**
     * Adds Tag newTag to this Tag's tagSet, if it is not already contained in it.
     *
     * @param newTag a Tag to be added to this Tag's tagSet.
     * @throws IOException when the tagFile is not found.
     */
    public static void addTagToSet(Tag newTag) throws IOException {
        if (!Tag.tagSet.contains(newTag)) {
            Tag.tagSet.add(newTag);
            writeTagFile();
        }
    }

    /**
     * Adds the elements of newTagList to this Tag's tagSet, if they are not already contained in it.
     *
     * @param newTagList an Array of Tags, whose elements are to be added to this Tag's tagSet.
     * @throws IOException when the tagFile is not found.
     */
    public static void addTagToSet(Tag[] newTagList) throws IOException {
        for (Tag element : newTagList) {
            if (!Tag.tagSet.contains(element)) {
                Tag.tagSet.add(element);
                writeTagFile();
            }
        }
    }

    /**
     * Writes the contents of this Tag's tagSet in the tagFile.
     *
     * @throws IOException when the tagFile does not exist
     */
    private static void writeTagFile() throws IOException {
        FileOutputStream tagFOS = new FileOutputStream(tagFile);
        BufferedWriter tagBW = new BufferedWriter(new OutputStreamWriter(tagFOS));
        for (Tag tag : tagSet) {
            tagBW.write(tag.getContent());
            tagBW.newLine();
        }
        tagBW.close();
        tagFOS.close();
    }

    /**
     * Reads the contents of a tagFile and adds the appropriate Tag objects generated from them in this Tag's tagSet.
     *
     * @throws IOException when the tagFile is empty
     */
    public static void readTagFile() throws IOException {
        BufferedReader tagBR = new BufferedReader(new FileReader(tagFile));
        String line = tagBR.readLine();
        while (line != null) {
            tagSet.add(new Tag(line));
            line = tagBR.readLine();
        }
        tagBR.close();
    }

    /**
     * Creates a new tagFile.
     *
     * @throws IOException if there is an existing tagFile
     */
    public static void createTagFile() throws IOException {
        tagFile.createNewFile();
    }

    /**
     * Returns whether tagFile is in the project directory.
     *
     * @return true if the tagFile is in the project directory
     */
    public static boolean hasTagFile() {
        return tagFile.isFile();
    }

    /**
     * Removes the elements of tagSet that are in an Array of Tags, namely tagArray.
     * Precondition: All the elements of tagArray must be contained in tagSet.
     *
     * @param tagArray an Array of Tags, whose elements are to be removed from tagSet.
     * @throws IOException when the tagFile is not found
     */
    public static void removeTagsFromTagSet(Tag[] tagArray) throws IOException {
        for (Tag tag : tagArray) {
            Tag.tagSet.remove(tag);
        }
        writeTagFile();
    }

    /**
     * Returns an hash code for a given Tag object, based on this Tag's content. Tags which are not equal, i.e. Tags
     * with different content, have different hash codes.
     *
     * @return an int which is a hash code for a given Tag object
     */
    @Override
    public int hashCode() {
        return this.content.hashCode();
    }
}
