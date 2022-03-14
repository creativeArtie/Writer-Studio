package com.creativeartie.writer.javafx.utils;

import java.util.*;
import com.creativeartie.writer.lang.markup.*;
import com.creativeartie.writer.lang.*;

public class CodeStyleBuilder {

    private static Properties properties = buildProperties();

    private static Properties buildProperties(){
        Properties ans = new Properties();
        try {
            ans.load(FileResource.CODE_STYLE.getResourceStream());
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return ans;
    }

    private static boolean hasParent(SpanLeaf leaf, Class<?> clazz){
        return leaf.getParent(clazz).isPresent();
    }

    public static String toCss(SpanLeaf leaf){
        ArrayList<String> styles = new ArrayList<>();

        if (hasParent(leaf, NoteCardSpan.class)){
            styles.add("Note");
        }

        leaf.getParent(LinedSpanLevelSection.class).ifPresent(s ->
            styles.add(s.isHeading()? "Heading": "Outline")
        );

        if (hasParent(leaf, LinedSpanAgenda.class) ||
                hasParent(leaf, FormatSpanAgenda.class)){
            styles.add("Agenda");
        }

        if (hasParent(leaf, LinedSpanPointLink.class) ||
                hasParent(leaf, FormatSpanLink.class)){
            styles.add("Link");
        }

        if (hasParent(leaf, FormatSpanPointId.class)){
            styles.add("Directory");
        }

        if (hasParent(leaf, FormatSpanPointKey.class)){
            styles.add("Reference");
        }

        leaf.getParent(EditionSpan.class).ifPresent(span ->{
            String style = "";
            switch (span.getEditionType()){
            case STUB:
                styles.add("Stub");
                break;
            case DRAFT:
                styles.add("Draft");
                break;
            case FINAL:
                styles.add("Final");
                break;
            case OTHER:
                styles.add("Other");
                break;
            default:
                break;
            }
        });


        leaf.getParent(FormatSpan.class).ifPresent(span ->{
            if(span.isBold())      styles.add("Bold");
            if(span.isItalics())   styles.add("Italics");
            if(span.isUnderline()) styles.add("Underline");
            if(span.isCoded())     styles.add("Coded");
        });

        switch(leaf.getLeafStyle()){
        case KEYWORD:
            styles.add("Keyword");
            break;
        case FIELD:
            styles.add("Field");
            break;
        case DATA:
            styles.add("Data");
            break;
        case PATH:
            styles.add("Path");
            break;
        case ID:
            leaf.getParent(Catalogued.class).ifPresent(span -> {
                switch(((SpanBranch)span).getIdStatus()){
                case MULTIPLE:
                case NOT_FOUND:
                    styles.add("Error");
                    break;
                case READY:
                    styles.add("Ready");
                    break;
                case UNUSED:
                default:
                    styles.add("Warning");
                    break;
                }
            });
        }

        styles.add("All");
        StringBuilder builder = new StringBuilder();
        for(String key: styles){
            builder.append(properties.get(key));
        }
        return builder.toString();
    }
}
