package com.creativeartie.writerstudio.javafx;

import java.util.function.*;
import javafx.scene.control.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * A label that show hints and can change style base on the position of the
 * cursor. The two things that can change the styles are:
 * <ul>
 * <li>if the span type is the same as the hint</li>
 * <li>if the span can be add here</li>
 * </ul>
 *
 */
class CheatsheetLabel extends Label{

    /// %Part 1: Static Constructors

    /**
     * Check if FormatSpanContent class is found. Helper method of
     * getLabel(FormatTypeStyle), getLabel(DirectoryType) and
     *
     * @see #getOtherFormatLabel(CheatsheetText)
     */
    private static boolean findContent(Document doc, Integer point){
        return doc.locateSpan(point, FormattedSpan.class).isPresent();
    }

    static CheatsheetLabel getLabel(CheatsheetText text){
        int ordinal = text.ordinal();
        if (check(ordinal, CheatsheetText.LINED_HEADING,
                CheatsheetText.LINED_OUTLINE)){
            return getLinedHeadLabel(text);
        }

        if (check(ordinal, CheatsheetText.LINED_ENDNOTE,
                CheatsheetText.LINED_FOOTNOTE)){
            return getLinedNoteLabel(text);
        }

        if (check(ordinal, CheatsheetText.LINED_NUMBERED,
                CheatsheetText.LINED_BULLET)){
            return getLinedListLabel(text);
        }

        if (check(ordinal, CheatsheetText.LINED_PARAGRAPH,
                CheatsheetText.LINED_NOTE)){
            return getLinedRestLabel(text);
        }

        if (check(ordinal, CheatsheetText.EDITION_STUB,
                CheatsheetText.EDITION_OTHER)){
            return getEditionLabel(text);
        }
        if (check(ordinal, CheatsheetText.FIELD_SOURCE,
                CheatsheetText.FIELD_REF)){
            return getInfoLabel(text);
        }

        if (check(ordinal, CheatsheetText.FORMAT_BOLD,
                CheatsheetText.FORMAT_CODED)){
            return getFormatLabel(text);
        }

        if (check(ordinal, CheatsheetText.FORMAT_CITE, CheatsheetText.FORMAT_ENDNOTE)){
            return getFormatIDLabel(text);
        }
        if (text == CheatsheetText.OTHER_ID){
            return getIdentityLabel();
        }
        return getOtherFormatLabel(text);
    }

    private static boolean check(int ordinal, CheatsheetText begin,
            CheatsheetText end){
        return Range.closed(begin.ordinal(), end.ordinal()).contains(ordinal);
    }

    private static CheatsheetLabel getLinedHeadLabel(CheatsheetText text){
        boolean head = text == CheatsheetText.LINED_HEADING;
        return new CheatsheetLabel(text.getLabel(),
            (doc, point) -> doc.locateSpan(point, LinedSpanLevelSection.class)
                .map(s -> head == s.isHeading())
                .orElse(false),
            (doc, point) -> true
        );
    }

    private static CheatsheetLabel getLinedListLabel(CheatsheetText text){
        boolean head = text == CheatsheetText.LINED_NUMBERED;
        return new CheatsheetLabel(text.getLabel(),
            (doc, point) -> doc.locateSpan(point, LinedSpanLevelList.class)
                .map(s -> head == s.isNumbered())
                .orElse(false),
            (doc, point) -> true
        );
    }

    private static CheatsheetLabel getLinedNoteLabel(CheatsheetText text){
        DirectoryType type = text == CheatsheetText.LINED_ENDNOTE?
            DirectoryType.ENDNOTE: DirectoryType.FOOTNOTE;
        return new CheatsheetLabel(text.getLabel(),
            (doc, point) -> doc.locateSpan(point, LinedSpanPointNote.class)
                .map(s -> s.getDirectoryType() == type)
                .orElse(false),
            (doc, point) -> true
        );
    }

    /**
     * Get lines hints labels.
     */
    private static CheatsheetLabel getLinedRestLabel(CheatsheetText text){
        Class<?> set;
        switch (text){
        case LINED_PARAGRAPH:
            set = LinedSpanParagraph.class;
            break;
        case LINED_QUOTE:
            set = LinedSpanQuote.class;
            break;
        case LINED_BREAK:
            set = LinedSpanBreak.class;
            break;
        case LINED_AGENDA:
            set = LinedSpanAgenda.class;
            break;
        case LINED_LINK:
            set = LinedSpanPointLink.class;
            break;
        case LINED_CITE:
            set = LinedSpanCite.class;
            break;
        case LINED_NOTE:
            set = LinedSpanNote.class;
            break;
        default:
            assert false: "Unexpected type:" + text;
            set = null;
        }
        return new CheatsheetLabel(text.getLabel(),
            (doc, point) -> doc.locateSpan(point, set).isPresent(),
            (doc, point) -> true
        );
    }

    /**
     * Get edition hints labels.
     */
    private static CheatsheetLabel getEditionLabel(CheatsheetText text){
        EditionType type = EditionType.values()[
            text.ordinal() - CheatsheetText.EDITION_STUB.ordinal()
        ];
        return new CheatsheetLabel(text.getLabel(),
            (doc, point) -> doc.locateSpan(point, EditionSpan.class)
                .map(span -> span.getEditionType() == type)
                .orElse(false),
            (doc, point) -> doc.locateSpan(point, EditionSpan.class)
                .isPresent()
        );
    }

    /**
     * Get format hints labels.
     */
    private static CheatsheetLabel getFormatLabel(CheatsheetText text){
        FormatTypeStyle type = FormatTypeStyle.values()[
            text.ordinal() - CheatsheetText.FORMAT_BOLD.ordinal()
        ];
        return new CheatsheetLabel(text.getLabel(),
            (doc, point) -> doc.locateSpan(point, FormatSpan.class)
                .map(span -> span.isFormat(type))
                .orElse(false),
            CheatsheetLabel::findContent
        );
    }


    /**
     * Get in line reference spans (footnote, endnote, and note) hints labels.
     */
    private static CheatsheetLabel getFormatIDLabel(CheatsheetText text){
        DirectoryType type = DirectoryType.values()[
            text.ordinal() - CheatsheetText.FORMAT_CODED.ordinal()
        ];
        return new CheatsheetLabel(text.getLabel(),
            (doc, point) -> doc.locateSpan(point, FormatSpanPointId.class)
                .map(span -> span.getIdType() == type)
                .orElse(false),
            (doc, point) -> doc.locateSpan(point, FormattedSpan.class)
                .map(s -> s.allowNotes()).orElse(false)
        );
    }

    /**
     * Get citition field names labels
     */
    private static CheatsheetLabel getInfoLabel(CheatsheetText text){
        InfoFieldType type = InfoFieldType.values()[
            text.ordinal() - CheatsheetText.FIELD_SOURCE.ordinal()
        ];
        if (type == InfoFieldType.ERROR){
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return new CheatsheetLabel(text.getLabel(),
            (doc, point) -> doc.locateSpan(point, LinedSpanCite.class).map(
                span -> span.getInfoFieldType() == type
            ).orElse(false),
            (doc, point) -> doc.locateSpan(point, LinedSpanCite.class)
                .isPresent()
        );
    }

    /**
     * Get identity syntax hint label
     */
    private static CheatsheetLabel getIdentityLabel(){
        return new CheatsheetLabel(CheatsheetText.OTHER_ID.getLabel(),
            (doc, point) -> doc.locateSpan(point, DirectorySpan.class)
                .isPresent(),
            (doc, point) ->
                doc.locateSpan(point, FormatSpanLinkRef.class).isPresent() ||
                doc.locateSpan(point, FormatSpanPointId.class).isPresent() ||
                doc.locateSpan(point, LinedSpanLevelSection.class).isPresent() ||
                doc.locateSpan(point, LinedSpanPoint.class).isPresent() ||
                doc.locateSpan(point, LinedSpanNote.class).isPresent()
        );
    }

    private static CheatsheetLabel getOtherFormatLabel(CheatsheetText type){
        Class<?> setup = null;
        switch (type){
            case OTHER_ESCAPE:
                setup = BasicTextEscape.class;
                break;
            case FORMAT_AGENDA:
                setup = FormatSpanAgenda.class;
                break;
            case FORMAT_DIRECT_LINK:
                setup = FormatSpanLinkDirect.class;
                break;
            case FORMAT_REF_LINK:
                setup = FormatSpanLinkRef.class;
                break;
            case FORMAT_REF_KEY:
                setup = FormatSpanPointKey.class;
                break;
            default:
                assert false: "Unsupported type: " + type;
        }
        final Class<?> test = setup;
        return new CheatsheetLabel(type.getLabel(),
            (doc, point) -> doc.locateSpan(point, test).isPresent(),
            CheatsheetLabel::findContent
        );
    }

    /// %Part 2: Instance Methods, Fields, and Construtor

    private final BiPredicate<Document, Integer> testSetted;
    private final BiPredicate<Document, Integer> testAllow;

    private CheatsheetLabel(String text, BiPredicate<Document, Integer> set,
            BiPredicate<Document, Integer> allow){
        super(text);
        testSetted = set;
        testAllow = allow;
    }


    /**
     * Update Label status with the information gather from document and the
     * current position.
     */
    void updateLabelStatus(Document doc, int point){
        StyleClass.setHintClass(this, testSetted.test(doc, point),
            testAllow.test(doc, point));
    }
}
