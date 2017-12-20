package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Division of a document.
 */
public abstract class Section {
    Section(){}

    public abstract List<? extends Section> getChildren();

    public abstract Optional<LinedSpanLevelSection> getLine();

    public Optional<MainSpanSection> getSection(){
        return getLine().map(span -> (MainSpanSection) span.getParent());
    }
}
