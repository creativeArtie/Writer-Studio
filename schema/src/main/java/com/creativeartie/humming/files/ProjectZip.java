package com.creativeartie.humming.files;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.creativeartie.humming.document.*;

public class ProjectZip {
    private ProjectProperties projectProps;
    private Log writingLog;
    private ArrayList<File> imageFiles;
    private ArrayList<Manuscript> documentFiles;
    private String fileLocation;

    public ProjectZip(String location) {
        projectProps = new ProjectProperties();
        writingLog = new Log();
        imageFiles = new ArrayList<>();
        documentFiles = new ArrayList<>();
        fileLocation = location;
    }

    public void save() throws FileNotFoundException, IOException {
        ObjectOutputStream log = null;
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileLocation))) {
            zos.putNextEntry(new ZipEntry("log.obj"));
            log = new ObjectOutputStream(zos);
            log.writeObject(writingLog);
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry("info.properties"));
            projectProps.save(zos);
            zos.closeEntry();
        } finally {
            log.close();
        }
    }

    public ProjectProperties getProjectProps() {
        return projectProps;
    }

    public Log getWritingLog() {
        return writingLog;
    }

    public ArrayList<File> getImageFiles() {
        return imageFiles;
    }

    public ArrayList<Manuscript> getDocumentFiles() {
        return documentFiles;
    }
}
