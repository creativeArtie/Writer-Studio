package com.creativeartie.humming.files;

import java.io.*;

import org.junit.jupiter.api.*;

@SuppressWarnings("nls")
class ProjectZipTest {
    private String zipFile = "test.zip";

    @Test
    void test() throws FileNotFoundException, IOException, ClassNotFoundException {
        ProjectZip data = ProjectZip.newProject(zipFile);
        ManuscriptFile draft = data.createManuscript("draft");

        draft.getManuscript().updateText("Hello World!");
        draft = data.newEmptyVersion("draft");
        draft.getManuscript().updateText("Hello World!\nSome other text!");
        data.start();
        data.end();

        data.addImage("tmp", new File("../doc/clean.png"));

        data.save();

        // ProjectZip result = ProjectZip.loadProject(zipFile);

        // TODO check results

        File file = new File("test.zip");
        file.delete();
    }
}
