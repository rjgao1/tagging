package Model;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Represents a TagInfo object, i.e. an object which keeps track of a list of Tags and a timestamp which denotes the
 * date and time it was created.
 */
public class TagInfo {


    private Tag[] tagList;
    private String time;
    private String tagListString;

    /**
     * Creates a TagInfo object with a tagList, time and tagListString.
     *
     * @param tagList this TagInfo's tagList, i.e. a list of Tags.
     */
    public TagInfo(Tag[] tagList) {
        this.tagList = tagList;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateObj = new Date();
        time = dateFormat.format(dateObj);
        StringBuilder s = new StringBuilder();
        for (Tag tag : tagList) {
            s.append("@");
            s.append(tag.getContent());
            s.append(" ");
        }
        tagListString = s.toString();
        if (tagList.length > 1) {
            tagListString.substring(tagListString.length() - 1);
        }
    }

    /**
     * Returns this TagInfo's time.
     *
     * @return a string representing the time this TagInfo was created.
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Returns this TagInfo's tagList.
     *
     * @return an Array of this TagInfo's Tags.
     */
    public Tag[] getTagList() {
        return tagList.clone();
    }

    /**
     * Returns this TagInfo's tagList as a string of the form "@tag1 @tag2 ...".
     *
     * @return a String containing the elements of tagList.
     */
    public String getTagListString() {
        return tagListString;
    }

    /**
     * Returns a string representation of this TagInfo, of the form "yyyy/MM/dd HH:mm:ss|@tag1@tag2...".
     *
     * @return a String of the form "yyyy/MM/dd HH:mm:ss|@tag1@tag2"
     */
    public String toString() {
        StringBuilder result = new StringBuilder("");
        result.append(this.getTime());
        result.append("|");
        for (Tag tag : tagList) {
            result.append("@");
            result.append(tag.getContent());
        }
        return result.toString();
    }

    /**
     * Sets this TagInfo's time to newTime.
     *
     * @param newTime a new String representing time
     */
    private void setTime(String newTime) {
        this.time = newTime;
    }

    /**
     * Returns a new TagInfo object, given a string of the form "yyyy/MM/dd HH:mm:ss|@tag1@tag2", with the time
     * specified in this string. This method only produces the correct result when the input string tagString is
     * of this particular form.
     *
     * @param tagString a String representing a TagInfo.
     * @return a TagInfo object with the information of the input string.
     */
    public static TagInfo stringToTagInfo(String tagString) {
        String currentTime = tagString.substring(0, 19);
        if (tagString.length() >= 21) {
            String initialTagsString = tagString.substring(21, tagString.length());
            String[] tagNames = initialTagsString.split("@");
            Tag[] tags = new Tag[tagNames.length];
            for (int i = 0; i < tags.length; i++) {
                tags[i] = new Tag(tagNames[i]);
            }
            TagInfo result = new TagInfo(tags);
            result.setTime(currentTime);
            return result;
        } else {
            TagInfo result = new TagInfo(new Tag[0]);
            result.setTime(currentTime);
            return result;
        }
    }
}