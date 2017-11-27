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

class PaneCheatsheetLabel extends Label{
    static PaneCheatsheetLabel getLabel(LinedType type){
        return new PaneCheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, LinedSpan.class)
                .map(span -> span.getLinedType() == type)
                .orElse(false)
        );
    }

    static PaneCheatsheetLabel getLabel(FormatType type){
        return new PaneCheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, FormatSpan.class)
                .map(span -> span.isFormat(type))
                .orElse(false)
        );
    }

    static PaneCheatsheetLabel getLabel(EditionType type){
        return new PaneCheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, EditionSpan.class)
                .map(span -> span.getEdition() == type)
                .orElse(false)
        );
    }

    static PaneCheatsheetLabel getLabel(DirectoryType type){
        if (type == DirectoryType.COMMENT || type == DirectoryType.LINK){
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return new PaneCheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, FormatSpanDirectory.class)
                .map(span -> span.getIdType() == type)
                .orElse(false)
            );
    }

    static PaneCheatsheetLabel getLabel(InfoFieldType type){
        if (type == InfoFieldType.ERROR){
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return new PaneCheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, LinedSpanCite.class).map(
                span -> span.getFieldType() == type
            ).orElse(false)
        );
    }

    static PaneCheatsheetLabel getIdentityLabel(){
        return new PaneCheatsheetLabel(SyntaxHintText.LABEL.getIdText(),
            (doc, point) -> doc.locateSpan(point, DirectorySpan.class).isPresent()
        );
    }

    static PaneCheatsheetLabel getLabel(AuxiliaryType type){
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
        return new PaneCheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, test).isPresent()
        );
    }

    private final BiPredicate<ManuscriptDocument, Integer> testSetted;

    private PaneCheatsheetLabel(String text,
            BiPredicate<ManuscriptDocument, Integer> set){
        super(text);
        testSetted = set;
    }

    public boolean isSetted(ManuscriptDocument doc, int point){
        return testSetted.test(doc, point);
    }
}
