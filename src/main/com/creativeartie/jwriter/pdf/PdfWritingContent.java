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
class PdfWritingContent extends PdfWriting{
    private class Page{
        private PdfSectionContent pageContent;
        private PdfSectionFootnote pageFootnote
        private int pageNumber;
    }
    private PdfSectionHeader pageHeader;
    private int pageTotal;

    private ArrayList<Page> contentPages;
    private ArrayList<Page> contentEndnotes;
    private ArrayList<Page> contentCitations;

    public PdfWritingContent(DataWriting data, StreamPdfFile output) throws
            IOException{
        contentCitations = new ArrayList<>();
        contentEndnotes = new ArrayList<>();
        outputFile = output;
    }
}