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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateObj = new Date();
        time = dateFormat.format(dateObj);
        return time;
    }

    public Tag[] getTagList() {
        return tagList.clone();
    }

    public String toString() {
        return new String();
    }
}
