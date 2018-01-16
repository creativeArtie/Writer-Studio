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

/**
 * A label that show hints and can change style base on the position of the
 * cursor. The two things that can change the styles are:
 * <ul>
 * <li>if the span type is the same as the hint</li>
 * <li>if the span can be add here</li>
 * </ul>
 */
class CheatsheetLabel extends Label{

    /**
     * Check if FormatSpanContent class is found. Helper method of
     * getLabel(FormatType), getLabel(DirectoryType) and
     * getLabel(AuxiliaryType).
     */
    private static boolean findContent(WritingText doc, Integer point){
        return doc.locateSpan(point, FormatSpanContent.class).isPresent();
    }

    /**
     * Get lines hints labels.
     */
    static CheatsheetLabel getLabel(LinedType type){
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, LinedSpan.class)
                .map(span -> span.getLinedType() == type)
                .orElse(false),
            (doc, point) -> true
        );
    }

    /**
     * Get format hints labels.
     */
    static CheatsheetLabel getLabel(FormatType type){
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, FormatSpan.class)
                .map(span -> span.isFormat(type))
                .orElse(false),
            CheatsheetLabel::findContent
        );
    }

    /**
     * Get edition hints labels.
     */
    static CheatsheetLabel getLabel(EditionType type){
        return new CheatsheetLabel(SyntaxHintText.LABEL.getText(type),
            (doc, point) -> doc.locateSpan(point, EditionSpan.class)
                .map(span -> span.getEdition() == type)
                .orElse(false),
            (doc, point) -> doc.locateSpan(point, EditionSpan.class)
                .isPresent()
        );
    }

    /**
     * Get in line reference spans (footnote, endnote, and note) hints labels.
     */
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

    /**
     * Get citition field names labels
     */
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

    /**
     * Get identity syntax hint label
     */
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

    /**
     * Get the other syntax hints labels.
     */
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

    private final BiPredicate<WritingText, Integer> testSetted;
    private final BiPredicate<WritingText, Integer> testAllow;

    private CheatsheetLabel(String text,
            BiPredicate<WritingText, Integer> set,
            BiPredicate<WritingText, Integer> allow){
        super(text);
        testSetted = set;
        testAllow = allow;
    }


    /**
     * Update Label status with the information gather from document and the
     * current position.
     */
    void updateLabelStatus(WritingText doc, int point){
        WindowStyleBuilder css = new WindowStyleBuilder();

        /// Add style based on cursor is on the syntax hint or not
        css.add(testSetted.test(doc, point)? WindowStyle.MARKUP_Set:
            WindowStyle.MARKUP_UNSET);

        /// Add style based on span can be add at the cursor or not
        css.add(testAllow.test(doc, point)? WindowStyle.SYNTAX_ALLOW:
            WindowStyle.SYNTAX_FORBID);

        css.add(WindowStyle.CHEATSHEET_BASE);
        setStyle(css.toString());
    }
}
