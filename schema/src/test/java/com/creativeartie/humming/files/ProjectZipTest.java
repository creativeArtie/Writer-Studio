package com.creativeartie.humming.files;

import java.io.*;

import org.junit.jupiter.api.*;

class ProjectZipTest {
    @Test
    void test() throws FileNotFoundException, IOException {
        ProjectZip data = new ProjectZip("test.zip");
        data.save();
    }
}
