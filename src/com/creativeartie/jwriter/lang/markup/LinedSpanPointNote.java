package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

public class LinedSpanPointNote extends LinedSpanPoint {

    LinedSpanPointNote(List<Span> children){
        super(children);
    }
    
    @Override
    public DirectoryType getDirectoryType(){
        return getLinedType() == LinedType.FOOTNOTE? DirectoryType.FOOTNOTE: 
            DirectoryType.ENDNOTE;
    }
    
    public Optional<FormatSpanMain> getFormattedSpan(){
        return spanFromLast(FormatSpanMain.class);
    }
}
