package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.*;

/**
 * Creates a Span to show the edition of an manuscript section.
 */
enum EditionParser implements SetupParser{
    STUB, DRAFT, FINAL, OTHER(){
        @Override
        public Optional<SpanBranch> parse(SetupPointer pointer){
            Checker.checkNotNull(pointer, "pointer");
            ArrayList<Span> children = new ArrayList<>();
            if (pointer.startsWith(children, EDITION_BEGIN)){
                return finalize(children, pointer);
            }
            return Optional.empty();
        }
    };
    
    /**
     * Helper method to parse all Spans
     */
    static boolean parseAll(ArrayList<Span> children, 
        SetupPointer pointer)
    {
        Checker.checkNotNull(children, "children");
        Checker.checkNotNull(pointer, "pointer");
        for(EditionParser parser: values()){
            if(parser.parse(children, pointer)){
                return true;
            }
        }
        return false;
    }
    
    protected Optional<SpanBranch> finalize(ArrayList<Span> children, 
        SetupPointer pointer)
    {
        Checker.checkNotNull(children, "children");
        Checker.checkNotNull(pointer, "pointer");
        /// Add the meta text, if any found
        new ContentParser().parse(children, pointer);
        
        return Optional.of(new EditionSpan(children));
        
    }
    
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, EDITION_BEGIN + name())){
            return finalize(children, pointer);
        }
        return Optional.empty();
    }
}
