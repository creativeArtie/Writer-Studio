package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * All styles that are not really part of any group of styles.
 */
public enum AuxiliaryType implements StyleInfo{
    ESCAPE,       /// For BasicTextEscape
    AGENDA,       /// For FormatSpanAgenda
    DIRECT_LINK,  /// For FormatSpanLinkDirect
    REF_LINK,     /// For FormatSpanLinkRef
    NO_ID,        /// For LinedSpanPoint, MainSpanNote
    DATA_ERROR,   /// For LinedSpanCite
    MAIN_SECTION, /// For MainSpanSection
    MAIN_NOTE;    /// For MainSpanNote

    public static AuxiliaryType[] getFormatTypes(){
        return Arrays.copyOfRange(values(), 0, REF_LINK.ordinal() + 1);
    }
}
