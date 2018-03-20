package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Represent main content pages.
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
            pageContent = new FormatterMatterContent().setBasics(data
                .getContentData(), output).addHeaderSpacing(height);
            pageFootnote = new FormatterMatterFootnote().setBasics(data
                .getContentData(), output);
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
        boolean first = true;
        float height = output.getHeight();
        for (DataContentLine line : data.getContentData().getContentLines(
                output)){
            if (first){
                first = false;
                setContentStartY(height, line.getPageBreak(), cur);
            } else {
                if (line.getPageBreak() != PageBreak.NONE){
                    cur = nextPage(data, output, cur);
                    setContentStartY(height, line.getPageBreak(), cur);
                }
            }
            item = splitItem(line.getFormatter().get(), cur);
            while (item.isPresent()){
                cur = nextPage(data, output, cur);
                item = splitItem(item.get(), cur);
            }
        }
        contentPages.add(cur);
    }

    private void setContentStartY(float height, PageBreak breaker, Page cur){
        float set = height;
        switch (breaker){
        case HALF_WAY:
            set -= height / 2;
            break;
        case THIRD_WAY:
            set -= height / 3;
            break;
        default:
            return;
        }
        cur.pageContent.setStartY(set);
    }

    private Page nextPage(DataWriting data, StreamData output, Page cur)
            throws IOException{
        contentPages.add(cur);
        output.toNextPage();
        return new Page(data, output);
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
        System.out.println("FormatSectionContent#render(StreamPdfFile)");
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
            float p = output.newStreamData().getHeight();
            float div = page.pageContent.getHeight();
            float margin = Data.cmToPoint(3f);
            float est = div + (margin * 2);
            float rd = div + (margin * 2) + (p / 3);
            System.out.println(div + "\t" + p + "\t" + est + "\t" + rd);
        }
    }
}
