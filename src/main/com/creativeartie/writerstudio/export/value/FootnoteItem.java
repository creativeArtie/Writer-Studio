package com.creativeartie.writerstudio.export.value;

import java.util.*;

import com.creativeartie.writerstudio.export.*;
import com.creativeartie.writerstudio.lang.*;

public class FootnoteItem{
    
    public static int getSpanIndex(SpanBranch span, List<FootnoteItem> list){
        int i = -1;
        for (FootnoteItem item: list){
            if (item.noteSpan.equals(item)){
                return i;
            }
        }
        return -1;
    }
    
    private SpanBranch noteSpan;
    private DivisionLine noteLine;
    
    public FootnoteItem(SpanBranch span, DivisionLine line){
        noteSpan = span;
        noteLine = line;
    }
    
    public float getHeight(){
        return noteLine.getHeight();
    }
    
    public DivisionLine getPrintLine(){
        return noteLine;
    }
    
    public SpanBranch getSpan(){
        return noteSpan;
    }
}