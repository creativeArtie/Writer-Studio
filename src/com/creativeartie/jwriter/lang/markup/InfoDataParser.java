package com.creativeartie.jwriter.lang.markup;

import java.util.Optional;

import java.util.ArrayList;

import com.google.common.base.CharMatcher;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;
import com.creativeartie.jwriter.lang.*;

public enum InfoDataParser implements SetupParser{
    FORMATTED(pointer -> {
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (new FormatParser(SetupLeafStyle.DATA).parse(children, pointer))
        {
            return Optional.of(new InfoDataSpanFormatted(children));
        }
        return Optional.empty();
    }), NUMBER(pointer -> {
        Checker.checkNotNull(pointer, "pointer");
        pointer.mark();
        ArrayList<Span> children = new ArrayList<>();
        CharMatcher data = CharMatcher.whitespace()
            .and(CharMatcher.isNot(LINED_END.charAt(0)))
            .or(CharMatcher.javaDigit());
        if (pointer.matches(children, data)){
            if (CharMatcher.javaDigit().countIn(children.get(0).getRaw()) > 0){
                if (! (new ContentParser().parse(children, pointer))){
                    pointer.getTo(children, LINED_END);
                    return Optional.of(new InfoDataSpanNumber(children));
                }
            }
        }
        pointer.rollBack(children);
        return Optional.empty();
    }), TEXT(pointer -> {
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (new ContentParser(SetupLeafStyle.DATA)
            .parse(children, pointer))
        {
            return Optional.of(new InfoDataSpanText(children));
        }
        return Optional.empty();
    }), ERROR (pointer -> {return Optional.empty();});
    
    private final SetupParser parser;
    
    private InfoDataParser(SetupParser dataParser){
        parser = dataParser;
    }
    
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        return parser.parse(pointer); /// check inside
    }
}
