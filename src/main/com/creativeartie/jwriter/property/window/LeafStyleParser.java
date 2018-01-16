package com.creativeartie.jwriter.property.window;

import java.util.*;
import java.io.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.control.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.*;
public enum LeafStyleParser {
    SCREEN("ScreenDisplay."), PRINT("PrintDisplay.");

    private String propPrefix;
    private WindowStyleBuilder builder;
    private LeafStyleParser(String prefix){
        propPrefix = prefix;
    }

    public String toCss(SpanLeaf leaf){
        builder = new WindowStyleBuilder();

        checkClass(leaf, NoteCardSpan.class, "Note");

        leaf.getParent(LinedSpanLevelSection.class).ifPresent(span -> {
            switch (span.getLinedType()){
            case HEADING:
                addKey("Heading");
                break;
            case OUTLINE:
                addKey("Outline");
                break;
            default:
            }
        });

        checkClass(leaf, LinedSpanAgenda.class, "Agenda");
        checkClass(leaf, LinedSpanPointLink.class, "Link");

        checkClass(leaf, FormatSpanAgenda.class, "Agenda");
        checkClass(leaf, FormatSpanDirectory.class, "Directory");
        checkClass(leaf, FormatSpanLink.class, "Link");
        leaf.getParent(EditionSpan.class).ifPresent(span ->{
            addKey(UPPER_UNDERSCORE.to(UPPER_CAMEL, span.getEdition().name()));
        });

        leaf.getParent(FormatSpan.class).ifPresent(span ->{
            if(span.isBold())      addKey("Bold");
            if(span.isItalics())   addKey("Italics");
            if(span.isUnderline()) addKey("Underline");
            if(span.isCoded())     addKey("Coded");
        });

        switch(leaf.getLeafStyle()){
        case KEYWORD:
            addKey("Keyword");
            break;
        case FIELD:
            addKey("Field");
            break;
        case DATA:
            addKey("Data");
            break;
        case PATH:
            addKey("Path");
            break;
        case ID:
            leaf.getParent(Catalogued.class).ifPresent(span -> {
                switch(((SpanBranch)span).getIdStatus()){
                case MULTIPLE:
                case NOT_FOUND:
                    addKey("Error");
                    break;
                case READY:
                    addKey("Ready");
                    break;
                case UNUSED:
                default:
                    addKey("Warning");
                    break;
                }
            });
        }
        addKey("All");
        return builder.toString();
    }

    private void checkClass(SpanLeaf leaf, Class<?> clazz, String name){
        leaf.getParent(clazz).ifPresent(span -> addKey(name));
    }

    private void addKey(String name){
        builder.add(propPrefix + name);
    }
}