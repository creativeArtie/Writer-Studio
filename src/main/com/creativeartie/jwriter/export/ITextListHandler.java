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

    private static final String BULLET_SYMBOL = "•  ";
    private final Document baseDoc;
    private final LinedType listType;
    private final Optional<ITextListHandler> parentList;
    private final List filledList;
    private final int listLevel;
    private ListItem lastItem;

    public static ITextListHandler start(Document doc, LinedType type){
        return new ITextListHandler(doc, type);
    }

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
            if (listLevel == line.getLevel()){
                addLast();
                lastItem = new ListItem();
                lastItem.add(ITextBridge.addLine(line.getFormattedSpan()));
                return Optional.of(this);
            }
            if (listLevel < line.getLevel()){
                ITextListHandler child = new ITextListHandler(baseDoc, this);
                return child.add(line);
            }
            if (listLevel > line.getLevel()){
                assert listLevel > 1;
                completed(false);
                parentList.get().add(line);
                return parentList;
            }
            return Optional.of(this);
        }
        completed();
        ITextListHandler next = start(baseDoc, line.getLinedType());
        next.add(line);
        return Optional.of(next);
    }

    void completed(){
        completed(true);
    }
    void completed(boolean all){
        addLast();
        if (parentList.isPresent()){
            ListItem last = parentList.get().lastItem;
            if (last == null){
                last = new ListItem();
            }
            last.add(filledList);

            if (all) parentList.get().completed();
            return;
        }
        baseDoc.add(filledList);
    }

    private void addLast(){
        if (lastItem != null){
            filledList.add(lastItem);
            System.out.println(lastItem);
        }
    }
}