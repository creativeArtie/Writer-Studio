package com.creativeartie.humming.document;

import java.util.*;

public interface SpanParent extends Span {
    List<SpanLeaf> getLeafs();

    List<Integer> findChild(Span span);

    List<StyleClass> getInheritedStyles();

    boolean add(Span division);
}
