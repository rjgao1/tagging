package Model;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TagInfo {

    Tag[] tagList;
    String time;
    String tagListString;

    public TagInfo(Tag[] tagList) {
        this.tagList = tagList;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateObj = new Date();
        time = dateFormat.format(dateObj);
        StringBuilder s = new StringBuilder();
        for (Tag tag: tagList) {
            s.append("@");
            s.append(tag.getContent());
            s.append(" ");
        }
        tagListString = s.toString();
        tagListString.substring(tagListString.length() - 1);
    }

    public String getTime() {
        return this.time;
    }

    public Tag[] getTagList() {
        return tagList.clone();
    }

    //Returns the elements of tagList in the form of a string "@tag1 @tag2 ..."
    public String getTagListString() {
        return tagListString;
    }

    //Returns a string of the form "yyyy/MM/dd HH:mm:ss|@tag1@tag2"
    public String toString() {
        StringBuilder result = new StringBuilder("");
        result.append(this.getTime());
        result.append("|");
        for (Tag tag: tagList) {
            result.append("@");
            result.append(tag.getContent());
        }
        return result.toString();
    }

    public void setTime(String newTime) {
        this.time = newTime;
    }

    public TagInfo(String tagString) {
        String[] tagList = tagString.split("@");
    }

    // Works only for strings of the form "yyyy/MM/dd HH:mm:ss|@tag1@tag2"
    public TagInfo stringToTagInfo(String tagString) {
        String currentTime = tagString.substring(0, 19);
        String initialTagsString = tagString.substring(21, tagString.length());
        String[] tagNames = initialTagsString.split("@");
        Tag[] tags = new Tag[tagNames.length];
        for (int i = 0; i < tags.length; i++) {
            tags[i] = new Tag(tagNames[i]);
        }
        TagInfo result = new TagInfo(tags);
        result.setTime(currentTime);
        return result;
    }
}