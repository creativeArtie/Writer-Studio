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
        try (PDFOutputStream out = new PDFOutputStream(file)){
            for (SpanLeaf leaf: outDoc.getLeaves()){
                 out.print(leaf.getRaw(), PDType1Font.HELVETICA_BOLD);
            }
        }


        /*
        try (PDFStream out = new PDFStream(file)){
            PDFStream.Line line = out.newLine();
            for (SpanLeaf leaf: outDoc.getLeaves()){
                if (leaf.getLeafStyle() == SetupLeafStyle.KEYWORD &&
                        leaf.getRaw().equals("\n")){
                    line.showLine();
                    line = out.newLine();
                } else {
                    for (String str: Splitter.on(CharMatcher.whitespace())
                        .trimResults().omitEmptyStrings().split(leaf.getRaw())){
                        if (! line.addText(str)){
                            line.showLine();
                            line = out.newLine();
                        }
                    }
                }
            }
        }*/
    }
}