package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Represent a title page.
 */
class PdfSectionContent extends PdfSection{
    private class Page{
        private PdfMatterContent pageContent;
        private PdfMatterFootnote pageFootnote;
        private PdfMatterHeader pageHeader;
        private Page(InputWriting data, StreamData output) throws IOException{
            pageHeader = new PdfMatterHeader().setBasics(data.getContentData(),
                output);
            float height = pageHeader.getHeight();
            pageContent = new PdfMatterContent().setBasics(data, output)
                .addHeaderSpacing(height);
            pageFootnote = new PdfMatterFootnote().setBasics(data, output);
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
            item = splitItem(line.getPdfItem(), cur);
            while (item.isPresent()){
                contentPages.add(cur);
                output.toNextPage();
                cur = new Page(data, output);
                item = splitItem(item.get(), cur);
            }
        }
        contentPages.add(cur);
    }

    private Optional<PdfItem> splitItem(PdfItem item, Page page){
        if (page.pageContent.addContentLine(item)){
            return Optional.empty();
        }
        Optional<PdfItem> adding = Optional.of(PdfItem.copySplitItem(item));
        PdfItem check = PdfItem.copyFormat(item);
        Optional<PdfItem> ans = Optional.of(PdfItem.copySplitItem(item));
        for (PdfItem.Line line: item){
            check.addLine(line);
            if (page.pageContent.canFit(check)){
                adding.get().addLine(line);
            } else {
                ans.get().addLine(line);
                adding.ifPresent(add -> page.pageContent.addContentLine(add));
                adding = Optional.empty();
            }
        }
        return ans;
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