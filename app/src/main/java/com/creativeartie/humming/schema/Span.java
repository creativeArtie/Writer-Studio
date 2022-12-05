package com.creativeartie.humming.schema;

import java.util.*;

import com.creativeartie.humming.document.*;

public interface Span {

    Document getRoot();

    List<StyleClasses> getInheritedStyles();

    void setInheritedStyles(List<StyleClasses> styles);

    default void setInheritedStyles(StyleClasses... styles) {
        setInheritedStyles(Arrays.asList(styles));
    }
}
