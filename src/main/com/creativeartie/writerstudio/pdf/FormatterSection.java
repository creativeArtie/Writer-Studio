package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Represent a section of the manuscript, an essay or an research paper.
 */
public abstract class FormatterSection{

    FormatterSection(){}

    public FormatterSection setData(DataWriting data, StreamData output)
            throws IOException{
        loadData(data, output);
        return this;
    }

    protected abstract void loadData(DataWriting data, StreamData output)
        throws IOException;

    protected abstract void render(StreamPdfFile output) throws IOException;
}