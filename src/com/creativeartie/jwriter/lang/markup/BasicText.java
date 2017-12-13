package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.base.*;

import com.creativeartie.jwriter.lang.*;

/**
 * Text with {@link AuxiliaryData#CHAR_ESCAPE escape character}.
 * Parent class of {@link ContentSpan} and {@link FormatSpanContent}.
 */
interface BasicText{

    public List<Span> delegate();

    /** Get the text with space collapsed, and escape character removed.*/
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

    /** Get text from {@link #getText}, but trimmed. */
    public default String getTrimmed(){
        return CharMatcher.whitespace().trimFrom(getText());
    }

    /** Check if the text starts with a whitespace. */
    public default boolean isSpaceBegin(){
        String output = getText();
        if (output.isEmpty()){
            return false;
        }
        return CharMatcher.whitespace().matches(output.charAt(0));
    }

    /** Check if the text ends with a whitespace. */
    public default boolean isSpaceEnd(){
        String output = getText();
        if (output.isEmpty()){
            return false;
        }
        return CharMatcher.whitespace()
            .matches(output.charAt(output.length() - 1));
    }

}
