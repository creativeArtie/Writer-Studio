package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

/** Section break line*/
public class LinedSpanBreak extends LinedSpan {

    /** Creates a {@linkplain LinedSpanBreak}.
     *
     * @param children
     *      span children
     * @see LinedParseRest#BREAK
     */
    LinedSpanBreak(List<Span> children){
        super(children);
    }

    @Override
    protected SetupParser getParser(String text){
        return null;
    }

}
