package com.creativeartie.humming.files;

import java.io.*;
import java.nio.file.*;
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
    private Optional<String> fileLocation;
    private Optional<LocalDateTime> startTime;

    /**
     * Create a new project
     *
     * @return a new project
     */
    public static ProjectZip newProject() {
        return new ProjectZip();
    }

    /**
     * Loads a project
     *
     * @param location
     *        the file path
     *
     * @return a loaded project
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static ProjectZip loadProject(String location)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        {
            File file = new File(location);
            Preconditions.checkArgument(file.isFile() && file.canWrite() && file.canRead());
        }
        ProjectZip project = new ProjectZip();

        TreeMap<String, TreeMap<Integer, String>> scripts;
        scripts = new TreeMap<>();

        try (ZipFile zipFile = new ZipFile(location)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            Splitter nameSplit = Splitter.on(File.separator);
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream input = zipFile.getInputStream(entry);

                // log file
                if (entry.getName().equals(Literals.LOG_FILE.getText())) {
                    try (ObjectInputStream obj = new ObjectInputStream(input)) {
                        project.writingLog = (Log) obj.readObject();
                    }

                } else if (entry.getName().equals(Literals.PROP_FILE.getText())) {
                    project.projectProps.load(input);

                } else if (entry.getName().startsWith(Literals.DOC_FOLDER.getText())) {

                    String[] names = nameSplit.splitToList(entry.getName()).toArray(new String[3]);
                    if (!scripts.containsKey(names[1])) {
                        scripts.put(names[1], new TreeMap<>());
                    }
                    String rawIdx = names[2].substring(0, names[2].indexOf(Literals.DOC_EXT.getText()));

                    try (Scanner scanner = new Scanner(input)) {
                        StringBuilder text = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            if (!text.isEmpty()) {
                                text.append('\n');
                            }
                            text.append(scanner.nextLine());
                        }
                        scripts.get(names[1]).put(Integer.parseInt(rawIdx), text.toString());
                    }

                    // Images
                } else if (entry.getName().equals(Literals.IMAGES_FILE.getText())) {
                    project.imageFiles.load(input);
                }
                input.close();

            }

            for (Entry<String, TreeMap<Integer, String>> entry : scripts.entrySet()) {
                ManuscriptFile data = new ManuscriptFile(entry.getKey());
                for (Entry<Integer, String> child : entry.getValue().entrySet()) {
                    if (child.getKey() != 1) data = new ManuscriptFile(data);
                    data.getManuscript().updateText(child.getValue());
                }
                project.documentFiles.put(entry.getKey(), data);
            }
        }
        return project;
    }

    private ProjectZip() {
        projectProps = new ProjectProperties();
        writingLog = new Log();
        imageFiles = new Properties();
        documentFiles = new TreeMap<>();
        fileLocation = Optional.empty();
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
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileLocation.get()))) {
            // log file
            zos.putNextEntry(new ZipEntry(Literals.LOG_FILE.getText()));
            log = new ObjectOutputStream(zos);
            log.writeObject(writingLog);
            zos.closeEntry();

            // property file
            zos.putNextEntry(new ZipEntry(Literals.PROP_FILE.getText()));
            projectProps.save(zos);
            zos.closeEntry();

            // text files
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

            zos.putNextEntry(new ZipEntry(Literals.IMAGES_FILE.getText()));
            imageFiles.store(zos, new String());
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
            written += writing.getManuscript().getWritingCount();
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
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    public boolean addImage(String name, File image) throws FileNotFoundException, IOException {
        if (imageFiles.containsKey(name)) {
            return false;
        }
        try (FileInputStream data = new FileInputStream(image)) {
            String read = Base64.getEncoder().encodeToString(data.readAllBytes());
            imageFiles.put(name, read);
            return true;
        }
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
    public String getEncodedImage(String name) {
        if (imageFiles.containsKey(name)) return imageFiles.getProperty(name);
        return null;
    }

    public void setFilePath(String name) throws IOException {
        File file = new File(name);
        if (file.isFile()) {
            throw new FileAlreadyExistsException(name);
        }
        file.createNewFile();
        fileLocation = Optional.of(file.getPath());
    }
}
