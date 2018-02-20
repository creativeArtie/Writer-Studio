package com.creativeartie.jwriter.export;

import java.io.*;

import com.creativeartie.jwriter.file.*;

public enum FileExporter{
    PDF_MANUSCRIPT(FileExporter::pdfManuscript);

    private static void pdfManuscript(ManuscriptFile input, File output) throws
            FileNotFoundException{
        try (ITextBridge export = new ITextBridge(input, output)){
            export.parse();
        }
    }

    private final ExportLambda exportConsumer;

    private FileExporter(ExportLambda exporter){
        exportConsumer = exporter;
    }

    public void exportFile(ManuscriptFile input, File output) throws
            FileNotFoundException{
        exportConsumer.exportFile(input, output);
    }
}