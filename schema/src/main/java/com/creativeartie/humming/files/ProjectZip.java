package com.creativeartie.humming.files;

import java.io.*;
import java.util.*;

import com.creativeartie.humming.document.*;

public class ProjectZip {
    private ProjectProperties projectProps;
    private Log writingLog;
    private ArrayList<File> imageFiles;
    private ArrayList<Manuscript> documentFiles;

    public ProjectZip() {
        projectProps = new ProjectProperties();
        writingLog = new Log();
        imageFiles = new ArrayList<>();
        documentFiles = new ArrayList<>();
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
