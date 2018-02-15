package com.creativeartie.jwriter.export;

import java.io.*;
import java.util.Optional;
import java.util.LinkedList;
import java.util.function.*;
import com.google.common.collect.*;

import static com.creativeartie.jwriter.main.Checker.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.lang.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.property.*;

class ITextListHandler{

    private static List buildList(LinedType type){
        if (type == LinedType.NUMBERED){
            return new List(ListNumberingType.DECIMAL);
        }
        List ans = new List();
        ans.setListSymbol(BULLET_SYMBOL);
        return ans;
    }

    public static ITextListHandler start(Document doc, LinedType type){
        return new ITextListHandler(doc, type);
    }

    private static final String BULLET_SYMBOL = "•  ";
    private final Document baseDoc;
    private final LinedType listType;
    private final Optional<ITextListHandler> parentList;
    private final List filledList;
    private final int listLevel;
    private ListItem lastItem;

    private ITextListHandler(Document doc, LinedType type){
        baseDoc = doc;
        listLevel = 1;
        listType = type;
        filledList = buildList(type);
        parentList = Optional.empty();
    }

    private ITextListHandler(Document doc, ITextListHandler parent){
        baseDoc = doc;
        listLevel = parent.listLevel + 1;
        listType = parent.listType;
        filledList = buildList(listType);
        parentList = Optional.of(parent);
    }

    Optional<ITextListHandler> add(LinedSpanLevelList line){
        if (line.getLinedType() == listType){
            if (line.getLevel() == listLevel){
                lastItem = new ListItem();
                lastItem.add(ITextBridge.addLine(line.getFormattedSpan()));
                filledList.add(lastItem);
                return Optional.of(this);
            }
            if (line.getLevel() > listLevel){
                ITextListHandler child = new ITextListHandler(baseDoc, this);
                return child.add(line);
            }
            if (line.getLevel() < listLevel){
                assert listLevel > 1: line;
                parentList.get().getLast().add(filledList);
                return parentList.get().add(line);
            }
            assert false;
        }
        completed();
        ITextListHandler ans = new ITextListHandler(baseDoc, line.getLinedType());
        return ans.add(line);
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