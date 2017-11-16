package com.creativeartie.jwriter.file;

import com.creativeartie.jwriter.lang.markup.*;

import java.io.*;
import java.util.zip.*;
import java.util.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

public class ManuscriptFile {
    private static final int BUFFER_SIZE = 2048;
    private static final String MAIN = "manuscript";
    private static final String RECORDS = "records";
    private static final String POST_FIX = ".txt";
    private final ManuscriptDocument documentText;
    private final RecordTable recordsFile;
    private Optional<File> zipFile;

    public static ManuscriptFile open(File file) throws IOException{
        try (ZipInputStream input = new ZipInputStream(new
            FileInputStream(file)))
        {
            ZipEntry entry = input.getNextEntry();
            ManuscriptDocument doc = null;
            RecordTable record = null;
            while (entry != null){
                StringBuilder builder = new StringBuilder();
                byte[] bytesIn = new byte[BUFFER_SIZE];
                int read = 0;
                while ((read = input.read(bytesIn)) != -1) {
                    builder.append(new String(bytesIn));
                }
                if (entry.getName().equals(MAIN + POST_FIX)){
                    doc = new ManuscriptDocument(builder.toString());
                }
                if (entry.getName().equals(RECORDS + POST_FIX)){
                    record = new RecordTable(builder.toString());
                }
                entry = input.getNextEntry();
            }
            if (doc != null && record != null){
                return new ManuscriptFile(file, doc, record);
            }
            System.out.println(doc);
            System.out.println(record);
            throw new IOException("Corrupted file.");
        }
    }

    public static ManuscriptFile newFile() {
        return new ManuscriptFile(null, new ManuscriptDocument(),
            new RecordTable());
    }

    @Deprecated
    public static ManuscriptFile withManuscript(ManuscriptDocument doc){
        return new ManuscriptFile(null, doc, new RecordTable());
    }

    private ManuscriptFile(File file, ManuscriptDocument doc,
        RecordTable table)
    {
        zipFile = Optional.ofNullable(file);
        documentText = doc;
        recordsFile = table;
    }

    public void setSave(File file){
        zipFile = Optional.of(file);
    }

    public ManuscriptDocument getDocument(){
        return documentText;
    }

    public RecordTable getRecords(){
        return recordsFile;
    }

    public boolean canSave(){
        return zipFile.isPresent();
    }

    public void save() throws IOException{
        if (! zipFile.isPresent()){
            throw new IOException("No file to save.");
        }
        try (ZipOutputStream writeTo = new ZipOutputStream(new FileOutputStream
            (zipFile.get())))
        {
            save(writeTo, MAIN + POST_FIX, documentText.getRaw());
            save(writeTo, RECORDS + POST_FIX, recordsFile.getSaveText());
        }
    }

    private void save(ZipOutputStream writeTo, String path, String text)
        throws IOException
    {
        writeTo.putNextEntry(new ZipEntry(path));
        writeTo.write(text.getBytes(), 0, text.length());
        writeTo.closeEntry();
    }
}