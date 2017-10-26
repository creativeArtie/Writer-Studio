package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

enum LinedParsePointer implements SetupParser {
    FOOTNOTE(LINED_FOOTNOTE), ENDNOTE(LINED_ENDNOTE), HYPERLINK(LINED_LINK){
    
        @Override
        public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
            ArrayList<Span> children = new ArrayList<>();
            if (pointer.startsWith(children, LINED_LINK)){
                parseCommon(children, pointer);
                if (pointer.startsWith(children, LINED_DATA)){
                    new ContentParser(SetupLeafStyle.PATH).parse(children, pointer);
                }
                pointer.startsWith(children, LINED_END);
                LinedSpanPointLink ans = new LinedSpanPointLink(children);
                return Optional.of(ans);
            }
            return Optional.empty();
        }
        
    };
    
    private final String spanStart;
    
    private LinedParsePointer(String start){
        spanStart = start;
    }
    
    DirectoryParser parseCommon(ArrayList<Span> children, SetupPointer pointer){
        Checker.checkNotNull(pointer, "childPointer");
        Checker.checkNotNull(children, "spanChildren");
        DirectoryType idType = DirectoryType.values()[ordinal() + 2];
        DirectoryParser output = new DirectoryParser(idType, LINED_DATA);
        output.parse(children, pointer);
        return output;
    }
    
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, spanStart)){
            
            parseCommon(children, pointer);
            
            if (pointer.startsWith(children, LINED_DATA)){
                new FormatParser(LINED_DATA).parse(children, pointer);
            }
            pointer.startsWith(children, LINED_END);
            
            return Optional.of(new LinedSpanPointNote(children));
        }
        return Optional.empty();
    }
}
