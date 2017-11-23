package Model;

import java.util.*;

public class Tag {

    private final String content;
    private static Set<Tag> tagSet;

    public Tag(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Set getTagSet(){
        return tagSet;
    }

    public void setTagSet(Set<Tag> newTagSet){
        Tag.tagSet = newTagSet;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag && this.content.equals(((Tag) obj).content);
    }
}
