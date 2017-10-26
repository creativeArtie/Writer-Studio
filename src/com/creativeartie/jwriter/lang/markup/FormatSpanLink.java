package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.creativeartie.jwriter.lang.*;

/**
 * A formatted {@link Span} for common methods in {@link FormatSpanLinkDirect} 
 * and {@link FormatSpanLinkRef}.
 */
public abstract class FormatSpanLink extends FormatSpan {
    
    FormatSpanLink(List<Span> children, boolean[] formats){
        super(children, formats);
    }
    
    /**
     * Return the path of the link.
     */
    public abstract String getPath();
    
    /**
     * Return the display output text.
     */
    public abstract String getText();
    
    @Override 
    public String getOutput(){
        return getText();
    }
}
