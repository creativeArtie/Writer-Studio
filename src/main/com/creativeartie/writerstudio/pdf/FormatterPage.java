package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Represent a non-title page in the output PDF file.
 */
public class FormatterPage{
    private FormatterMatterContent pageContent;
    private FormatterMatterFootnote pageFootnote;
    private FormatterMatterHeader pageHeader;
    private Optional<FormatterPage> pageNext;
    private Data commonData;
    private StreamData commonOutput;
    private SectionType sectionType;

    FormatterPage(SectionType type){
        pageContent = new FormatterMatterContent();
        pageFootnote = new FormatterMatterFootnote();
        pageHeader = new FormatterMatterHeader();
        pageNext = Optional.empty();
        sectionType = type;

    }

    FormatterPage setBasics(Data data, StreamData output) throws IOException{
        commonData = data;
        commonOutput = output;
        pageHeader.setBasics(data.getContentData(), output, sectionType);
        float height = pageHeader.getHeight();
        pageContent.setBasics(data.getContentData(), output)
            .addHeaderSpacing(height);
        pageFootnote.setBasics(data.getContentData(), output);
        return this;
    }

    void setContentStartY(float height, PageBreak breaker){
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
        pageContent.setStartY(set);
    }

    FormatterPage addContent(FormatterItem item) throws IOException{
        if (pageContent.canFit(item)){
            pageContent.addContentLine(item);
            return this;
        }
        FormatterItem allows = FormatterItem.copyFormat(item);
        FormatterItem checker = FormatterItem.copyFormat(item);
        FormatterItem overflow = null;
        for (FormatterItem.Line line: item){
            checker.addLine(line);
            if (pageContent.canFit(checker)){
                allows.addLine(line);
            } else {
                if (overflow == null){
                    if(allows.isEmpty()){
                        return nextPage().addContent(item);
                    }
                    overflow = FormatterItem.splitItem(item);
                }
                overflow.addLine(line);
            }
        }
        if (! allows.isEmpty()){
            pageContent.addContentLine(allows);
        }
        return nextPage().addContent(overflow);
    }

    FormatterPage nextPage() throws IOException{
        commonOutput.toNextPage();
        FormatterPage next = new FormatterPage(sectionType)
            .setBasics(commonData, commonOutput);
        pageNext = Optional.of(next);
        return next;
    }

    FormatterPage render(StreamPdfFile output) throws IOException{
        boolean isFirst = true;
        output.renderText(pageHeader);
        output.renderText(pageContent);
        output.renderText(pageFootnote);
        if (pageNext.isPresent()){
            pageNext.get().render(output.addPage());
        }
        return this;
    }
}