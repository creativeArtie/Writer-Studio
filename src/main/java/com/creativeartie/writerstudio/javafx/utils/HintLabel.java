package com.creativeartie.writerstudio.javafx.utils;

import java.util.*;
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
public class HintLabel extends Label{

    /// %Part 1: Static Constructors

    /**
     * Check if FormatSpanContent class is found. Helper method of
     * getLabel(FormatTypeStyle), getLabel(DirectoryType) and
     *
     * @see #getOtherFormatLabel(HintLabel)
     */
    private static boolean findContent(SpanLeaf leaf){
        return leaf.getParent(FormattedSpan.class).isPresent();
    }

    public static HintLabel getLabel(HintText text){
        int ordinal = text.ordinal();
        if (check(ordinal, HintText.LINED_HEADING,
                HintText.LINED_OUTLINE)){
            return getLinedHeadLabel(text);
        }

        if (check(ordinal, HintText.LINED_ENDNOTE,
                HintText.LINED_FOOTNOTE)){
            return getLinedNoteLabel(text);
        }

        if (check(ordinal, HintText.LINED_NUMBERED,
                HintText.LINED_BULLET)){
            return getLinedListLabel(text);
        }

        if (check(ordinal, HintText.LINED_PARAGRAPH,
                HintText.LINED_NOTE)){
            return getLinedRestLabel(text);
        }

        if (check(ordinal, HintText.EDITION_STUB,
                HintText.EDITION_OTHER)){
            return getEditionLabel(text);
        }
        if (check(ordinal, HintText.FIELD_SOURCE,
                HintText.FIELD_REF)){
            return getInfoLabel(text);
        }

        if (check(ordinal, HintText.FORMAT_BOLD,
                HintText.FORMAT_CODED)){
            return getFormatLabel(text);
        }

        if (check(ordinal, HintText.FORMAT_CITE, HintText.FORMAT_ENDNOTE)){
            return getFormatIDLabel(text);
        }
        if (text == HintText.OTHER_ID){
            return getIdentityLabel();
        }
        return getOtherFormatLabel(text);
    }

    private static boolean check(int ordinal, HintText begin,
            HintText end){
        return Range.closed(begin.ordinal(), end.ordinal()).contains(ordinal);
    }

    private static HintLabel getLinedHeadLabel(HintText text){
        boolean head = text == HintText.LINED_HEADING;
        return new HintLabel(text.getLabel(),
            l -> l.getParent(LinedSpanLevelSection.class)
                .map(s -> head == s.isHeading())
                .orElse(false),
            l -> true
        );
    }

    private static HintLabel getLinedListLabel(HintText text){
        boolean head = text == HintText.LINED_NUMBERED;
        return new HintLabel(text.getLabel(),
            l -> l.getParent(LinedSpanLevelList.class)
                .map(s -> head == s.isNumbered())
                .orElse(false),
            l -> true
        );
    }

    private static HintLabel getLinedNoteLabel(HintText text){
        DirectoryType type = text == HintText.LINED_ENDNOTE?
            DirectoryType.ENDNOTE: DirectoryType.FOOTNOTE;
        return new HintLabel(text.getLabel(),
            l -> l.getParent(LinedSpanPointNote.class)
                .map(s -> s.getDirectoryType() == type)
                .orElse(false),
            l -> true
        );
    }

    /**
     * Get lines hints labels.
     */
    private static HintLabel getLinedRestLabel(HintText text){
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
        return new HintLabel(text.getLabel(),
            l -> l.getParent(set).isPresent(),
            l -> true
        );
    }

    /**
     * Get edition hints labels.
     */
    private static HintLabel getEditionLabel(HintText text){
        EditionType type = EditionType.values()[
            text.ordinal() - HintText.EDITION_STUB.ordinal()
        ];
        return new HintLabel(text.getLabel(),
            l -> l.getParent(EditionSpan.class)
                .map(span -> span.getEditionType() == type)
                .orElse(false),
            l -> l.getParent(EditionSpan.class)
                .isPresent()
        );
    }

    /**
     * Get format hints labels.
     */
    private static HintLabel getFormatLabel(HintText text){
        FormatTypeStyle type = FormatTypeStyle.values()[
            text.ordinal() - HintText.FORMAT_BOLD.ordinal()
        ];
        return new HintLabel(text.getLabel(),
            l -> l.getParent(FormatSpan.class)
                .map(span -> span.isFormat(type))
                .orElse(false),
            HintLabel::findContent
        );
    }


    /**
     * Get in line reference spans (footnote, endnote, and note) hints labels.
     */
    private static HintLabel getFormatIDLabel(HintText text){
        DirectoryType type = DirectoryType.values()[
            text.ordinal() - HintText.FORMAT_CODED.ordinal()
        ];
        return new HintLabel(text.getLabel(),
            l -> l.getParent(FormatSpanPointId.class)
                .map(span -> span.getIdType() == type)
                .orElse(false),
            l -> l.getParent(FormattedSpan.class)
                .map(s -> s.allowNotes()).orElse(false)
        );
    }

    /**
     * Get citition field names labels
     */
    private static HintLabel getInfoLabel(HintText text){
        InfoFieldType type = InfoFieldType.values()[
            text.ordinal() - HintText.FIELD_SOURCE.ordinal()
        ];
        if (type == InfoFieldType.ERROR){
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return new HintLabel(text.getLabel(),
            l -> l.getParent(LinedSpanCite.class)
                .map(span -> span.getInfoFieldType() == type)
                .orElse(false),
            l -> l.getParent(LinedSpanCite.class)
                .isPresent()
        );
    }

    /**
     * Get identity syntax hint label
     */
    private static HintLabel getIdentityLabel(){
        return new HintLabel(HintText.OTHER_ID.getLabel(),
            l -> l.getParent(DirectorySpan.class)
                .isPresent(),
            l ->
                l.getParent(FormatSpanLinkRef.class).isPresent() ||
                l.getParent(FormatSpanPointId.class).isPresent() ||
                l.getParent(LinedSpanLevelSection.class).isPresent() ||
                l.getParent(LinedSpanPoint.class).isPresent() ||
                l.getParent(LinedSpanNote.class).isPresent()
        );
    }

    private static HintLabel getOtherFormatLabel(HintText type){
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
        return new HintLabel(type.getLabel(),
            l -> l.getParent(test).isPresent(),
            HintLabel::findContent
        );
    }

    /// %Part 2: Instance Methods, Fields, and Construtor

    private final Predicate<SpanLeaf> testSetted;
    private final Predicate<SpanLeaf> testAllow;

    private HintLabel(String text, Predicate<SpanLeaf> set,
            Predicate<SpanLeaf> allow){
        super(text);
        testSetted = set;
        testAllow = allow;
    }


    /**
     * Update Label status with the information gather from document and the
     * current position.
     */
    public void showStatus(Document doc, int pos){
        if (doc != null){
            Optional<SpanLeaf> leaf = doc.locateLeaf(pos);
            if (leaf.isPresent()){
                showStatus(leaf.get());
                return;
            }
        }
        StyleClass.setHintClass(this, false, false);
    }

    /**
     * Update Label status with the information gather from document and the
     * current position.
     */
    public void showStatus(SpanLeaf leaf){
        if (leaf == null){
            StyleClass.setHintClass(this, false, false);
        } else {
            StyleClass.setHintClass(this, testSetted.test(leaf), testAllow
                .test(leaf));
        }
    }
}
