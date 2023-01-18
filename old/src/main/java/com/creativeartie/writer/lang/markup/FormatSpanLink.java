package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

/** Formatted text with links. */
public abstract class FormatSpanLink extends FormatSpan {

    /** Creates a {@linkplain FormatSpanLink}.
     *
     * @param children
     *      span children
     * @param formats
     *      format list
     */
    FormatSpanLink(List<Span> children, boolean[] formats){
        super(children, formats);
    }

    /** Gets the display output text.
     *
     * @return answer
     */
    public abstract String getText();

    /** Check if linke is not a bookmark.
     *
     * @return answer
     */
    public abstract boolean isExternal();

}
