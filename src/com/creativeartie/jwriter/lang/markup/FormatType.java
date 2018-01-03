package com.creativeartie.jwriter.lang.markup;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

import com.creativeartie.jwriter.lang.StyleInfo;

/**
 * Styles for text formatting describing {@link FormatSpan}.
 */
public enum FormatType implements StyleInfo{
    /// Value order mandated by FormatSpan and FormatParser
    BOLD, ITALICS, UNDERLINE, CODED;

}
