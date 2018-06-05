package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

/** {@link StyleInfo} that is part of no group. */
public enum AuxiliaryType implements StyleInfo{
    /** {@linkplain StyleInfo} for {@link BasicTextEscape}. */
    ESCAPE,
    /** {@linkplain StyleInfo} for {@link FormatSpanAgenda}. */
    AGENDA,
    /** {@linkplain StyleInfo} for {@link FormatSpanLinkDirect}. */
    DIRECT_LINK,
    /** {@linkplain StyleInfo} for {@link FormatSpanLinkRef}. */
    REF_LINK,
    /** {@linkplain StyleInfo} for {@link FormatSpanPointKey}. */
    REF_KEY,
    /** {@linkplain StyleInfo} for no {@link DirectorySpan} is found.
     *
     * This is for {@link LinedSpanPoint} and {@link NoteCardSpan}.
     */
    NO_ID,
    /** {@linkplain StyleInfo} for there data error in {@link LinedSpanCite}. */
    DATA_ERROR,
    /** {@linkplain StyleInfo} for {@link SectionSpanScene}. */
    SECTION_SCENE,
    /** {@linkplain StyleInfo} for {@link SectionSpanHead}. */
    SECTION_HEAD,
    /** {@linkplain StyleInfo} for {@link NoteCardSpan}. */
    MAIN_NOTE,
    /** {@linkplain StyleInfo} for {@link StatSpanDate}. */
    STAT_DATE,
    /** {@linkplain StyleInfo} for unknown {@link StatSpanData}. */
    STAT_UNKNOWN;
}
