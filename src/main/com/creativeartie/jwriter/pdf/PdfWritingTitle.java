package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

/**
 * Represent a title page.
 */
class PdfWritingTitle extends PdfWriting{
    private final PdfSectionTitleTop titleTop;
    private final PdfSectionTitleCenter titleCenter;
    private final PdfSectionTitleBottom titleBottom;
    private final StreamPdfFile outputFile;

    public PdfWritingTitle(DataWriting data, StreamPdfFile output) throws
            IOException{
        titleTop = new PdfSectionTitleTop(data.getTitleData(), output);
        titleCenter = new PdfSectionTitleCenter(data.getTitleData(), output);
        titleBottom = new PdfSectionTitleBottom(data.getTitleData(), output);
        outputFile = output;
    }

    public void render() throws IOException{
        PDPageContentStream stream = outputFile.getContentStream();
        titleTop.render(stream);
        titleCenter.render(stream);
        titleBottom.render(stream);
        outputFile.addPage();
    }
}