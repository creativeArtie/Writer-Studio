package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.pdf.value.*;

/**
 * Represent a section of the manuscript, an essay or an research paper.
 */
public abstract class PdfSection{

    private Optional<StreamPdfFile> outputStream;

    PdfSection(){}

    public PdfSection setData(InputWriting data, StreamPdfFile output)
            throws IOException{
        outputStream = Optional.of(output);
        loadData(data, output.newStreamData());
        return this;
    }

    protected abstract void loadData(InputWriting data, StreamData output)
        throws IOException;

    public final PdfSection render() throws IOException{
        render(outputStream.orElseThrow(() ->
            new IllegalStateException("setData(...) was not call before render")
        ));
        return this;
    }

    protected abstract void render(StreamPdfFile output) throws IOException;
}