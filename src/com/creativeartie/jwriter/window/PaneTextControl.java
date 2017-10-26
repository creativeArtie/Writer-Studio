package com.creativeartie.jwriter.window;

import java.util.*;

import org.fxmisc.richtext.*;

import com.google.common.base.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.*;

public class PaneTextControl extends PaneTextView {

    @Override
    public void loadDoc(ManuscriptDocument doc){
        getTextArea().replaceText(0, getTextArea().getLength(),
            doc.getRaw());
        getButtonPane().setDocument(doc);
        // getButtonPane().refreshButtons();
    }

    @Override
    public void updateCss(final ManuscriptDocument doc){
        doc.getLeaves().forEach(leaf -> updateCss(leaf));
    }

    private void updateCss(SpanLeaf leaf){
        ArrayList<StyleProperty> list = new ArrayList<>();
        checkClass(leaf, MainSpanNote.class, list, "Note");

        leaf.getParent(LinedSpanSection.class).ifPresent(span -> {
            switch (span.getLinedType()){
            case HEADING:
                addProperty(list, "Heading");
                break;
            case OUTLINE:
                addProperty(list, "Outline");
                break;
            default:
            }
        });

        checkClass(leaf, LinedSpanAgenda.class, list, "Agenda");
         checkClass(leaf, LinedSpanPointLink.class, list, "Link");

        leaf.getParent(FormatSpan.class).ifPresent(span ->{
            if(span.isBold()) addProperty(list, "Bold");
            if(span.isItalics())   addProperty(list, "Italics");
            if(span.isUnderline()) addProperty(list, "Underline");
            if(span.isCoded())     addProperty(list, "Coded");
        });

        checkClass(leaf, FormatSpanAgenda.class, list, "Agenda");
        checkClass(leaf, FormatSpanDirectory.class, list, "Directory");
        checkClass(leaf, FormatSpanLink.class, list, "Link");
        leaf.getParent(EditionSpan.class).ifPresent(span ->{
            addProperty(list, CaseFormat.UPPER_UNDERSCORE
                .to(CaseFormat.UPPER_CAMEL, span.getEdition().name()));
        });

        switch(leaf.getLeafStyle()){
        case KEYWORD:
            addProperty(list, "Keyword");
            break;
        case FIELD:
            addProperty(list, "Field");
            break;
        case DATA:
            addProperty(list, "Data");
            break;
        case PATH:
            addProperty(list, "Path");
            break;
        case ID:
            leaf.getParent(Catalogued.class).ifPresent(span -> {
                switch(span.getBranch().getIdStatus()){
                case MULTIPLE:
                case NOT_FOUND:
                    addProperty(list, "Error");
                    break;
                case READY:
                    addProperty(list,"Ready");
                    break;
                case UNUSED:
                default:
                    addProperty(list, "Warning");
                    break;
                }
            });
        }
        addProperty(list, "All");
        getTextArea().setStyle(leaf.getStart(), leaf.getEnd(), StyleProperty
            .toCss(list));
    }

    private <T> void checkClass(SpanLeaf leaf, Class<T> clazz,
        ArrayList<StyleProperty> list, String name){
        leaf.getParent(clazz).ifPresent(span -> addProperty(list, name));
    }

    private void addProperty(ArrayList<StyleProperty> list, String name){
        list.add(Utilities.getStyleProperty("TextView." + name));
    }

    @Override
    public void moveTo(int position){
        if (position == getTextArea().getLength()){
            getTextArea().moveTo(position);
        } else {
            getTextArea().moveTo(position - 1);
        }
    }

    @Override
    public void moveTo(Span span){
        int end = span.getEnd();
        if (end == getTextArea().getLength()){
            getTextArea().moveTo(end);
        } else {
            getTextArea().moveTo(end - 1);
        }
    }

    public void returnFocus(){
        getTextArea().requestFollowCaret();
        getTextArea().requestFocus();
    }
}