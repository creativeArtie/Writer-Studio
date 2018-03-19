package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Represent a title page.
 */
class FormatterSectionTitle extends FormatterSection{
    private final FormatterMatterTitleTop titleTop;
    private final FormatterMatterTitleCenter titleCenter;
    private final FormatterMatterTitleBottom titleBottom;

    public FormatterSectionTitle(){
        titleTop = new FormatterMatterTitleTop();
        titleCenter = new FormatterMatterTitleCenter();
        titleBottom = new FormatterMatterTitleBottom();
    }

    public void loadData(DataWriting data, StreamData output) throws
            IOException{
        titleTop.setData(data.getTitleData(), output);
        titleCenter.setData(data.getTitleData(), output);
        titleBottom.setData(data.getTitleData(), output);
    }

    public void render(StreamPdfFile output) throws IOException{
        output.renderText(titleTop);
        output.renderText(titleCenter);
        output.renderText(titleBottom);
        output.addPage();
    }
}