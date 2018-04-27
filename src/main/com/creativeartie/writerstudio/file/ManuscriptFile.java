package com.creativeartie.writerstudio.file;

import com.creativeartie.writerstudio.lang.markup.*; // WritingData, WritingText;

import java.io.*; // File, IOException, InputStreamReader;
import java.util.*; // Enumeration, Optional, Arrays;
import java.util.Optional;
import java.util.zip.*; // ZipEntry, ZipFile, ZipOutputStream;

import com.google.common.base.*;// MoreObjects, Charsets;
import com.google.common.io.*; // CharStream, Files;

import com.creativeartie.writerstudio.resource.*; // MetaData;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Stores associated files in a single zip file.
 *
 * Purpose
 * <ul>
 * <li> Saving the document </li>
 * <li> Access to individuals parts of the document </li>
 * </ul>
 */
public final class ManuscriptFile {

    /// %Part 1: Constructors and Fields #######################################
    /// %Part 1.1: Public Static Constructors and Constants ===================

    /// file name and extension for {@link #open(File)} and {@link #save()}
    private static final String TEXT = "manuscript";
    private static final String RECORDS = "records";
    private static final String META = "meta";
    private static final String EXTENSION = ".txt";

    /** Opens a zip file.
     * @param File
     *      the zip file
     * @return answer
     */
    public static ManuscriptFile open(File file) throws IOException{
        argumentNotNull(file, "file");

        /// {@link #ManuscriptFile(File,WritingText, RecordList} params:
        WritingText doc = null;
        RecordList record = null;
        WritingData data = null;

        try (ZipFile input = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> it = input.entries();
            if (it.hasMoreElements()){
                ZipEntry entry = it.nextElement();
                /// For each file extracted in the zip file:
                String text = CharStreams.toString(new InputStreamReader(
                    input.getInputStream(entry)));

                /// For actual documents
                if (entry.getName().equals(TEXT + EXTENSION)){
                    doc = new WritingText(text);
                }

                /// For goal and stats
                if (entry.getName().equals(RECORDS + EXTENSION)){
                    record = new RecordList(text);
                }

                /// For meta data
                if (entry.getName().equals(META + EXTENSION)){
                    data = new WritingData(text);
                }

            }
        }

        /// Create Object or throw exception
        if (doc != null && record != null && data != null){
            return new ManuscriptFile(file, doc, record, data);
        }
        throw new IOException("Corrupted file: document -> " + doc +
            "; records -> " + record + "; data -> " + data);
    }

    /** Create a {@linkplain ManuscriptFile} with no data.
     *
     * @return answer
     */
    public static ManuscriptFile newFile() {
        return new ManuscriptFile(null, new WritingText(), new RecordList(),
            new WritingData());
    }

    /** Create a {@linkplain ManuscriptFile} with a test {@link WritingText}.
     *
     * @param doc
     *      text document
     * @return answer
     */
    @Deprecated
    public static ManuscriptFile withManuscript(WritingText doc){
        argumentNotNull(doc, "doc");
        String data = String.join("\n", Arrays.asList(
            "head-top     |left  |John Roe",
            "head-top     |left  |555 Main Street",
            "head-top     |left  |Anytown, Province",
            "head-top     |left  |Your Country",
            "head-top     |left  |(555)555-1234",
            "head-top     |left  |agent@example.com",
            "head-centre  |center|Some Novel Title",
            "head-centre  |center|",
            "head-centre  |center|by",
            "head-centre  |center|",
            "head-centre  |center|Mary Sue",
            "head-centre  |center|",
            "head-centre  |center|Approx {%Stats.WordCountEst} words",
            "head-bottom  |right |Jane Doe",
            "head-bottom  |right |123 Nowhere",
            "head-bottom  |right |Anytown, Province",
            "head-bottom  |right |Your Country",
            "head-bottom  |right |(555)555-4321",
            "head-bottom  |center|Copyright 1900 (c) Jane Doe",
            "text-header  |right |Doe/Novel/{%Stats.PageNumber}",
            "text-break   |center|#",
            "text-after   |center|The End",
            "cite-header  |center|Word Cited",
            "meta-author  |text  |Jane Smith",
            "meta-keywords|text  |example text",
            "meta-subject |text  |exmaple, novel",
            "meta-title   |text  |Some Novel Title"
        ));
        ManuscriptFile ans = new ManuscriptFile(null, doc, new RecordList(),
            new WritingData(data));
        return ans;
    }

    /// %Part 1.2: Private Constructors and Fields =============================

    private final WritingText documentText;
    private final RecordList recordsFile;
    private Optional<File> zipFile;
    private final WritingData metaData;

    /** {@linkplain ManuscriptFile}'s constructor.
     *
     * @param file
     *      storage file
     * @param doc
     *      writing document
     * @param table
     *      record list
     * @param data
     *      writing meta data
     */
    private ManuscriptFile(File file, WritingText doc,
            RecordList table, WritingData data) {
        assert doc != null: "Null doc";
        assert table != null: "Null table";
        assert data != null: "Null data";

        zipFile = Optional.ofNullable(file);
        documentText = doc;
        recordsFile = table;
        metaData = data;
    }

    /// %Part 2: Saving Actions ################################################
    /// %Part 2.1: Saving ======================================================

    /** Dumps the file in app folder.
     *
     * @return answer
     */
    public File dumpFile() throws IOException{
        File dump = new File("backup.zip");
        int counter = 1;
        while(dump.exists()){
            dump = new File("backup" + counter + ".zip");
            counter++;
        }
        setSave(dump);
        save();
        return dump;
    }

    /** Save the object into a zip file. */
    public void save() throws IOException{
        ioCheck(canSave(), "No file to save.");

        try (ZipOutputStream writeTo = new ZipOutputStream(new FileOutputStream
                (zipFile.get()))){
            save(writeTo, TEXT + EXTENSION, documentText.getRaw());
            save(writeTo, RECORDS + EXTENSION, recordsFile.getSaveText());
            save(writeTo, META + EXTENSION, metaData.getRaw());
        }
    }

    /** Save a String to a single file. Helper method of {@link #save}.
     *
     * @param out
     *      output stream
     * @param path
     *      file path
     * @param text
     *      output text
     */
    private static void save(ZipOutputStream out, String path, String text)
            throws IOException {
        assert out != null: "Null out";
        assert path != null: "Null path";
        assert text != null: "Null text";

        out.putNextEntry(new ZipEntry(path));
        out.write(text.getBytes(), 0, text.length());
        out.closeEntry();
    }

    /// %Part 2.2 Saving Relate Get and Set Methods ============================

    /** Set the save file
     *
     * @param file
     *      new save file path
     */
    public void setSave(File file){
        argumentNotNull(file, "file");

        zipFile = Optional.of(file);
    }

    /** Check if saving is allowed
     *
     * @return answer
     */
    public boolean canSave(){
        return zipFile.isPresent();
    }

    /// %Part 3: Saved Items Get Methods #######################################

    /** Gets the writing text
     *
     * @return answer
     */
    public WritingText getDocument(){
        return documentText;
    }

    /** Gets the writing goal and daily stat records.
     *
     * @return answer
     */
    public RecordList getRecords(){
        return recordsFile;
    }

    /** Gets the writing meta data.
     *
     * @return answer
     */
    public WritingData getMetaData(){
        return metaData;
    }

    /// %Part 5: Overrides Methods =============================================

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .add("document", documentText)
            .add("records", recordsFile)
            .toString();
    }
}
