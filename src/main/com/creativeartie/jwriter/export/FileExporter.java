package com.creativeartie.jwriter.export;

import java.io.*;

import com.creativeartie.jwriter.file.*;

public enum FileExporter{
    PDF_MANUSCRIPT(ITextBridge::pdfManuscript);

    private final ExportLambda exportConsumer;

    private FileExporter(ExportLambda exporter){
        exportConsumer = exporter;
    }

    public void exportFile(ManuscriptFile input, File output) throws
            FileNotFoundException{
        exportConsumer.exportFile(input, output);
    }
}