package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.google.common.collect.*;

/**
 * Defines the placement of the text on the page.
 */
abstract class PdfMatter<T extends Data> extends ForwardingList<PdfItem>{
    private Optional<Float> divWidth;
    private Optional<T> inputData;
    private Optional<StreamPdfFile> outputDoc;

    public PdfMatter(){
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

    public PdfMatter<T> setData(T data, StreamPdfFile output)
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

    void render(PDPageContentStream output) throws IOException{
        checkReady();
        output.beginText();
        StreamRender render = new StreamRender(output,
            getXLocation(), getYLocation(), divWidth.get());
        for (PdfItem block: this){
            render.changeAlign(block.getTextAlignment());
            for (PdfItem.Line line: block){
                // TODO change indent
                render.printText(line);
                render.nextLine(line.getHeight());
            }
        }
        output.endText();
    }

    abstract float getXLocation();
    abstract float getYLocation();
}