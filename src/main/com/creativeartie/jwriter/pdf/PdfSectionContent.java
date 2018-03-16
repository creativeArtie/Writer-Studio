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
    public void loadData(InputWriting data, StreamData output)
            throws IOException{
        PdfMatterContent content = new PdfMatterContent()
            .setBasics(data, output);
        PdfMatterFootnote footnote = new PdfMatterFootnote()
            .setBasics(data, output);
        for (InputContentLine line : data.getContentData().getContentLines(
                output)){
            Optional<PdfItem> item = content.addContentLine(line.getPdfItem());
            if (item.isPresent()){
                contentPages.add(new Page(content, footnote));
                content = new PdfMatterContent().setBasics(data, output);
                footnote = new PdfMatterFootnote().setBasics(data, output);
                output.toNextPage();
            }
        }
        contentPages.add(new Page(content, footnote));
    }

    @Override
    public void render(StreamPdfFile output) throws IOException{
        boolean isFirst = true;
        for (Page page: contentPages){
            if (isFirst){
                isFirst = false;
            } else {
                output.addPage();
            }
            page.pageContent.render(output.getContentStream());
            page.pageFootnote.render(output.getContentStream());
        }
    }
}