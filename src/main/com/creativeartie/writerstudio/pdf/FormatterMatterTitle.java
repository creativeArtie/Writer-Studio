package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

/**
 * Prints the title page footer
 */
abstract class FormatterMatterTitle extends FormatterMatter{

    private Optional<Float> divWidth;
    private Optional<DataTitle> inputData;

    public FormatterMatterTitle(){
        inputData = Optional.empty();
        divWidth = Optional.empty();
    }

    void checkReady(){
        divWidth.orElseThrow(() -> new IllegalStateException(
            "addData(...) had not be called"));
    }

    @Override
    public float getWidth(){
        checkReady();
        return divWidth.get();
    }

    public FormatterMatterTitle setData(DataTitle data, StreamData output)
            throws IOException{
        divWidth = Optional.of(output.getRenderWidth(data.getMargin()));
        inputData = Optional.of(data);
        parseData(data, output);
        return this;
    }

    protected abstract void parseData(DataTitle data, StreamData output)
        throws IOException;

}