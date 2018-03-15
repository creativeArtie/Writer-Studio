package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

import com.creativeartie.jwriter.pdf.value.*;

import com.google.common.collect.*;

public final class InputContentLine implements Input{
    private InputWriting baseData;
    private LinedSpan mainNote;
    private ArrayList<ArrayList<InputContentNote>> pointerNotes;
    private Optional<PdfItem> contentItem;

    public InputContentLine(InputWriting data, LinedSpan line, float width)
            throws IOException{
        baseData = data;
        pointerNotes = new ArrayList<>();
        System.out.println(line);
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