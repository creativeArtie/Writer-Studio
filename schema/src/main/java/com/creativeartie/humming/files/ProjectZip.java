package com.creativeartie.humming.files;

import java.io.*;
import java.util.*;
import java.util.zip.*;

/**
 * Project zip file
 */
public class ProjectZip {
    private ProjectProperties projectProps;
    private Log writingLog;
    private ArrayList<File> imageFiles;
    private ArrayList<ManuscriptFile> documentFiles;
    private String fileLocation;

    /**
     * create a project with a location
     *
     * @param location
     *        file location
     */
    public ProjectZip(String location) {
        projectProps = new ProjectProperties();
        writingLog = new Log();
        imageFiles = new ArrayList<>();
        documentFiles = new ArrayList<>();
        fileLocation = location;
    }

    /**
     * Saves the file
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void save() throws FileNotFoundException, IOException {
        @SuppressWarnings("resource") // TODO
        ObjectOutputStream log = null;
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileLocation))) {
            zos.putNextEntry(new ZipEntry(Literals.LOG_FILE.getText()));
            log = new ObjectOutputStream(zos);
            log.writeObject(writingLog);
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry(Literals.PROP_FILE.getText()));
            projectProps.save(zos);
            zos.closeEntry();
        } finally {
            log.close();
        }
    }

    /**
     * get the project properties
     *
     * @return project properties
     */
    public ProjectProperties getProjectProps() {
        return projectProps;
    }

    /**
     * Gets the writing log
     *
     * @return writing log
     */
    public Log getWritingLog() {
        return writingLog;
    }

    /**
     * get the image files
     *
     * @return image file list
     */
    public ArrayList<File> getImageFiles() {
        return imageFiles;
    }

    /**
     * get the manuscript files
     *
     * @return manuscript file list
     */
    public ArrayList<ManuscriptFile> getDocumentFiles() {
        return documentFiles;
    }
}
