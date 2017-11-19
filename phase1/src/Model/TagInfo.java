package Model;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TagInfo {

    Tag[] tagList;
    String time;

    public TagInfo(Tag[] tagList) {
        this.tagList = tagList;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateObj = new Date();
        time = dateFormat.format(dateObj);
    }

    public String getTime() {
        return this.time;
    }

    public Tag[] getTagList() {
        return tagList.clone();
    }

    public String getTagListString() {
        StringBuilder result = new StringBuilder("");
        if (tagList.length == 0) {
            return result.toString();
        } else {
            int i = 0;
            while (i < tagList.length - 1) {
                result.append("@");
                StringBuilder element = new StringBuilder(tagList[i].getContent());
                result.append(element);
                result.append(" ");
                i = i + 1;
            }
            result.append("@");
            result.append(tagList[tagList.length - 1].getContent());
        }
        return result.toString();
    }

    public String toString() {
        StringBuilder result = new StringBuilder("");
        result.append("Time: ");
        result.append(this.getTime());
        result.append(System.getProperty("line.separator"));
        result.append("Tags: ");
        if (tagList.length == 0) {
            result.append("");
        } else {
            int i = 0;
            while (i < tagList.length - 1) {
                StringBuilder element = new StringBuilder(tagList[i].getContent());
                result.append(element);
                result.append(", ");
                i = i + 1;
            }
            result.append(tagList[tagList.length - 1].getContent());
        }
        return result.toString();
    }

    public void setTime(String newTime) {
        this.time = newTime;
    }

    public TagInfo(String tagString) {
        String[] tagList = tagString.split("@");
    }

    public TagInfo stringToTagInfo(String tagString) {
        String[] tags = tagString.split("@");
        Tag[] tempTagList = new Tag[tags.length - 1];
        for (int i = 0; i < tempTagList.length; i++) {
            tempTagList[i] = new Tag(tags[i + 1]);
        }
        return new TagInfo(tempTagList);
    }
}