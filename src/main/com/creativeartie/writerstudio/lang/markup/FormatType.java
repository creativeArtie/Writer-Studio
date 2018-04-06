package com.creativeartie.writerstudio.lang.markup;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

import com.creativeartie.writerstudio.lang.StyleInfo;

/**
 * Styles for text formatting describing {@link FormatSpan}.
 */
public enum FormatType implements StyleInfo{
    /// Value order mandated by FormatSpan and FormattedParser
    BOLD, ITALICS, UNDERLINE, CODED;

}
