package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

/** Describe data type. */
public enum InfoDataType implements StyleInfo{
    /** Data type of {@link FormattedSpan} */
    FORMATTED,
    /** Data type of {@link DirectorySpan} for {@link DirectoryType#RESEARCH}. */
    NOTE_REF,
    /** Data type of {@link ContentSpan} */
    TEXT;
}