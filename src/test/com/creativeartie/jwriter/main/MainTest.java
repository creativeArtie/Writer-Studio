package com.creativeartie.jwriter.main;

import java.io.*;

import java.io.File;
import java.io.IOException;

import com.creativeartie.jwriter.output.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.creativeartie.jwriter.export.*;

public class MainTest {

    public static void main(String args[]) throws IOException {
        File file = new File("data/pdf-long.txt");
        WritingText doc = new WritingText(file);
        ManuscriptFile use = ManuscriptFile.withManuscript(doc);

        File out = new File("test.pdf");
        try (PdfFileOutput output = new PdfFileOutput(out)){
            output.render(new OutputInfo(use));
        }
        // FileExporter.PDF_MANUSCRIPT.exportFile(use, out);
    }
}