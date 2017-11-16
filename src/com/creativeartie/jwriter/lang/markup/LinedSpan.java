package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

public abstract class LinedSpan extends SpanBranch {

    LinedSpan(List<Span> children){
        super(children);
    }

    private LinedType linedType;

    public LinedType getLinedType(){
        if (linedType == null){
            linedType = LinedType.findType(get(0).getRaw());
        }
        return linedType;
    }

    @Override
    public String toString(){
        return getLinedType() + super.toString();
    }

    @Override
    public List<DetailStyle> getBranchStyles(){
        return ImmutableList.of(getLinedType());
    }

    public int getPublishTotal(){
        return 0;
    }

    public int getNoteTotal(){
        return 0;
    }


    /* // TODO Speed up preformance by edit only some of the text
    @Override
    protected DetailUpdater getUpdater(int indexed, String text){
        Checker.checkNotNull(text, "text");
        if (getLinedType() == LinedType.findType(text)){
            SetupParser parser = getLinedType().getParser();
            int found = search(text, CHAR_ESCAPE, LINED_END);
            if (found == -1){
                return DetailUpdater.mergeNext(parser);
            } else if (found == text.length()){
                return DetailUpdater.replace(parser);
            }
        }
        return DetailUpdater.unable();
    }
    */
}
