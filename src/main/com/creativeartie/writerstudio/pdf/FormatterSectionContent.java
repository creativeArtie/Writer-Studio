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
        /// Comment out code is use for testing.
        /// variable `est` or `rd` needs to be the same as `div`
        // TODO make unit test
        // System.out.println("FormatSectionContent#render(StreamPdfFile)");
        contentPage.render(output);
            // float p = output.newStreamData().getHeight();
            // float div = page.pageContent.getHeight();
            // float head = page.pageHeader.getHeight();
            // float margin = Data.cmToPoint(3f);
            // float est = div + (margin * 2) + head;
            // float rd = div + (p / 3) + margin;
            // System.out.printf("%5.3f %5.3f %5.3f %5.3f %5.3f %5.3f \n",
            //                   div,  p,  margin, head, est,  rd);
    }
}
