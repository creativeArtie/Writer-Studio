package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.Checker;

/**
 * A {@link Span} for text that has been formatted. This makes all {@link Span}
 * starting with {@code Format}.
 */
class FormatParser implements SetupParser {
    
    private final String[] spanEnders;
    
    private final SetupLeafStyle leafStyle;
    private final boolean willReparse;
    public FormatParser(boolean parse, String ... enders){
        this (SetupLeafStyle.TEXT, parse, enders);
    }
    
    public FormatParser(String ... enders){
        this (SetupLeafStyle.TEXT, true, enders);
    }
    
    public FormatParser(SetupLeafStyle style, String ... enders){
        this (style, true, enders);
    }
    
    public FormatParser(SetupLeafStyle style, boolean reparse, 
        String ... enders
    ){
        /// Combine the list of span enders and formatting enders
        Checker.checkNotNull(enders, "enders");
        spanEnders = SetupParser.combine(listFormatEnderTokens(), enders);
        leafStyle = Checker.checkNotNull(style, "style");
        willReparse = reparse;
    }
    
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        Checker.checkNotNull(pointer, "pointer");
        
        /// Setup format style: bold, italics, underline, coded
        boolean[] formats = new boolean[]{false, false, false, false};
        
        /// Setup for FormatSpanMain
        ArrayList<Span> children = new ArrayList<>();
        
        /// check where the loop ends
        boolean more;
        
        do {
            more = false; /// Assume FormatSpanMain has ended
            
            /// try to find text first
            if (new FormatParseContent(leafStyle, formats, willReparse, spanEnders)
                .parse(children, pointer)
            ){
                more = true;
            }
            
            if (FormatParseAgenda.PARSER.parse(children, pointer)){
                more = true;
            }
            
            /// Keeps FomratContentParser parsing alone b/c of needs to edit format
            int i = 0;
            for (String type : listFormatTextTokens()){
                if (pointer.startsWith(children, type)){
                    /// change format of bold/italics/underline/code
                    formats[i] = ! formats[i];
                    
                    more = true;
                    break;
                }
                i++;
            }
            
            /// Lastly deal with FormatParseCurly and FormatParseLink together
            for (SetupParser parser: SetupParser.combine(
                FormatParseDirectory.getParsers(formats), 
                FormatParseLink.getParsers(formats)
            )){
                if(parser.parse(children, pointer)){
                    /// Striaght forwarding adding of found spans.
                    more = true;
                }
            }
        } while(more);
        
        /// Add the FormatParser with its children spans if there are children.
        if (children.size() > 0){
            return Optional.of(new FormatSpanMain(children));
        }
        return Optional.empty();
    }
}
