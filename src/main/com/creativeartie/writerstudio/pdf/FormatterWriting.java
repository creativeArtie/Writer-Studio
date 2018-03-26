package com.creativeartie.writerstudio.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.value.*;

public class FormatterWriting {
    private FormatterSectionTitle titlePage;
    private FormatterSectionContent contentPages;

    public FormatterWriting(){
        titlePage = new FormatterSectionTitle();
        contentPages = new FormatterSectionContent();
    }

    public FormatterWriting setData(Data input, StreamData data) throws
            IOException{
        titlePage.setData(input, data);
        contentPages.setData(input, data);
        data.setMetaData(input);
        return this;
    }

    public FormatterWriting render(StreamPdfFile output) throws IOException{
        titlePage.render(output);
        contentPages.render(output);
        return this;
    }
}