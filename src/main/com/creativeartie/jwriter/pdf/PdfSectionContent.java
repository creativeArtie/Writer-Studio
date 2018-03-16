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
        private PdfMatterHeader pageHeader;
        private Page(InputWriting data, StreamData output) throws IOException{
            pageContent = new PdfMatterContent().setBasics(data, output);
            pageFootnote = new PdfMatterFootnote().setBasics(data, output);
            pageHeader = new PdfMatterHeader().setBasics(data.getContentData(),
                output);
        }

        private Optional<PdfItem> addContentLine(PdfItem item){
            return pageContent.addContentLine(item);
        }
    }

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
        Page cur = new Page(data, output);
        Optional<PdfItem> item = Optional.empty();
        for (InputContentLine line : data.getContentData().getContentLines(
                output)){
            item = cur.addContentLine(line.getPdfItem());
            while (item.isPresent()){
                contentPages.add(cur);
                output.toNextPage();
                cur = new Page(data, output);
                item = cur.addContentLine(item.get());
            }
        }
        contentPages.add(cur);
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
            page.pageHeader.render(output.getContentStream());
            page.pageContent.render(output.getContentStream());
            page.pageFootnote.render(output.getContentStream());
        }
    }
}