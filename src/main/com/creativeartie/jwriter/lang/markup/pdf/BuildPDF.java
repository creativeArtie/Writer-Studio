package com.creativeartie.jwriter.lang.markup.pdf;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import java.io.*;

import com.google.common.base.*;

public final class BuildPDF{
    private ManuscriptDocument outDoc;

    public BuildPDF(ManuscriptDocument doc){
        outDoc = doc;
    }

    public void export(File file) throws IOException{
        PDFont font = PDType1Font.HELVETICA_BOLD;
        try (PDFOutputStream out = new PDFOutputStream(file, font)){
            for (SpanLeaf leaf: outDoc.getLeaves()){
                if (leaf.getRaw() == "\n"){
                    out.println(font);
                }
                 out.print(leaf.getRaw(), font);
            }
        }
    }
}