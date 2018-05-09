package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt SectionHead1} and {@code SectionHead}. */
enum SectionParseHead implements SectionParser {
    /** Top most section.
     *
     * This is the main parser for {@link WritingText}.
     */
    SECTION_1,
    /** Section 2. */ SECTION_2,     /** Section 3. */ SECTION_3,
    /** Section 4. */ SECTION_4,    /** Section 5. */ SECTION_5,
    /** Section 6. */ SECTION_6;

    private final String lineStarter;
    private static final String OUTLINE = LEVEL_STARTERS
        .get(LinedParseLevel.OUTLINE).get(0);

    /** Creates a {@line SectionParseHead}. */
    private SectionParseHead(){
        lineStarter = LEVEL_STARTERS.get(LinedParseLevel.HEADING).get(ordinal());
    }

    @Override
    public String getStarter(){
        return lineStarter;
    }

    @Override
    public Optional<String> getNextStarter(){
        if (ordinal() < values().length - 1){
            return Optional.of(values()[ordinal() + 1].lineStarter);
        }
        return Optional.empty();
    }

    @Override
    public LinedParseLevel getHeadParser(){
        return LinedParseLevel.HEADING;
    }

    /** parse the outline
     *
     * @param pointer
     *      setup pointer
     * @param children
     *      span children
     * @see SectionParser#parse(SetupPointer)
     */
    static void parseOutline(SetupPointer pointer, ArrayList<Span> children){
        while (pointer.hasNext(OUTLINE)){
            SectionParseScene.SCENE_1.parse(pointer, children);
        }
    }

    @Override
    public SectionParseHead nextParser(){
        return values()[ordinal() + 1];
    }
    @Override
    public SectionSpan buildSpan(ArrayList<Span> children){
        argumentNotEmpty(children, "children");
        return new SectionSpanHead(children, this);
    }
}
