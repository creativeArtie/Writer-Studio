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
class PdfSectionTitle extends PdfSection{
    private final PdfMatterTitleTop titleTop;
    private final PdfMatterTitleCenter titleCenter;
    private final PdfMatterTitleBottom titleBottom;

    public PdfSectionTitle(){
        titleTop = new PdfMatterTitleTop();
        titleCenter = new PdfMatterTitleCenter();
        titleBottom = new PdfMatterTitleBottom();
    }

    public void loadData(InputWriting data, StreamData output) throws
            IOException{
        titleTop.setData(data.getTitleData(), output);
        titleCenter.setData(data.getTitleData(), output);
        titleBottom.setData(data.getTitleData(), output);
        output.resetPageNumber();
    }

    public void render(StreamPdfFile output) throws IOException{
        PDPageContentStream stream = output.getContentStream();
        titleTop.render(stream);
        titleCenter.render(stream);
        titleBottom.render(stream);
        output.addPage();
    }
}