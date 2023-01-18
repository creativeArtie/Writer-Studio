package com.creativeartie.writer.lang.markup;

import java.util.*;
import com.creativeartie.writer.lang.*;

/** A single line in the manuscript. */
public abstract class LinedSpan extends SpanBranch {

    /** Creates a {@linkplain LinedSpan}.
     *
     * @param children
     *      span children
     */
    LinedSpan(List<Span> children){
        super(children);
    }

    /** Gets the publishing manuscript word count.
     *
     * This excludes footnote, endnotes, citations, and other matters.
     *
     * @return answer
     */
    public int getPublishTotal(){
        return 0;
    }

    /** Gets the research notes word count.
     *
     * This includes footnotes, endnotes and citations.
     *
     * @return answer
     */
    public int getNoteTotal(){
        return 0;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + super.toString() + "\n";
    }
}
