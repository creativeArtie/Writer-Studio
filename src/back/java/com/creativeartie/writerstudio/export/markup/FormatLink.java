package com.creativeartie.writerstudio.export.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.*;

final class FormatLink extends Format<FormatSpanLink>{

    FormatLink(FormatSpanLink content){
        super(content);
    }

    @Override
    public boolean isSuperscript(){
        return false;
    }

    @Override
    public String getText(){
        return getSpan().getText();
    }

    @Override
    public Optional<BridgeDivision> getNote(){
        return Optional.empty();
    }

    @Override
    public Optional<String> getLink(){
        FormatSpanLink link = getSpan();
        if (link instanceof FormatSpanLinkDirect){
            return Optional.of(((FormatSpanLinkDirect)link).getPath());
        }
        return ((FormatSpanLinkRef) link).getPathSpan()
            .filter(s -> s instanceof LinedSpanPointLink)
            .map(s -> (LinedSpanPointLink) s)
            .map(l -> l.getPath());

    }
}
