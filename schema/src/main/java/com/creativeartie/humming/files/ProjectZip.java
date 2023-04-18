package com.creativeartie.humming.files;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.Map.*;
import java.util.Optional;
import java.util.zip.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

/**
 * Project zip file
 */
public class ProjectZip {
    private ProjectProperties projectProps;
    private Log writingLog;
    private Properties imageFiles;
    private TreeMap<String, ManuscriptFile> documentFiles;
    private String fileLocation;
    private Optional<LocalDateTime> startTime;

    public static ProjectZip newProject(String location) {
        return new ProjectZip(location);
    }

    public static ProjectZip loadProject(String location)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        File file = new File(location);
        Preconditions.checkArgument(file.isFile() && file.canWrite() && file.canRead());

        ProjectZip project = new ProjectZip(location);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(location))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName() == Literals.LOG_FILE.getText()) {
                    ObjectInputStream log = new ObjectInputStream(zis);
                    project.writingLog = (Log) log.readObject();
                } else if (entry.getName() == Literals.LOG_FILE.getText()) System.out.println(entry.getName());
                zis.closeEntry();
            }

        }
        return project;
    }

    private ProjectZip(String location) {
        projectProps = new ProjectProperties();
        writingLog = new Log();
        imageFiles = new Properties();
        documentFiles = new TreeMap<>();
        fileLocation = location;
        startTime = Optional.empty();
    }

    /**
     * Saves the file
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void save() throws FileNotFoundException, IOException {
        // Needs to be create after new entry + close after the zip output stream closes
        @SuppressWarnings("resource")
        ObjectOutputStream log = null;
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileLocation))) {
            zos.putNextEntry(new ZipEntry(Literals.LOG_FILE.getText()));
            log = new ObjectOutputStream(zos);
            log.writeObject(writingLog);
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry(Literals.PROP_FILE.getText()));
            projectProps.save(zos);
            zos.closeEntry();

            String ext = Literals.DOC_EXT.getText();

            for (Entry<String, ManuscriptFile> file : documentFiles.entrySet()) {
                String key = Literals.DOC_FOLDER.getText() + File.separator + file.getKey();
                Optional<ManuscriptFile> value = Optional.of(file.getValue());
                do {
                    int draftNum = value.get().getDraftNumber();
                    String path = key + File.separator + Integer.toString(draftNum) + ext;
                    zos.putNextEntry(new ZipEntry(path));
                    zos.write(value.get().getManuscript().getText().getBytes());
                    zos.closeEntry();

                    value = value.get().getPreviousDraft();
                } while (value.isPresent());
            }
            Properties saveFiles = new Properties();

            for (String key : imageFiles.stringPropertyNames()) {
                File file = new File(imageFiles.getProperty(key));
                if (!file.canRead()) continue;

                try (FileInputStream image = new FileInputStream(file)) {
                    String path = Literals.IMAGE_FOLDER.getText() + File.separator + file.getName();
                    saveFiles.setProperty(key, path);
                    zos.putNextEntry(new ZipEntry(path));
                    zos.write(image.readAllBytes());
                    zos.closeEntry();
                }
            }

            zos.putNextEntry(new ZipEntry(Literals.IMAGES_FILE.getText()));
            saveFiles.store(zos, new String());
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
     * Start writing
     *
     * @return {@code true} if timer starts.
     */
    boolean start() {
        updateLogEntry();
        if (startTime.isEmpty()) {
            startTime = Optional.of(LocalDateTime.now());
            return true;
        }
        return false;
    }

    /**
     * Ending writing
     *
     * @return {@code true} if timer ends.
     */
    boolean end() {
        updateLogEntry();
        if (startTime.isPresent()) {
            startTime = Optional.empty();
            return true;
        }
        return false;
    }

    private void updateLogEntry() {
        int written = 0;
        int outline = 0;
        for (ManuscriptFile writing : documentFiles.values()) {
            written += writing.getManuscript().getWordCount();
            outline += writing.getManuscript().getOutlineCount();
        }
        writingLog.getCurrent().setWrittenCount(written);
        writingLog.getCurrent().setOutlineCount(outline);
        startTime.ifPresent((start) -> {
            writingLog.getCurrent().addTime(Duration.between(start, LocalDateTime.now()));
        });
    }

    /**
     * Create or get a new document.
     *
     * @param name
     *        the file name
     *
     * @return the ManuscriptFile
     */
    public ManuscriptFile createManuscript(String name) {
        if (documentFiles.containsKey(name)) {
            return null;
        }
        ManuscriptFile file = new ManuscriptFile(name);
        documentFiles.put(name, file);
        return file;
    }

    /**
     * Create or get a new document.
     *
     * @param name
     *        the file name
     *
     * @return the ManuscriptFile
     */
    public ManuscriptFile getManuscript(String name) {
        return documentFiles.get(name);
    }

    /**
     * Create new empty version
     *
     * @param name
     *        the file name
     *
     * @return the ManuscriptFile by the file name, or null if not existed
     *
     * @see #newFilledVersion(String)
     */
    public ManuscriptFile newEmptyVersion(String name) {
        if (!documentFiles.containsKey(name)) {
            return null;
        }
        ManuscriptFile previous = documentFiles.get(name);
        ManuscriptFile file = new ManuscriptFile(previous);
        documentFiles.put(name, file);
        return file;
    }

    /**
     * Create new filled version
     *
     * @param name
     *        the file name
     *
     * @return the ManuscriptFile by the file name, or null if not existed
     *
     * @see #newEmptyVersion(String)
     */
    public ManuscriptFile newFilledVersion(String name) {
        if (!documentFiles.containsKey(name)) {
            return null;
        }
        ManuscriptFile previous = documentFiles.get(name);
        ManuscriptFile file = new ManuscriptFile(previous);
        file.getManuscript().updateText(previous.getManuscript().getText());
        documentFiles.put(name, file);
        return file;
    }

    /**
     * Is the name is allowed to be use
     *
     * @param name
     *        the name to check
     *
     * @return {@code true} if allowed
     */
    public boolean isAllowed(String name) {
        return IdentityPattern.matcher(name) != null;
    }

    /**
     * Is the name is allowed to be use and can be use for a new document?
     *
     * @param name
     *        the name to check
     *
     * @return {@code true} if allowed
     */
    public boolean isNewDoc(String name) {
        return isAllowed(name) && !documentFiles.containsKey(name);
    }

    /**
     * Is the name is allowed to be use and can be use for a new image?
     *
     * @param name
     *        the name to check
     *
     * @return {@code true} if allowed
     */
    public boolean isNewImage(String name) {
        return isAllowed(name) && !imageFiles.containsKey(name);
    }

    /**
     * check if the manuscript existed
     *
     * @param name
     *        name of the file
     *
     * @return if there is a file with that name
     */

    public boolean hasManuscript(String name) {
        return documentFiles.containsKey(name);
    }

    /**
     * returns list of manuscripts
     *
     * @return the manuscript list
     */
    public Set<String> listManuscripts() {
        return documentFiles.keySet();
    }

    /**
     * adds a new image
     *
     * @param name
     *        the id name
     * @param image
     *        the image file
     *
     * @return {@true} if successful
     */
    public boolean addImage(String name, File image) {
        if (imageFiles.containsKey(name)) {
            return false;
        }
        imageFiles.put(name, image.getAbsolutePath());
        return true;
    }

    /**
     * returns list of images
     *
     * @return the image list
     */
    public Enumeration<Object> getImageList() {
        return imageFiles.keys();
    }

    /**
     * Replace a image
     *
     * @param name
     *        the id name
     * @param image
     *        the image file
     *
     * @return {@true} if successful
     */
    public boolean replaceImage(String name, File image) {
        if (imageFiles.containsKey(name)) {
            imageFiles.put(name, image);
            return true;
        }
        return false;
    }

    /**
     * Deletes an image
     *
     * @param name
     *        the id name
     *
     * @return {@true} if successful
     */
    public boolean deleteImage(String name) {
        return imageFiles.remove(name) != null;
    }

    /**
     * gets the image
     *
     * @param name
     *        the id name
     *
     * @return File or null if not exist
     */
    public File getImage(String name) {
        return new File(imageFiles.getProperty(name));
    }
}
