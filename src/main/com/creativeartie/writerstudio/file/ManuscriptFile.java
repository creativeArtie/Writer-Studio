package com.creativeartie.writerstudio.file;

import com.creativeartie.writerstudio.lang.markup.*;

import java.io.*; // File, IOException, InputStreamReader
import java.util.*; // Enumeration, Optional, Arrays
import java.util.Optional;
import java.util.zip.*; // ZipEntry, ZipFile, ZipOutputStream

import com.google.common.io.*; // CharStream, Files
import com.google.common.base.*;// MoreObjects, Charsets

import com.creativeartie.writerstudio.resource.*; // MetaData

import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Stores the {@link WritingText} and {@link RecordList} in a zip file.
 * Classes outside of this package must use this class to indirectly create
 * instances of {@link RecordList} and {@link Record}.
 */
public final class ManuscriptFile {

    /// file name and extension for {@link #open(File)} and {@link #save()}
    private static final String TEXT = "manuscript";
    private static final String RECORDS = "records";
    private static final String META = "meta";
    private static final String EXTENSION = ".txt";

    /**
     * Extract text from a zip input stream. Helper method of
     * {@link #open(file)}.
     */
    private static String extractText(InputStream input) throws IOException{
        assert input != null: "Null input.";

        StringBuilder builder = new StringBuilder();

        /// Reads by character by character FUTURE: read by x amount
        int read = input.read();
        while (read != -1){
            builder.append((char)read);
            read = input.read();
        }
        return builder.toString();
    }

    /** Open a zip file. */
    public static ManuscriptFile open(File file) throws IOException{
        checkNotNull(file, "file");

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

                if (entry.getName().equals(TEXT + EXTENSION)){
                    doc = new WritingText(text);
                }

                if (entry.getName().equals(RECORDS + EXTENSION)){
                    record = new RecordList(text);
                }

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

    /** Create a {@linkplain ManuscriptFile} with no data. */
    public static ManuscriptFile newFile() {
        return new ManuscriptFile(null, new WritingText(), new RecordList(),
            new WritingData());
    }

    /**
     * Create a {@linkplain ManuscriptFile} with a test
     * {@link WritingText}.
     */
    @Deprecated
    public static ManuscriptFile withManuscript(WritingText doc){
        checkNotNull(doc, "doc");
        String data = String.join("\n", Arrays.asList(
            "head-top     |left  |John Smith",
            "head-top     |left  |555 Main Street",
            "head-top     |left  |Vancouver",
            "head-top     |left  |agent@example.com",
            "head-centre  |center|Some Novel Title",
            "head-centre  |center|",
            "head-centre  |center|by",
            "head-centre  |center|",
            "head-centre  |center|Mary Sue",
            "head-bottom  |right |Jane Smith",
            "head-bottom  |right |123 Nowhere",
            "head-bottom  |right |(555)765-4321",
            "head-bottom  |center|Copyright 1900 (c) Jane Smith",
            "text-header  |right |Smith/Novel/{Stat.PageNumber}",
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

    private final WritingText documentText;
    private final RecordList recordsFile;
    private Optional<File> zipFile;
    private final WritingData metaData;

    /** {@linkplain ManuscriptFile}'s constructor.*/
    private ManuscriptFile(File file, WritingText doc,
            RecordList table, WritingData data) {
        assert doc != null: "Null doc";
        assert table != null: "Null table";
        /// nullable file

        zipFile = Optional.ofNullable(file);
        documentText = doc;
        recordsFile = table;
        metaData = data;
    }

    public File dumpFile() throws IOException{
        File dump = new File("tmp.zip");
        int counter = 1;
        while(dump.exists()){
            dump = new File("tmp" + counter + ".zip");
            counter++;
        }
        setSave(dump);
        save();
        return dump;
    }

    public void setSave(File file){
        checkNotNull(file, "file");

        zipFile = Optional.of(file);
    }

    public WritingText getDocument(){
        return documentText;
    }

    public RecordList getRecords(){
        return recordsFile;
    }

    public WritingData getMetaData(){
        return metaData;
    }

    public boolean canSave(){
        return zipFile.isPresent();
    }

    /** Save the object into a zip file with two text files.*/
    public void save() throws IOException{
        checkIO(canSave(), "No file to save.");

        try (ZipOutputStream writeTo = new ZipOutputStream(new FileOutputStream
                (zipFile.get()))){
            save(writeTo, TEXT + EXTENSION, documentText.getRaw());
            save(writeTo, RECORDS + EXTENSION, recordsFile.getSaveText());
            save(writeTo, META + EXTENSION, metaData.getRaw());
        }
    }

    /** Save a String to a single file. Helper method of {@link #save}.*/
    private static void save(ZipOutputStream out, String path, String text)
            throws IOException {
        assert out != null: "Null out";
        assert path != null: "Null path";
        assert text != null: "Null text";

        out.putNextEntry(new ZipEntry(path));
        out.write(text.getBytes(), 0, text.length());
        out.closeEntry();
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .add("document", documentText)
            .add("records", recordsFile)
            .toString();
    }
}
