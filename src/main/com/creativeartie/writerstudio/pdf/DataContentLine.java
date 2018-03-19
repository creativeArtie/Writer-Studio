package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

public class DataContentLine implements Data{
    private DataWriting baseData;
    private LinedSpan mainNote;
    private ArrayList<ArrayList<DataContentNote>> pointerNotes;
    private Optional<FormatterItem> itemFormatter;

    public DataContentLine(DataWriting input, LinedSpan line, StreamData data)
            throws IOException{
        baseData = input;
        pointerNotes = new ArrayList<>();
        float width = data.getRenderWidth(input.getMargin());
        /*
        FormatterItem item = null;
        switch (line.getLinedType()){
        case HEADING:
            LinedSpanLevelSection found = (LinedSpanLevelSection) line;
            break;
        }
        itemFormatter = Optional.ofNullable(item);
        */
        itemFormatter = Optional.of(new FormatterItem(width).appendText(
            line.getRaw(), getBaseFont()));
    }

    private LinedSpanLevelSection getSectionLine(){
        return null;
    }

    private FormatterItem parseLine(FormatSpanMain input, FormatterItem output){
        return output;
    }

    public Optional<FormatterItem> getFormatter(){
        return itemFormatter;
    }

    @Override
    public DataWriting getBaseData(){
        return baseData;
    }
}