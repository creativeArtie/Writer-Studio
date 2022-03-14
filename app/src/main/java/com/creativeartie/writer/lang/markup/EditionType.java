package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.google.common.base.*;

/** Edition status of a document section.
 *
 * The value order is set by:
 * <ul>
 * <li>{@link #getNamedSyntaxes()}</li>
 * </ul>
 */
public enum EditionType {
    /** Draft with no text or just an outline. */
    STUB,
    /** Draft with text, but unedited */
    DRAFT,
    /** Draft with edited text */
    FINAL,
    /** Draft with user define status */
    OTHER,
    /** No draft status set.*/
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
