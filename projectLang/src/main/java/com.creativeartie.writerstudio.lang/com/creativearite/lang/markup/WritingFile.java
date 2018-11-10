package com.creativeartie.writerstudio.lang.markup;

import java.io.*;
import java.util.*;
import java.util.Optional;
import java.util.zip.*;

import com.google.common.base.*;
import com.google.common.io.*;

import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** Stores associated files in a single zip file.
 *
 * Purpose
 * <ul>
 * <li> Saving the document </li>
 * <li> Access to individuals parts of the document </li>
 * </ul>
 */
public final class WritingFile {

    /// %Part 1: Constructors and Fields #######################################
    /// %Part 1.1: Public Static Constructors and Constants ===================

    /// file name and extension for {@link #open(File)} and {@link #save()}
    private static final String TEXT = "manuscript";
    private static final String STATS = "word-count";
    private static final String META = "meta";
    private static final String EXTENSION = ".txt";

    /** Opens a zip file.
     * @param file
     *      the zip file
     * @return answer
     */
    public static WritingFile open(File file) throws IOException{
        argumentNotNull(file, "file");

        /// {@link #WritingFile(File, WritingText, WritingStat} params:
        WritingText doc = null;
        WritingStat record = null;
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
                if (entry.getName().equals(STATS + EXTENSION)){
                    record = new WritingStat(text);
                }

                /// For meta data
                if (entry.getName().equals(META + EXTENSION)){
                    data = new WritingData(text);
                }

            }
        }

        /// Create Object or throw exception
        if (doc != null && record != null && data != null){
            return new WritingFile(file, doc, record, data);
        }
        throw new IOException("Corrupted file: document -> " + doc +
            "; records -> " + record + "; data -> " + data);
    }

    /** Create a {@linkplain WritingFile} with no data.
     *
     * @return answer
     */
    public static WritingFile newFile() {
        return new WritingFile(null, new WritingText(), new WritingStat(),
            new WritingData());
    }

    /** Create a {@linkplain WritingFile} with a test {@link WritingText}.
     *
     * @param file
     *      text document file
     * @return answer
     */
    public static WritingFile newSampleFile(File file) throws IOException{
        argumentNotNull(file, "file");
        String data = String.join("\n", Arrays.asList(
            "head-top     |left  |John Roe",
            "head-top     |left  |555 Main Street",
            "head-top     |left  |Anytown, Province",
            "head-top     |left  |Your Country",
            "head-top     |left  |(555)555-1234",
            "head-top     |left  |agent@example.com",
            "head-centre  |centre|Some Novel Title",
            "head-centre  |centre|",
            "head-centre  |centre|by",
            "head-centre  |centre|",
            "head-centre  |centre|Mary Sue",
            "head-centre  |centre|",
            "head-centre  |centre|Approx {%Stats.WordCountEst} words",
            "head-bottom  |right |Jane Doe",
            "head-bottom  |right |123 Nowhere",
            "head-bottom  |right |Anytown, Province",
            "head-bottom  |right |Your Country",
            "head-bottom  |right |(555)555-4321",
            "head-bottom  |centre|Copyright 1900 (c) Jane Doe",
            "text-header  |right |Doe/Novel/{%Stats.PageNumber}",
            "text-break   |centre|#",
            "text-ender   |centre|The End",
            "cite-starter |centre|Word Cited",
            "meta-author  |text  |Jane Smith",
            "meta-keywords|text  |example text",
            "meta-subject |text  |exmaple, novel",
            "meta-title   |text  |Some Novel Title"
        ));

        String stats = String.join("\n", Arrays.asList(
            "2018-09-11|publish-goal:5|time-goal:PT10M|publish-count:8|note-count:1|time-count:PT12M|",
            "2018-09-12|publish-goal:5|time-goal:PT10M|publish-count:17|note-count:1|time-count:PT13M|",
            "2018-09-13|publish-goal:5|time-goal:PT10M|publish-count:22|note-count:1|time-count:PT9M|",
            "2018-09-15|publish-goal:5|time-goal:PT10M|publish-count:29|note-count:1|time-count:PT14M|",
            "2018-09-16|publish-goal:5|time-goal:PT10M|publish-count:39|note-count:1|time-count:PT20M|",
            "2018-09-17|publish-goal:5|time-goal:PT10M|publish-count:42|note-count:0|time-count:PT12M|",
            "2018-09-18|publish-goal:5|time-goal:PT10M|publish-count:46|note-count:2|time-count:PT31M|",
            "2018-09-19|publish-goal:5|time-goal:PT10M|publish-count:49|note-count:3|time-count:PT12M|",
            "2018-09-21|publish-goal:5|time-goal:PT10M|publish-count:51|note-count:4|time-count:PT18M|",
            "2018-09-24|publish-goal:5|time-goal:PT10M|publish-count:62|note-count:5|time-count:PT21M|",
            "2018-09-25|publish-goal:5|time-goal:PT10M|publish-count:66|note-count:5|time-count:PT31M|",
            "2018-09-26|publish-goal:5|time-goal:PT10M|publish-count:76|note-count:5|time-count:PT51M|",
            "2018-09-27|publish-goal:5|time-goal:PT10M|publish-count:81|note-count:5|time-count:PT12M|",
            "2018-09-28|publish-goal:5|time-goal:PT10M|publish-count:86|note-count:5|time-count:PT21M|",
            "2018-09-29|publish-goal:5|time-goal:PT10M|publish-count:92|note-count:5|time-count:PT51M|",
            "2018-09-30|publish-goal:5|time-goal:PT10M|publish-count:101|note-count:5|time-count:PT21M|\n"
        ));
        WritingFile ans = new WritingFile(null, new WritingText(file),
            new WritingStat(stats), new WritingData(data));
        return ans;
    }

    /// %Part 1.2: Private Constructors and Fields =============================

    private final WritingText documentText;
    private final WritingStat recordsFile;
    private Optional<File> zipFile;
    private final WritingData metaData;

    /** {@linkplain WritingFile}'s constructor.
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
    private WritingFile(File file, WritingText doc,
            WritingStat table, WritingData data) {
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
            save(writeTo, TEXT  + EXTENSION, documentText.getRaw());
            save(writeTo, STATS + EXTENSION, recordsFile.getRaw());
            save(writeTo, META  + EXTENSION, metaData.getRaw());
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
    public WritingStat getRecords(){
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
