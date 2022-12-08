package com.creativeartie.humming.document;

import java.util.*;

public interface Span {

    Document getRoot();

    List<StyleClasses> getInheritedStyles();

    void setInheritedStyles(List<StyleClasses> styles);

    default void setInheritedStyles(StyleClasses... styles) {
        setInheritedStyles(Arrays.asList(styles));
    }
}
