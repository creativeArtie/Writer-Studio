package com.creativeartie.humming.files;

import java.io.*;

import org.junit.jupiter.api.*;

@SuppressWarnings("nls")
class ProjectZipTest {
    @Test
    void test() throws FileNotFoundException, IOException {
        ProjectZip data = new ProjectZip("test.zip");
        data.save();
        File file = new File("test.zip");
        file.delete();
    }
}
