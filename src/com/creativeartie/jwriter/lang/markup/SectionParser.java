package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Parser for {@code MainSpan*} classes. See {@code design/SectionParser.txt}
 * for details
 *
 */
interface SectionParser extends SetupParser {

    static final String[] HEAD_STARTERS = SetupParser.combine(
        getLevelTokens(LinedParseLevel.HEADING),
        getLevelTokens(LinedParseLevel.OUTLINE));

    public static void parseContent(ArrayList<Span> children,
            SetupPointer pointer){
        while (! pointer.hasNext(HEAD_STARTERS)){
            if (! NoteCardParser.PARSER.parse(children, pointer)){
                for (SetupParser parser: SECTION_PARSERS){
                    if (parser.parse(children, pointer)){
                        break;
                    }
                }
            }
        }
    }

    public static boolean hasChild(SetupPointer pointer, SectionParser[] parsers,
            SectionParser current){
        boolean checking = false;
        for (SectionParser parser: parsers){
            if (parser == current){
                checking = true;
            } else if (pointer.hasNext(parser.getStarter())){
                    return true;
            }
        }
        return false;
    }

    public String getStarter();
}
