package com.creativeartie.writerstudio.javafx.utils;

import java.util.*;
import java.time.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.main.*;

/**
 *
 * The value order is set by:
 * <ul>
 * <li>{@link InfoFieldType} through {@link CheatsheetLabel#getLabel(HintText)}</li>
 * <li>{@link DirectoryType} through {@link CheatsheetLabel#getLabel(HintText)}</li>
 * </ul>
 */
public enum HintText {
    LINED_HEADING("LineHeading"),     LINED_OUTLINE("LineOutline"),
    LINED_ENDNOTE("LineEndnote"),     LINED_FOOTNOTE("LineFootnote"),
    LINED_NUMBERED("LineNumbered"),   LINED_BULLET("LineBullet"),
    LINED_PARAGRAPH("LineParagraph"), LINED_QUOTE("LineQuote"),
    LINED_BREAK("LineBreak"),         LINED_AGENDA("LineAgenda"),
    LINED_LINK("LineLink"),           LINED_CITE("LineSource"),
    LINED_NOTE("LineNote"),

    EDITION_STUB("EditionStub"),   EDITION_DRAFT("EditionDraft"),
    EDITION_FINAL("EditionFinal"), EDITION_OTHER("EditionOther"),

    FIELD_SOURCE("FieldSource"),     FIELD_IN_TEXT("FieldInText"),
    FIELD_FOOTNOTE("FieldFootnote"), FIELD_REF("FieldRef"),

    FORMAT_BOLD("FormatBold"),          FORMAT_ITALICS("FormatItalics"),
    FORMAT_UNDERLINE("FormatUnderline"),FORMAT_CODED("FormatCoded"),

    FORMAT_CITE("PointResearch"),   FORMAT_FOOTNOTE("PointFootnote"),
    FORMAT_ENDNOTE("PointEndnote"),

    OTHER_ID("ID"), OTHER_ESCAPE("OtherEscape"),

    FORMAT_REF_LINK("OtherRefLink"), FORMAT_DIRECT_LINK("OtherDirectLink"),
    FORMAT_AGENDA("FormatAgenda"),   FORMAT_REF_KEY("OtherRefKey");

    private static final String LABEL   = "LabelText.";
    private static final String TOOLTIP = "TooltipText.";

    private String keyPostfix;

    private HintText(String postfix){
        keyPostfix = postfix;
    }

    private static ResourceBundle texts;
    private static ResourceBundle getBundle(){
        if (texts == null){
            texts = FileResource.HINT_TEXTS.getResourceBundle();
        }
        return texts;
    }

    public String getLabel(){
        return getBundle().getString(LABEL + keyPostfix);
    }

    /*
    private String getToolTip(){
        return getBundle().getString(TOOLTIP + keyPostfix);
    }
    */

}
