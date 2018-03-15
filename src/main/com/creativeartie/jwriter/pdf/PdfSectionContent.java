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
        private Page(PdfMatterContent content, PdfMatterFootnote footnote){
            pageContent = content;
            pageFootnote = footnote;
        }
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
    public void loadData(InputWriting data, StreamPdfFile output)
            throws IOException{
        PdfMatterContent content = new PdfMatterContent();
        content.setBasics(data, output);
        PdfMatterFootnote footnote = new PdfMatterFootnote();
        footnote.setBasics(data, output);
        for (InputContentLine line : data.getContentData().getContentLines(content.getWidth())){
            content.addContentLine(line.getPdfItem());
        }
        contentPages.add(new Page(content, footnote));
    }

    @Override
    public void render(StreamPdfFile output) throws IOException{
        for (Page page: contentPages){
            page.pageContent.render(output.getContentStream());
            page.pageFootnote.render(output.getContentStream());
        }
    }
}