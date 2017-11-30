package Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.*;
import java.io.*;

class TagInfoTest {
    private Tag tag1 = new Tag("tag1");
    private Tag tag2 = new Tag("tag2");
    private Tag tag3 = new Tag("tag3");
    private Tag tag4 = new Tag("tag4");

    private Tag[] tagList = new Tag[] {tag1, tag2, tag3, tag4};
    private TagInfo tagInfo;

    @BeforeEach
    void setUp() {
        this.tagInfo = new TagInfo(tagList);
    }

    @AfterEach
    void tearDown() {
        this.tagInfo = null;
    }

    @Test
    public void testTagInfoGetTime() {
//        Tag[] tagList = {new Tag("tag1"), new Tag("tag2"), new Tag("tag3"), new Tag("tag4")};
//        TagInfo tagInfo = new TagInfo(tagList);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateObj = new Date();

        assertEquals(dateFormat.format(dateObj), tagInfo.getTime());
    }



    @org.junit.jupiter.api.Test
    void getTagList() {
    }

    @org.junit.jupiter.api.Test
    void getTagListString() {
    }

    @Test
    void testToStringWithTags() {
        assertEquals(tagInfo.getTime()+"|@tag1@tag2@tag3@tag4", tagInfo.toString());
    }

    @Test
    void testToStringWithoutTags() {

    }



    @org.junit.jupiter.api.Test
    void stringToTagInfo() {
    }

    @org.junit.jupiter.api.Test
    void setTagListString() {
    }



}