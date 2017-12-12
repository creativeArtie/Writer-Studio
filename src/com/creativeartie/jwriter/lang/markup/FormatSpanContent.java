package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * A {@link ContentSpan} with format for {@link FormatSpanMain}.
 */
public class FormatSpanContent extends FormatSpan implements BasicText{

    /// Stuff for reparsing
    private final FormatParseContent spanReparser;
    private final boolean willReparse;

    FormatSpanContent(List<Span> spanChildren, boolean[] formats,
            FormatParseContent reparser, boolean reparse){
        super(spanChildren, formats);
        spanReparser = reparser;
        willReparse = reparse;
    }

    @Override
    public String getOutput(){
        return getText();
    }

    @Override
    protected SetupParser getParser(String text){
        // TODO editRaw
        return null;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }
}
