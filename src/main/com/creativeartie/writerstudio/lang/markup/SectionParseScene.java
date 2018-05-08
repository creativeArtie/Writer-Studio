package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Implements {@code design/ebnf.txt SectionScene}. */
enum SectionParseScene implements SectionParser {
    /** Top most section. */
    SCENE_1,
    /** Scene 2. */
    SCENE_2,
    /** Scene 3. */
    SCENE_3,
    /** Scene 4. */
    SCENE_4,
    /** Scene 5. */
    SCENE_5,
    /** Scene 6. */
    SCENE_6;

    private final String lineStarter;

    /** Creates a {@line SectionParseScene}. */
    private SectionParseScene(){
        lineStarter = LEVEL_STARTERS.get(LinedParseLevel.OUTLINE).get(ordinal());
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
        return LinedParseLevel.OUTLINE;
    }

    @Override
    public SectionParseScene nextParser(){
        return values()[ordinal() + 1];
    }

    @Override
    public SectionSpan buildSpan(ArrayList<Span> children){
        return new SectionSpanScene(children, this);
    }
}
