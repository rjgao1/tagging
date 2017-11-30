package Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
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

    private Tag[] tagList;
    private TagInfo tagInfo;

    @BeforeEach
    void setUp() {
        this.tagList = new Tag[] {tag1, tag2, tag3, tag4};
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


    @Test
    void testGetTagListWithTags() {
        Tag[] testTagList = new Tag[] {tag1, tag2, tag3, tag4};
        assertTrue(tagInfo.getTagList().length == 4);
        assertEquals(tagList, tagInfo.getTagList());
        for (int i = 0; i < tagInfo.getTagList().length; i++) {
            assertEquals(Array.get(testTagList, i), Array.get(tagInfo.getTagList(), i));
        }
    }

    @Test
    void testGetTagListWithoutTags() {
        this.tagList = new Tag[0];
        this.tagInfo = new TagInfo(this.tagList);
        assertTrue(tagInfo.getTagList().length == 0);
    }

    @org.junit.jupiter.api.Test
    void getTagList() {
    }

    @Test
    void testGetTagListStringWithTags() {
        String exp = "@tag1 @tag2 @tag3 @tag4 ";
        String result = tagInfo.getTagListString();
        assertEquals(exp, result);
    }

    @org.junit.jupiter.api.Test
    void getTagListString() {
    }

    @Test
    void testToStringWithTags() {
        assertEquals(tagInfo.getTime() + "|@tag1@tag2@tag3@tag4", tagInfo.toString());
    }

    @Test
    void testToStringWithoutTags() {
        this.tagList = new Tag[0];
        this.tagInfo = new TagInfo(this.tagList);
        assertEquals(tagInfo.getTime() + "|", tagInfo.toString());
    }


    @Test
    void testStringToTagInfoWithTags() {
        String tagInfoString = "2017/11/30 02:27:11|@tag1@tag2@tag3@tag4";
        assertEquals("2017/11/30 02:27:11", TagInfo.stringToTagInfo(tagInfoString).getTime());
        assertEquals("@tag1 @tag2 @tag3 @tag4 ", TagInfo.stringToTagInfo(tagInfoString).getTagListString());
    }


    @org.junit.jupiter.api.Test
    void setTagListString() {
    }



}