package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

public enum LinedType implements DetailStyle{
    /// name() is being used to generate keys in window_text "TextView.*"
    /// LinedType follows the same order as PaneCheatsheatLabel.Name

    HEADING(LinedParseLevel.HEADING), OUTLINE(LinedParseLevel.OUTLINE),
    NUMBERED(LinedParseLevel.NUMBERED), BULLET(LinedParseLevel.BULLET),

    FOOTNOTE(LinedParsePointer.FOOTNOTE, LINED_FOOTNOTE),
    ENDNOTE(LinedParsePointer.ENDNOTE, LINED_ENDNOTE),
    HYPERLINK(LinedParsePointer.HYPERLINK, LINED_LINK),
    NOTE(LinedParseRest.NOTE, LINED_NOTE),

    AGENDA(LinedParseRest.AGENDA, LINED_AGENDA),
    QUOTE(LinedParseRest.QUOTE, LINED_QUOTE),
    BREAK(LinedParseRest.BREAK, LINED_BREAK),
    SOURCE(LinedParseCite.INSTANCE, LINED_CITE),
    PARAGRAPH(LinedParseRest.PARAGRAPH);

    private final SetupParser setupParser;
    private final Optional<String> spanStarter;

    private LinedType(SetupParser parser){
        setupParser = parser;
        spanStarter = Optional.empty();
    }

    private LinedType(SetupParser parser, String starter){
        setupParser = parser;
        spanStarter = Optional.of(starter);
    }

    SetupParser getParser(){
        return setupParser;
    }

    static LinedType findType(String raw){
        Checker.checkNotNull(raw, "raw");
        for (LinedType type: values()){
            if (type == PARAGRAPH){
                return type;
            }
            if (type.spanStarter.isPresent() &&
                raw.startsWith(type.spanStarter.get())
            ){
                return type;
            }
            if (type.setupParser instanceof LinedParseLevel){
                LinedParseLevel level = (LinedParseLevel)type.setupParser;
                for (int i = LEVEL_MAX + 1; i > 0; i--){
                    if(raw.startsWith(getLevelToken(level, i))){
                        if (i == LEVEL_MAX + 1){
                            return LinedType.PARAGRAPH;
                        } else {
                            return type;
                        }
                    }
                }
            }
        }
        assert false;
        return null;
    }
}
