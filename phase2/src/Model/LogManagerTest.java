package Model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class LogManagerTest {
    private final String oldUserDir = System.getProperty("user.dir");

    @Rule
//    public TemporaryFolder tempFolder = new TemporaryFolder(new File(System.getProperty("user.dir")));
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @org.junit.Before
    public void setUp() throws IOException {
        System.setProperty("user.dir", tempFolder.getRoot().toString());
    }

    @org.junit.After
    public void tearDown() throws Exception {
        System.setProperty("user.dir", oldUserDir);
    }

    @Test
    public void testLogManagerCreateNewLog() throws IOException {
        String logDirString = Paths.get(tempFolder.getRoot().toString(), "Logs").toString();


//        tempFolder.newFile("testImage1.jpg");
//        Image testImage1 = new Image(Paths.get(tempFolder.getRoot().getPath(), "testImage1.jpg").toString());

//        LogManager lm = testImage1.getLogManager();
        LogManager lm = new LogManager(Paths.get(tempFolder.getRoot().getPath(), "testImage1.jpg").toString());


        File[] fileList1 = Paths.get(System.getProperty("user.dir")).toFile().listFiles();
        File[] fileList2 = Paths.get(logDirString).toFile().listFiles();
        boolean result1 = false;
        boolean result2 = false;
        for (File file : fileList1) {
            System.out.println(file.toPath());
            if (file.toPath().endsWith("Logs")) {
                result1 = true;
            }
        }
        for (File file : fileList2) {
            System.out.println(file.toPath());
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
        String testImage1PathString = Paths.get(tempFolder.getRoot().getPath(), "testImage1.jpg").toString();
        String newTestImage1PathString = Paths.get(tempFolder.getRoot().getPath(), "testImage1 @tag1 @tag2.jpg").toString();
        LogManager lm = new LogManager(testImage1PathString);
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
        String logDirString = Paths.get(tempFolder.getRoot().toString(), "Logs").toString();
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

    @org.junit.Test
    public void addTagInfo() throws Exception {
    }

    @org.junit.Test
    public void getTagInfos() throws Exception {
    }

}