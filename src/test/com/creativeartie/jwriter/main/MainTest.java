package com.creativeartie.jwriter.main;

import java.io.*;

import java.io.File;
import java.io.IOException;

import com.creativeartie.jwriter.export.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.markup.*;

public class MainTest {

    public static void main(String args[]) throws IOException {
        File file = new File("data/pdf-base.txt");
        WritingText doc = new WritingText(file);
        ManuscriptFile use = ManuscriptFile.withManuscript(doc);

        File out = new File("test.pdf");
        FileExporter.PDF_MANUSCRIPT.exportFile(use, out);
    }
}