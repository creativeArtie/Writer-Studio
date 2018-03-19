package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

import com.google.common.collect.*;

public final class InputContentLine implements Input{
    private InputWriting baseData;
    private LinedSpan mainNote;
    private ArrayList<ArrayList<InputContentNote>> pointerNotes;
    private Optional<PdfItem> contentItem;

    public InputContentLine(InputWriting input, LinedSpan line, StreamData data)
            throws IOException{
        baseData = input;
        pointerNotes = new ArrayList<>();
        float width = data.getRenderWidth(input.getMargin());
        /*
        PdfItem item = null;
        switch (line.getLinedType()){
        case HEADING:
            LinedSpanLevelSection found = (LinedSpanLevelSection) line;
            break;
        }
        contentItem = Optional.ofNullable(item);
        */
        contentItem = Optional.of(new PdfItem(width).appendText(
            line.getRaw(), getBaseFont()));
    }

    private LinedSpanLevelSection getSectionLine(){
        return null;
    }

    private PdfItem parseLine(FormatSpanMain input, PdfItem output){
        return output;
    }

    public Optional<PdfItem> getContentItem(){
        return contentItem;
    }

    public PdfItem getPdfItem(){
        return contentItem.get();
    }

    @Override
    public InputWriting getBaseData(){
        return baseData;
    }
}