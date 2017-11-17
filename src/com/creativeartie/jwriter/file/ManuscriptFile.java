package com.creativeartie.jwriter.file;

import com.creativeartie.jwriter.lang.markup.*;

import java.io.*;
import java.util.zip.*;
import java.util.*;

import com.google.common.base.MoreObjects;
import static com.google.common.base.Preconditions.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * Stores the {@link ManuscriptDocument} and {@link RecordList} in a zip file.
 */
public final class ManuscriptFile {
    private static final String TEXT = "manuscript";
    private static final String RECORDS = "records";
    private static final String EXTENSION = ".txt";

    private static String extractText(ZipInputStream input) throws IOException{
        assert input != null: "Null input.";

        StringBuilder builder = new StringBuilder();

        int read = input.read();
        while (read != -1){
            builder.append((char)read);
            read = input.read();
        }
        return builder.toString();
    }

    public static ManuscriptFile open(File file) throws IOException{
        checkNotNull(file, "Zip file cannot be null.");

        ManuscriptDocument doc = null;
        RecordList record = null;

        try (ZipInputStream input = new ZipInputStream(new
            FileInputStream(file)))
        {
            ZipEntry entry = input.getNextEntry();
            while (entry != null){
                String text = extractText(input);
                if (entry.getName().equals(TEXT + EXTENSION)){
                    doc = new ManuscriptDocument(text);
                }
                if (entry.getName().equals(RECORDS + EXTENSION)){
                    record = new RecordList(text);
                }
                entry = input.getNextEntry();
            }
        }
        if (doc != null && record != null){
            return new ManuscriptFile(file, doc, record);
        }

        throw new IOException("Corrupted file: document -> " + doc +
            "records -> " + record);
    }

    public static ManuscriptFile newFile() {
        return new ManuscriptFile(null, new ManuscriptDocument(),
            new RecordList());
    }

    @Deprecated
    public static ManuscriptFile withManuscript(ManuscriptDocument doc){
        checkNotNull(doc, "Document can not be null.");

        return new ManuscriptFile(null, doc, new RecordList());
    }

    private final ManuscriptDocument documentText;
    private final RecordList recordsFile;
    private Optional<File> zipFile;

    private ManuscriptFile(File file, ManuscriptDocument doc,
        RecordList table)
    {
        assert doc != null: "Null doc";
        assert table != null: "Null table";
        zipFile = Optional.ofNullable(file);
        documentText = doc;
        recordsFile = table;
    }

    public void setSave(File file){
        checkNotNull(file, "Zip file can not be null.");

        zipFile = Optional.of(file);
    }

    public ManuscriptDocument getDocument(){
        return documentText;
    }

    public RecordList getRecords(){
        return recordsFile;
    }

    public boolean canSave(){
        return zipFile.isPresent();
    }

    /// {@see #canSave} to get reference
    public void save() throws IOException{
        if (! zipFile.isPresent()){
            throw new IOException("No file to save.");
        }

        try (ZipOutputStream writeTo = new ZipOutputStream(new FileOutputStream
            (zipFile.get())))
        {
            save(writeTo, TEXT + EXTENSION, documentText.getRaw());
            save(writeTo, RECORDS + EXTENSION, recordsFile.getSaveText());
        }
    }

    private static void save(ZipOutputStream writeTo, String path, String text)
        throws IOException
    {
        writeTo.putNextEntry(new ZipEntry(path));
        writeTo.write(text.getBytes(), 0, text.length());
        writeTo.closeEntry();
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .add("document", documentText)
            .add("records", recordsFile)
            .toString();
    }
}