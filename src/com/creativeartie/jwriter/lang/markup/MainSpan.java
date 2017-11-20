package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

public abstract class MainSpan extends SpanBranch implements Catalogued{
    MainSpan(List<Span> spanChildren){
        super(spanChildren);
    }

    @Override
    public boolean isId(){
        return true;
    }


    public int getPublishTotal(){
        int publish = 0;
        for (Span span: this){
            publish += ((LinedSpan)span).getPublishTotal();
        }
        return publish;
    }

    public int getNoteTotal(){
        int note = 0;
        for (Span span: this){
            note += ((LinedSpan)span).getNoteTotal();
        }
        return note;
    }
}
