package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Line that create a section break, or a scene break in the document or a
 * novel. Represented in design/ebnf.txt as {@code LinedBreak}.
 */
public class LinedSpanBreak extends LinedSpan {

    LinedSpanBreak(List<Span> children){
        super(children);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

    @Override
    protected void childEdited(){}

    @Override
    protected void docEdited(){}
}
