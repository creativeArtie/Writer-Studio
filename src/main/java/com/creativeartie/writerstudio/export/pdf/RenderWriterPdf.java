package com.creativeartie.writerstudio.export.pdf;

import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.export.*;
import com.creativeartie.writerstudio.resource.*;

public class RenderWriterPdf implements RenderWriter<Float> {
    private final PDDocument pdfDocument;
    private final PDFont[] embedFonts;

    public RenderWriterPdf(){
        pdfDocument = new PDDocument();

        /// setup fonts
        InputStream[] fonts = FileResources.getFontFiles();
        embedFonts = new PDFont[fonts.length];
        int i = 0;
        for (InputStream font: fonts){
            embedFonts[i++] = PDType0Font.load(pdfDocument, font);
        }
    }

    public RenderPage<Float> newPage(DataPageType type){
        // return new RenderPagePdf();
        return null;
    }
}
