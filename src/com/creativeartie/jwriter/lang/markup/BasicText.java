package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.base.*;

import com.creativeartie.jwriter.lang.*;

interface BasicText{
    
    public List<Span> delegate();
    
    public default String getText(){
        StringBuilder builder = new StringBuilder();
        delegate().forEach((child) -> {
            if (child instanceof BasicTextEscape){
                builder.append(((BasicTextEscape)child).getEscape());
            } else if (child instanceof SpanLeaf){
                /// Add text from a basic span
                builder.append(child.getRaw());
            } else {
                assert false: child.getClass();
            }
        });
        return CharMatcher.whitespace().collapseFrom(builder, ' ');
    }
    
    public default String getParsed(){
        return CharMatcher.whitespace().trimFrom(getText());
    }
    
    public default boolean isSpaceBegin(){
        String output = getText();
        if (output.isEmpty()){
            return false;
        }
        return CharMatcher.whitespace().matches(output.charAt(0));
    }
    
    public default boolean isSpaceEnd(){
        String output = getText();
        if (output.isEmpty()){
            return false;
        }
        return CharMatcher.whitespace()
            .matches(output.charAt(output.length() - 1));
    }
}
