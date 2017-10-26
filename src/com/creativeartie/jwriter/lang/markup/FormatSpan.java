package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * A {@link span} for formatted text.
 */
public abstract class FormatSpan extends SpanBranch {
    private final boolean[] spanFormats; 
    FormatSpan(List<Span> spanChildren, boolean[] formats){
        super(spanChildren);
        Checker.checkArraySize(formats, "formats", FORMAT_TYPES);
        spanFormats = Arrays.copyOf(formats, formats.length);
    }
    
    FormatSpan(){
        this(new ArrayList<>(), new boolean[FORMAT_TYPES]);
    }
    
    boolean[] getFormats(){
        return spanFormats;
    }
    
    public List<FormatType> listFormats(){
        ImmutableList.Builder<FormatType> list = ImmutableList.builder();
        int i = 0;
        for (boolean format: spanFormats){
            if (format){
                list.add(FormatType.values()[i]);
            }
            i++;
        }
        return list.build();
    }
    
    public boolean isBold(){
        return spanFormats[0];
    }
    
    public boolean isItalics(){
        return spanFormats[1];
    }
    
    public boolean isUnderline(){
        return spanFormats[2];
    }
    
    public boolean isCoded(){
        return spanFormats[3];
    }
    
    @Override
    public String toString(){
        StringBuilder ans = new StringBuilder();
        if (spanFormats[0]) ans.append("b");
        if (spanFormats[1]) ans.append("i");
        if (spanFormats[2]) ans.append("u");
        if (spanFormats[3]) ans.append("c");
        return ans.toString() + super.toString();
    }
    
    @Override
    public List<DetailStyle> getBranchStyles(){
        return ImmutableList.copyOf(listFormats());
    }
    
    public abstract String getOutput();
}
