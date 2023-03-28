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
        try (FileOutputStream output = new FileOutputStream(fileLocation);
                ZipOutputStream zos = new ZipOutputStream(output)) {
            ZipEntry ze = new ZipEntry("log.csv");
            zos.putNextEntry(ze);
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
