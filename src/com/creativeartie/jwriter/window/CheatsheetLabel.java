package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;
import java.util.function.*;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.window.*;
import com.creativeartie.jwriter.main.*;

class CheatsheetLabel extends Label{

    private static boolean findContent(ManuscriptDocument doc, Integer point){
        return doc.locateSpan(point, FormatSpanContent.class).isPresent();
    }

    static CheatsheetLabel getLabel(LinedType type){
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, LinedSpan.class)
                .map(span -> span.getLinedType() == type)
                .orElse(false),
            (doc, point) -> true
        );
    }

    static CheatsheetLabel getLabel(FormatType type){
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, FormatSpan.class)
                .map(span -> span.isFormat(type))
                .orElse(false),
            CheatsheetLabel::findContent
        );
    }

    static CheatsheetLabel getLabel(EditionType type){
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, EditionSpan.class)
                .map(span -> span.getEdition() == type)
                .orElse(false),
            (doc, point) -> doc.locateSpan(point, EditionSpan.class)
                .isPresent()
        );
    }

    static CheatsheetLabel getLabel(DirectoryType type){
        if (type == DirectoryType.COMMENT || type == DirectoryType.LINK){
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, FormatSpanDirectory.class)
                .map(span -> span.getIdType() == type)
                .orElse(false),
            CheatsheetLabel::findContent
        );
    }

    static CheatsheetLabel getLabel(InfoFieldType type){
        if (type == InfoFieldType.ERROR){
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, LinedSpanCite.class).map(
                span -> span.getFieldType() == type
            ).orElse(false),
            (doc, point) -> doc.locateSpan(point, LinedSpanCite.class)
                .isPresent()
        );
    }

    static CheatsheetLabel getIdentityLabel(){
        return new CheatsheetLabel(SyntaxHintText.LABEL.getIdText(),
            (doc, point) -> doc.locateSpan(point, DirectorySpan.class)
                .isPresent(),
            (doc, point) ->
                doc.locateSpan(point, FormatSpanLinkRef.class).isPresent() ||
                doc.locateSpan(point, FormatSpanDirectory.class).isPresent() ||
                doc.locateSpan(point, LinedSpanLevelSection.class).isPresent() ||
                doc.locateSpan(point, LinedSpanPoint.class).isPresent() ||
                doc.locateSpan(point, LinedSpanNote.class).isPresent()
        );
    }

    static CheatsheetLabel getLabel(AuxiliaryType type){
        Class<?> setup = null;
        switch (type){
            case ESCAPE:
                setup = BasicTextEscape.class;
                break;
            case AGENDA:
                setup = FormatSpanAgenda.class;
                break;
            case DIRECT_LINK:
                setup = FormatSpanLinkDirect.class;
                break;
            case REF_LINK:
                setup = FormatSpanLinkRef.class;
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
        final Class<?> test = setup;
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, test).isPresent(),
            CheatsheetLabel::findContent
        );
    }

    private final BiPredicate<ManuscriptDocument, Integer> testSetted;
    private final BiPredicate<ManuscriptDocument, Integer> testAllow;

    private CheatsheetLabel(String text,
            BiPredicate<ManuscriptDocument, Integer> set,
            BiPredicate<ManuscriptDocument, Integer> allow){
        super(text);
        testSetted = set;
        testAllow = allow;
    }

    public boolean isSetted(ManuscriptDocument doc, int point){
        return testSetted.test(doc, point);
    }

    public boolean isAllowed(ManuscriptDocument doc, int point){
        return testAllow.test(doc, point);
    }
}