package Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import static org.junit.Assert.*;

public class TagInfoTest {
    private Tag tag1 = new Tag("tag1");
    private Tag tag2 = new Tag("tag2");
    private Tag tag3 = new Tag("tag3");
    private Tag tag4 = new Tag("tag4");

    private Tag[] tagList;
    private TagInfo tagInfo;

    @Before
    public void setUp() throws Exception {
        this.tagList = new Tag[]{tag1, tag2, tag3, tag4};
        this.tagInfo = new TagInfo(tagList);
    }

    @After
    public void tearDown() throws Exception {
        this.tagInfo = null;
    }

    @Test
    public void testGetTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateObj = new Date();

        assertEquals(dateFormat.format(dateObj), tagInfo.getTime());
    }


    @Test
    public void testGetTagListWithTags() {
        assertTrue(tagInfo.getTagList().length == 4);
        for (int i = 0; i < tagInfo.getTagList().length; i++) {
            assertEquals(Array.get(this.tagList, i), Array.get(tagInfo.getTagList(), i));
        }
    }

    @Test
    public void testGetTagListWithoutTags() {
        this.tagList = new Tag[0];
        this.tagInfo = new TagInfo(this.tagList);
        assertTrue(tagInfo.getTagList().length == 0);
    }

    @Test
    public void testGetTagListStringWithTags() {
        String exp = "@tag1 @tag2 @tag3 @tag4 ";
        String result = tagInfo.getTagListString();
        assertEquals(exp, result);
    }

    @Test
    public void testGetTagListStringWithoutTags() {
        this.tagList = new Tag[0];
        this.tagInfo = new TagInfo(tagList);
        String exp = "";
        String result = tagInfo.getTagListString();
        assertEquals(exp, result);
    }

    @Test
    public void testToStringWithTags() {
        assertEquals(tagInfo.getTime() + "|@tag1@tag2@tag3@tag4", tagInfo.toString());
    }

    @Test
    public void testToStringWithoutTags() {
        this.tagList = new Tag[0];
        this.tagInfo = new TagInfo(this.tagList);
        assertEquals(tagInfo.getTime() + "|", tagInfo.toString());
    }

    @Test
    public void testStringToTagInfoWithTags() {
        String tagInfoString = "2017/11/30 02:27:11|@tag1@tag2@tag3@tag4";
        assertEquals("2017/11/30 02:27:11", TagInfo.stringToTagInfo(tagInfoString).getTime());
        assertEquals("@tag1 @tag2 @tag3 @tag4 ", TagInfo.stringToTagInfo(tagInfoString).getTagListString());
    }

    @Test
    public void testStringToTagInfoWithoutTags() {
        String tagInfoString = "2017/11/30 02:27:11|";
        assertEquals("2017/11/30 02:27:11", TagInfo.stringToTagInfo(tagInfoString).getTime());
        assertTrue(TagInfo.stringToTagInfo(tagInfoString).getTagList().length == 0);
        assertTrue(TagInfo.stringToTagInfo(tagInfoString).getTagListString().equals(""));
        assertEquals("2017/11/30 02:27:11|", TagInfo.stringToTagInfo(tagInfoString).toString());
    }


}