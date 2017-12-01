package Model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class LogManagerTest {
    private final String oldUserDir = System.getProperty("user.dir");
    private String logDirString;
    private String testImagePathString;
    @Rule
//    public TemporaryFolder tempFolder = new TemporaryFolder(new File(System.getProperty("user.dir")));
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @org.junit.Before
    public void setUp() throws IOException {
        System.setProperty("user.dir", tempFolder.getRoot().toString());
        logDirString = Paths.get(tempFolder.getRoot().toString(), "Logs").toString();
        testImagePathString = Paths.get(tempFolder.getRoot().getPath(), "testImage1.jpg").toString();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        logDirString = null;
        System.setProperty("user.dir", oldUserDir);
    }

    @Test
    public void testLogManagerCreateNewLog() throws IOException {

        LogManager lm = new LogManager(testImagePathString);


        File[] fileList1 = Paths.get(System.getProperty("user.dir")).toFile().listFiles();
        File[] fileList2 = Paths.get(logDirString).toFile().listFiles();
        boolean result1 = false;
        boolean result2 = false;
        for (File file : fileList1) {
            if (file.toPath().endsWith("Logs")) {
                result1 = true;
            }
        }
        for (File file : fileList2) {
            if (file.toString().endsWith("testImage1.jpg.txt")) {
                result2 = true;
            }
        }
        assertTrue(result1);
        assertTrue(result2);
        assertEquals(Paths.get(tempFolder.getRoot().toString(), "Logs").toString(), logDirString);

    }

    @Test
    public void testRenameLogFileImagePath() throws Exception {
        boolean result1 = true;
        boolean result2 = false;
        String newTestImage1PathString = Paths.get(tempFolder.getRoot().getPath(), "testImage1 @tag1 @tag2.jpg").toString();
        LogManager lm = new LogManager(testImagePathString);
        lm.renameLogFile(newTestImage1PathString, true);
        File[] fileList = Paths.get(tempFolder.getRoot().getPath(), "Logs").toFile().listFiles();
        for (File file : fileList) {
            if (file.toString().endsWith("testImage1.jpg.txt")) {
                result1 = false;
            }
            if (file.toString().endsWith("testImage1 @tag1 @tag2.jpg.txt")) {
                result2 = true;
            }
        }
        /* make sure the old log name does not exist */
        assertTrue(result1);
        /* make sure the new log name exist */
        assertTrue(result2);

    }

    @Test
    public void testRenameLogFileNewLogPath() throws Exception {
        boolean result1 = true;
        boolean result2 = false;
        String oldPath = Paths.get(logDirString, "testImage1 @tag1 @tag2 @tag3.jpg.txt").toString();
        String newPath = Paths.get(logDirString, "testImage1 @tag1 @tag3 @tag4.jpg.txt").toString();
        LogManager lm = new LogManager(oldPath);
        lm.renameLogFile(newPath, false);
        File[] fileList = Paths.get(logDirString).toFile().listFiles();
        for (File file : fileList) {
            if (file.toString().endsWith("testImage1 @tag1 @tag2 @tag3.jpg.txt")) {
                result1 = false;
            }
            if (file.toString().endsWith("testImage1 @tag1 @tag3 @tag4.jpg.txt")) {
                result2 = true;
            }
        }
        assertTrue(result1);
        assertTrue(result2);
    }

    @org.junit.Test
    public void renameLogFile() throws Exception {

    }

    @Test
    public void testAddSingleTagInfo() throws Exception {
        /* construct a timestamp string as part of the expected String */
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateObj = new Date();
        String time = dateFormat.format(dateObj);

        String logFilePathString = new String("");
        TagInfo newTagInfo = new TagInfo(new Tag[]{new Tag("tag1"), new Tag("tag2"), new Tag("tag3")});
        String exp = time + "|@tag1@tag2@tag3";
        String act = new String("");

        LogManager lm = new LogManager(testImagePathString);
        lm.addTagInfo(newTagInfo);

        File[] fileList = Paths.get(logDirString).toFile().listFiles();

        for (File file : fileList) {
            if (file.toString().endsWith("testImage1 @tag1 @tag2 @tag3.jpg.txt")) {
                logFilePathString = file.toString();
            }
        }
        if (!logFilePathString.equals("")) {
            BufferedReader br = new BufferedReader(new FileReader(logFilePathString));
            String line = br.readLine();
            while (line != null) {
                act = line;
                line = br.readLine();
            }
        } else {
            assertTrue(false);
        }
        if (!act.equals("")) {
            assertEquals(exp, act);
        } else {
            assertTrue(false);
        }
    }

    @Test
    public void addMultipleTagInfos() throws Exception {
        /* construct a timestamp string as part of the expected String */
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateObj = new Date();
        String time = dateFormat.format(dateObj);

        String logFilePathString = new String("");
        TagInfo newTagInfo1 = new TagInfo(new Tag[]{new Tag("tag1"), new Tag("tag2"), new Tag("tag3")});
        TagInfo newTagInfo2 = new TagInfo(new Tag[]{new Tag("tag2"), new Tag("tag3"),
                new Tag("tag5"), new Tag("tag6")});
        TagInfo newTagInfo3 = new TagInfo(new Tag[0]);
        String[] exp = new String[]{time + "|@tag1@tag2@tag3", time + "|@tag2@tag3@tag5@tag6", time + "|"};
        String[] act = new String[0];

        LogManager lm = new LogManager(testImagePathString);
        lm.addTagInfo(newTagInfo1);
        lm.addTagInfo(newTagInfo2);
        lm.addTagInfo(newTagInfo3);

        File[] fileList = Paths.get(logDirString).toFile().listFiles();
        for (File file : fileList) {
            if (file.toString().endsWith("testImage1.jpg.txt")) {
                logFilePathString = file.toString();
            }
        }
        if (!logFilePathString.equals("")) {
            BufferedReader br = new BufferedReader(new FileReader(logFilePathString));
            String line = br.readLine();
            int i = 0;
            while (line != null) {
                assertEquals(exp[i], line);
                i++;
                line = br.readLine();
            }
        } else {
            assertTrue(false);
        }
    }

    @org.junit.Test
    public void addTagInfo() throws Exception {
    }

    @Test
    public void testGetTagInfosGeneral() throws Exception {
        ArrayList<TagInfo> tagInfosExp = new ArrayList<>(0);
        TagInfo tagInfo1 = new TagInfo(new Tag[]{new Tag("tag1"), new Tag("tag2"), new Tag("tag3")});
        TagInfo tagInfo2 = new TagInfo(new Tag[0]);
        TagInfo tagInfo3 = new TagInfo(new Tag[]{new Tag("tagA"), new Tag("tag2"), new Tag("tagC")});
        tagInfosExp.add(tagInfo1);
        tagInfosExp.add(tagInfo2);
        tagInfosExp.add(tagInfo3);


        LogManager lm = new LogManager(testImagePathString);
        lm.addTagInfo(tagInfo1);
        lm.addTagInfo(tagInfo2);
        lm.addTagInfo(tagInfo3);

        ArrayList<TagInfo> tagInfosAct = lm.getTagInfos();

        assertEquals(tagInfosExp.size(), tagInfosAct.size());

        for (int i = 0; i < tagInfosAct.size(); i++) {
            assertEquals(tagInfosExp.get(i).getTagList().length, tagInfosAct.get(i).getTagList().length);
            assertEquals(tagInfosExp.get(i).getTime(), tagInfosAct.get(i).getTime());

            for (int k = 0; k < tagInfosAct.get(i).getTagList().length; k++) {
                assertEquals(Array.get(tagInfosExp.get(i).getTagList(), k), Array.get(tagInfosAct.get(i).getTagList(), k));
            }
        }
    }

    @org.junit.Test
    public void getTagInfos() throws Exception {
    }

}