package com.creativeartie.writerstudio.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.value.*;

public interface StreamData {

    public float getWidth();

    public float getHeight();

    public int getPageNumber();

    public StreamData resetPageNumber();

    public StreamData toNextPage();

    public default float getRenderWidth(Margin margin){
        return getWidth() - margin.getLeft() - margin.getRight();
    }

    public default float getRenderHeight(Margin margin){
        return getHeight() - margin.getTop() - margin.getBottom();
    }
}