package com.creativeartie.writerstudio.main;

import java.io.*;

import java.io.File;
import java.io.IOException;

import com.creativeartie.writerstudio.pdf.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.export.*;

public class PdfMain {

    public static void main(String args[]) throws IOException {
        File file = new File("data/pdf-long.txt");
        WritingText doc = new WritingText(file);
        ManuscriptFile use = ManuscriptFile.withManuscript(doc);

        try (StreamPdfFile output = new StreamPdfFile("test.pdf")){
            new PdfWriting().setData(new InputWriting(use), output).render();
        }

    }
}