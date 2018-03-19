package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.base.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

import com.creativeartie.writerstudio.lang.StyleInfo;
/**
 * Styles showing the current status of section in the document.
 */
public enum EditionType implements StyleInfo{
    /// Enum order mandated by WindowText

    /** The status to describe a section with empty text. */
    STUB,
    /**
     * The status to describe a section with text, but not ready to be
     * published.
     */
    DRAFT,
    /** The status to describe a section that is ready to be published. */
    FINAL,
    /** The status to describe a section that is user defined. */
    OTHER,
    /** The status to describe a section that not assigned by the user.*/
    NONE;

    /** Get edition with a name in the syntax. */
    static EditionType[] getNamedSyntaxes(){
        return Arrays.copyOfRange(values(), 0, OTHER.ordinal());
    }

    @Override
    public String toString(){
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name());
    }
}
