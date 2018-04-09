package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // Arrays

import com.creativeartie.writerstudio.lang.*; //StyleInfo

/**
 * All styles that are not really part of any group of styles.
 */
public enum AuxiliaryType implements StyleInfo{
    /** {@linkplain StyleInfo} for {@link BasicTextEscape}. */
    ESCAPE,
    /** {@linkplain StyleInfo} for {@link FormatSpanAgenda}. */
    AGENDA,
    /** {@linkplain StyleInfo} for {@link FormatSpanLinkDirect}. */
    DIRECT_LINK,
    /** {@linkplain StyleInfo} for {@link FormatSpanLinkRef}. */
    REF_LINK,
    /**
     * {@linkplain StyleInfo} when there is no {@link DirectorySpan} is found.
     * Used in {@link LinedSpanPoint}, {@link MainSpanNote}.
     */
    NO_ID,
    /**
     * {@linkplain StyleInfo} for there is a data error. Used in
     * {@link LinedSpanCite}.
     */
    DATA_ERROR,
    /** {@linkplain StyleInfo} for {@link MainSpanSection}. */
    MAIN_SECTION,
    /** {@linkplain StyleInfo} for {@link SectionSpanScene}. */
    SECTION_SCENE,
    /** {@linkplain StyleInfo} for {@link SectionSpanHead}. */
    SECTION_HEAD,
    /** {@linkplain StyleInfo} for {@link MainSpanNote}. */
    MAIN_NOTE;

    /** Get the format types that have associated syntax.*/
    public static AuxiliaryType[] getFormatTypes(){
        return Arrays.copyOfRange(values(), 0, REF_LINK.ordinal() + 1);
    }
}
