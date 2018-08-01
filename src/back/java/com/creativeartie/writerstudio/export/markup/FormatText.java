package com.creativeartie.writerstudio.export.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.*;

final class FormatText extends Format<FormatSpanContent>{

    FormatText(FormatSpanContent content){
        super(content);
    }

    @Override
    public boolean isSuperscript(){
        return false;
    }

    @Override
    public String getText(){
        return getSpan().getRendered();
    }

    @Override
    public Optional<BridgeDivision> getNote(){
        return Optional.empty();
    }

    @Override
    public Optional<String> getLink(){
        return Optional.empty();
    }
}
