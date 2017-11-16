package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;

public abstract class MainSpan extends SpanBranch implements Catalogued{
    MainSpan(List<Span> spanChildren){
        super(spanChildren);
    }

    /* // TODO Speed up preformance by edit only some of the text
    @Override
    protected DetailUpdater getUpdater(int index, String newText){
        if (size() == 1){
            return DetailUpdater.mergeBoth(new MainParser());
        }

        if (index = 0){
            return DetailUpdater.mergeLast(new MainParser());
        }

        if (index = size() - 1){
            return DetailUpdater.mergeNext(new MainParser());
        }
        return DetailUpdater.replace(new MainParser());
    }*/

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
