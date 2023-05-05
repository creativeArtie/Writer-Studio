package com.creativeartie.humming.files;

import java.io.*;
import java.time.*;

import org.junit.jupiter.api.*;

@SuppressWarnings("nls")
class ProjectZipTest {
    private String zipFile = "test.zip";

    @Test
    void test() throws FileNotFoundException, IOException, ClassNotFoundException {
        ProjectZip data = ProjectZip.newProject(zipFile);
        ManuscriptFile draft = data.createManuscript("draft");

        String firstDraft = "Hello World!";
        String secondDraft = firstDraft + "\nSome other text!";

        draft.getManuscript().updateText(firstDraft);
        draft = data.newEmptyVersion("draft");
        data.start();
        draft.getManuscript().updateText(secondDraft);
        data.end();
        data.addImage("tmp", new File("../doc/clean.png"));
        String image = data.getEncodedImage("tmp");

        Log.Entry log = data.getWritingLog().getCurrent();
        Duration time = log.getTimeSpent();
        LocalDate date = log.getCreatedDate();

        data.save();

        ProjectZip result = ProjectZip.loadProject(zipFile);
        Assertions.assertEquals(time, result.getWritingLog().getCurrent().getTimeSpent(), "time spent");
        Assertions.assertEquals(date, result.getWritingLog().getCurrent().getCreatedDate(), "created date");

        ManuscriptFile savedDraft = result.getManuscript("draft");
        Assertions.assertNotNull(savedDraft, "Missing Draft");
        Assertions.assertEquals(secondDraft, savedDraft.getManuscript().getText(), "Second Draft");
        Assertions.assertEquals(firstDraft, draft.getPreviousDraft().get().getManuscript().getText(), "first draft");

        String imageData = result.getEncodedImage("tmp");
        Assertions.assertEquals(image, imageData, "image file");

        File file = new File("test.zip");
        file.delete();
    }
}
