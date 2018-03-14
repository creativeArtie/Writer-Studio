package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.jwriter.pdf.value.*;

import com.google.common.collect.*;

/**
 * Prints the title page footer
 */
abstract class PdfMatterTitle<T extends Data> extends PdfMatter{

    private Optional<Float> divWidth;
    private Optional<T> inputData;
    private Optional<StreamPdfFile> outputDoc;

    public PdfMatterTitle(){
        inputData = Optional.empty();
        outputDoc = Optional.empty();
        divWidth = Optional.empty();
    }

    void checkReady(){
        divWidth.orElseThrow(() -> new IllegalStateException(
            "addData(...) had not be called"));
    }

    public float getWidth(){
        checkReady();
        return divWidth.get();
    }

    public PdfMatterTitle<T> setData(T data, StreamPdfFile output)
            throws IOException{
        divWidth = Optional.of(output.getPage().getMediaBox().getWidth() -
            (data.getMargin() * 2));
        inputData = Optional.of(data);
        outputDoc = Optional.of(output);
        parseData(data, output);
        return this;
    }

    protected abstract void parseData(T data, StreamPdfFile output)
        throws IOException;

}