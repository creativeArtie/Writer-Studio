package com.creativeartie.jwriter.file;

import com.creativeartie.jwriter.lang.markup.*;

import java.io.*;
import java.util.zip.*;
import java.util.*;

import com.google.common.base.MoreObjects;
import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.resource.*;

/**
 * Stores the {@link WritingText} and {@link RecordList} in a zip file.
 * Classes outside of this package must use this class to indirectly create
 * instances of {@link RecordList} and {@link Record}.
 */
public final class ManuscriptFile {

    /// file name and extension for {@link #open(File)} and {@link #save()}
    private static final String TEXT = "manuscript";
    private static final String RECORDS = "records";
    private static final String META = "meta.properties";
    private static final String EXTENSION = ".txt";

    /**
     * Extract text from a zip input stream. Helper method of
     * {@link #open(file)}.
     */
    private static String extractText(ZipInputStream input) throws IOException{
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
        TextProperties prop = null;

        try (ZipInputStream input = new ZipInputStream(
            new FileInputStream(file))
        ) {
            ZipEntry entry = input.getNextEntry();
            while (entry != null){
                /// For each file extracted in the zip file:
                String text = extractText(input);

                if (entry.getName().equals(TEXT + EXTENSION)){
                    doc = new WritingText(text);
                }

                if (entry.getName().equals(RECORDS + EXTENSION)){
                    record = new RecordList(text);
                }

                if (entry.getName().equals(META)){
                    // prop = new TextProperties(input);
                }

                entry = input.getNextEntry();
            }
        }

        /// Create Object or throw exception
        if (doc != null && record != null && prop != null){
            return new ManuscriptFile(file, doc, record, prop);
        }
        throw new IOException("Corrupted file: document -> " + doc +
            "; records -> " + record + "; data -> " + prop);
    }

    /** Create a {@linkplain ManuscriptFile} with no data. */
    public static ManuscriptFile newFile() {
        return new ManuscriptFile(null, new WritingText(),
            new RecordList(), new TextProperties());
    }

    /**
     * Create a {@linkplain ManuscriptFile} with a test
     * {@link WritingText}.
     */
    @Deprecated
    public static ManuscriptFile withManuscript(WritingText doc){
        checkNotNull(doc, "doc");
        TextProperties props = new TextProperties();
        props.setText(MetaData.AGENT_NAME, "John Smith");
        props.setText(MetaData.AGENT_ADDRESS, "555 Main Street\nVancouver");
        props.setText(MetaData.AGENT_EMAIL, "test@example.com");
        props.setText(MetaData.AGENT_PHONE, "(555)123-4567");
        props.setText(MetaData.TITLE, "Some Novel Title");
        props.setText(MetaData.PEN_NAME, "Mary Sue");
        props.setText(MetaData.FIRST_NAME, "Jane");
        props.setText(MetaData.LAST_NAME, "Smith");
        props.setText(MetaData.ADDRESS, "123 Nowhere");
        props.setText(MetaData.EMAIL, "author@example.com");
        props.setText(MetaData.PHONE, "(556)765-4321");
        props.setText(MetaData.COPYRIGHT, "1900");
        ManuscriptFile ans = new ManuscriptFile(null, doc, new RecordList(),
            props);
        return ans;
    }

    private final WritingText documentText;
    private final RecordList recordsFile;
    private final TextProperties textData;
    private Optional<File> zipFile;

    /** {@linkplain ManuscriptFile}'s constructor.*/
    private ManuscriptFile(File file, WritingText doc,
            RecordList table, TextProperties prop) {
        assert doc != null: "Null doc";
        assert table != null: "Null table";
        /// nullable file

        zipFile = Optional.ofNullable(file);
        documentText = doc;
        recordsFile = table;
        textData = prop;
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

    public String getText(MetaData data){
        if (data == MetaData.BY){
            return "By";
        }
        if (data == MetaData.PAGE){
            return "Page";
        }
        if (data == MetaData.AUTHOR){
            return getText(MetaData.FIRST_NAME) + " " + getText(MetaData
                .LAST_NAME);
        }
        return textData.getText(data);
    }

    public String getText(MetaData ... keys){
        for (MetaData key: keys){
            String ans = textData.getText(key);
            if (ans != null && ans.length() > 0){
                return ans;
            }
        }
        return "";
    }

    public void setText(MetaData data, String text){
        textData.setText(data, text);
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
            ///TODO
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
