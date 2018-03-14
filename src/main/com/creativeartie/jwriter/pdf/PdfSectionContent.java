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
class PdfSectionContent extends PdfSection{
    private class Page{
        private PdfMatterContent pageContent;
        private PdfMatterFootnote pageFootnote;
        private int pageNumber;
    }
    private PdfMatterHeader pageHeader;
    private int pageTotal;

    private ArrayList<Page> contentPages;
    private ArrayList<Page> contentEndnotes;
    private ArrayList<Page> contentCitations;

    public PdfSectionContent() {
        contentCitations = new ArrayList<>();
        contentEndnotes = new ArrayList<>();
        contentPages = new ArrayList<>();
    }

    @Override
    public void loadData(DataWriting data, StreamPdfFile output)
        throws IOException{
    }

    @Override
    public void render(StreamPdfFile output) throws IOException{
    }
}