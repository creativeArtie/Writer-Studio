package com.creativeartie.writerstudio.export.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.*;

final class FormatNote extends Format<FormatSpanPointId>{
    private String noteText;

    FormatNote(FormatSpanPointId content, String text){
        super(content);
        noteText = text;
    }

    @Override
    public boolean isSuperscript(){
        return true;
    }

    @Override
    public String getText(){
        return noteText;
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
