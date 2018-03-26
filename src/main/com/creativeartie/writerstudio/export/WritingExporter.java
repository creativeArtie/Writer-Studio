package com.creativeartie.writerstudio.export;

import java.io.*;

import org.apache.pdfbox.pdmodel.*;

import com.creativeartie.writerstudio.file.*;

public class WritingExporter implements AutoCloseable{
    private final String savePath;
    private final PDDocument pdfDocument;
    private final SectionTitle frontMatter;

    public WritingExporter(String path){
        savePath = path;
        pdfDocument = new PDDocument();
        frontMatter = new SectionTitle();
    }

    public void export(ManuscriptFile data){
        frontMatter.export(pdfDocument, data);
    }

    @Override
    public void close() throws IOException{
        pdfDocument.save(savePath);
        pdfDocument.close();
    }
}