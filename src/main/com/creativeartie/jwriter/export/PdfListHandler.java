package com.creativeartie.jwriter.export;

import java.io.*;
import java.util.*;
import java.util.function.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.*;
import com.itextpdf.layout.renderer.*;
import com.itextpdf.kernel.geom.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.layout.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.List;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.kernel.pdf.action.*;
import com.itextpdf.kernel.colors.*;
import com.itextpdf.kernel.events.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.font.*;
import com.itextpdf.kernel.pdf.canvas.draw.*;

class PdfListHandler{
    private static final String BULLET_SYMBOL = "•  ";

    static List buildList(LinedType type){
        if (type == LinedType.NUMBERED){
            return new List(ListNumberingType.DECIMAL);
        }
        List ans = new List();
        ans.setListSymbol(BULLET_SYMBOL);
        return ans;
    }

    static PdfListHandler start(Document doc, LinedType type){
        return new PdfListHandler(doc, type);
    }

    private final Document baseDoc;
    private final LinedType listType;
    private final Optional<PdfListHandler> parentList;
    private final List filledList;
    private final int listLevel;
    private ListItem lastItem;

    private PdfListHandler(Document doc, LinedType type){
        baseDoc = doc;
        listLevel = 1;
        listType = type;
        filledList = buildList(type);
        parentList = Optional.empty();
    }

    private PdfListHandler(Document doc, PdfListHandler parent){
        baseDoc = doc;
        listLevel = parent.listLevel + 1;
        listType = parent.listType;
        filledList = buildList(listType);
        parentList = Optional.of(parent);
    }

    Optional<PdfListHandler> add(LinedSpanLevelList line,
            Function<LinedSpanLevelList, Paragraph> formatter){
        if (line.getLinedType() == listType){
            if (line.getLevel() == listLevel){
                lastItem = new ListItem();
                lastItem.add(formatter.apply(line));
                filledList.add(lastItem);
                return Optional.of(this);
            }
            if (line.getLevel() > listLevel){
                PdfListHandler child = new PdfListHandler(baseDoc, this);
                return child.add(line, formatter);
            }
            if (line.getLevel() < listLevel){
                assert listLevel > 1: line;
                parentList.get().getLast().add(filledList);
                return parentList.get().add(line, formatter);
            }
            assert false;
        }
        completed();
        PdfListHandler ans = new PdfListHandler(baseDoc, line.getLinedType());
        return ans.add(line, formatter);
    }

    private ListItem getLast(){
        if (lastItem == null){
            lastItem = new ListItem();
            filledList.add(lastItem);
        }
        return lastItem;
    }

    void completed(){
        if(parentList.isPresent()){
            parentList.get().getLast().add(filledList);
            parentList.get().completed();
        } else {
            baseDoc.add(filledList);
        }
    }
}