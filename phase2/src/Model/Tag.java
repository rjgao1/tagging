package Model;

import java.io.IOException;
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag && this.content.equals(((Tag) obj).content);
    }

    public Set getTagSet(){
        return tagSet;
    }

    public void setTagSet(Set<Tag> newTagSet){
        Tag.tagSet = newTagSet;
    }

    public void addTagToSet(Tag newTag){
        if (!Tag.tagSet.contains(newTag)) {
            Tag.tagSet.add(newTag);
        }
    }

    public void addTagToSet(Tag[] newTagList){
        for (element : newTagList) {
            if (!Tag.tagSet.contains(element)) {
                Tag.tagSet.add(element)
            }
        }
    }

    public void writeTagFile() throws IOException {
    }
}
