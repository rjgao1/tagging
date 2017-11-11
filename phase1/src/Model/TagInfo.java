package Model;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TagInfo {

    Tag[] tagList;
    String time;

    public TagInfo(Tag[] tagList) {
        this.tagList = tagList;
    }

    public String getTime() {
        return time;
    }

    public Tag[] getTagList() {
        return tagList;
    }

    public String toString() {
        return new String();
    }
}
