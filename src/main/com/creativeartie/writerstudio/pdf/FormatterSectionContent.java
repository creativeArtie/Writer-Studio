package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;
import com.creativeartie.writerstudio.main.*;

/**
 * Represent main content pages.
 */
class FormatterSectionContent extends FormatterSection{
    private int pageTotal;

    private FormatterPage contentPage;

    FormatterSectionContent() {
        contentPage = new FormatterPage(SectionType.CONTENT);
    }

    @Override
    public void loadData(Data data, StreamData output) throws IOException{
        DataContent content = data.getContentData();

        contentPage.setBasics(data, output);
        FormatterPage ptr = contentPage;
        float height = output.getHeight();
        for (DataContentLine line : content.getContentLines(output)){
            if (contentPage == ptr){
                ptr.setContentStartY(height, line.getPageBreak());
            } else {
                if (line.getPageBreak() != PageBreak.NONE){
                    ptr = ptr.nextPage();
                    ptr.setContentStartY(height, line.getPageBreak());
                }
            }
            ptr = ptr.addContent(line.getFormatter().get());

        }
    }

    @Override
    public void render(StreamPdfFile output) throws IOException{
        contentPage.render(output);
    }
}
