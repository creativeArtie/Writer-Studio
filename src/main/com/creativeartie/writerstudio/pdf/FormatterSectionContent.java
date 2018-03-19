package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Represent a title page.
 */
class FormatterSectionContent extends FormatterSection{
    private class Page{
        private FormatterMatterContent pageContent;
        private FormatterMatterFootnote pageFootnote;
        private FormatterMatterHeader pageHeader;
        private Page(DataWriting data, StreamData output) throws IOException{
            pageHeader = new FormatterMatterHeader().setBasics(data.getContentData(),
                output);
            float height = pageHeader.getHeight();
            pageContent = new FormatterMatterContent().setBasics(data, output)
                .addHeaderSpacing(height);
            pageFootnote = new FormatterMatterFootnote().setBasics(data, output);
        }
    }

    private int pageTotal;

    private ArrayList<Page> contentPages;
    private ArrayList<Page> contentEndnotes;
    private ArrayList<Page> contentCitations;

    public FormatterSectionContent() {
        contentCitations = new ArrayList<>();
        contentEndnotes = new ArrayList<>();
        contentPages = new ArrayList<>();
    }

    @Override
    public void loadData(DataWriting data, StreamData output)
            throws IOException{
        Page cur = new Page(data, output);
        Optional<FormatterItem> item = Optional.empty();
        for (DataContentLine line : data.getContentData().getContentLines(
                output)){
            item = splitItem(line.getFormatter().get(), cur);
            while (item.isPresent()){
                contentPages.add(cur);
                output.toNextPage();
                cur = new Page(data, output);
                item = splitItem(item.get(), cur);
            }
        }
        contentPages.add(cur);
    }

    private Optional<FormatterItem> splitItem(FormatterItem item, Page page){
        if (page.pageContent.addContentLine(item)){
            return Optional.empty();
        }
        Optional<FormatterItem> adding = Optional.of(FormatterItem.copySplitItem(item));
        FormatterItem check = FormatterItem.copyFormat(item);
        Optional<FormatterItem> ans = Optional.of(FormatterItem.copySplitItem(item));
        for (FormatterItem.Line line: item){
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
            output.renderText(page.pageHeader);
            output.renderText(page.pageContent);
            output.renderText(page.pageFootnote);
        }
    }
}