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
        checkNotNull(children, "children");
        checkNotNull(pointer, "pointer");
        while (! pointer.hasNext(HEAD_STARTERS) && pointer.hasNext()){
            if (! NoteCardParser.PARSER.parse(children, pointer)){
                for (SetupParser parser: SECTION_PARSERS){
                    if (parser.parse(children, pointer)){
                        break;
                    }
                }

            }
        }
    }

    public default boolean hasChild(SetupPointer pointer, SectionParser current){
        checkNotNull(pointer, "pointer");
        checkNotNull(current, "current");
        System.out.println(pointer);
        boolean checking = false;
        for (SectionParser parser: getParsers()){
            System.out.print(parser + ": ");
            if (parser == current){
                System.out.println("set");
                checking = true;
                continue;
            }
            if (checking && pointer.hasNext(parser.getStarter())){
                System.out.println("done");
                return true;
            }
            System.out.println(checking);
        }
        return false;
    }

    @Override
    public default Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.hasNext(getStarter())){
            if (isLast() && ! pointer.hasNext(getNext().getStarter())){
                LinedParseLevel.HEADING.parse(children, pointer);
                parseContent(children, pointer);
            }
        }

        System.out.println(this);
        if (! isLast()){
            if (hasChild(pointer, this)){
                getNext().parse(children, pointer);
            }
        }
        return Optional.ofNullable(children.isEmpty()? null:
            create(children));
    }

    public boolean isFirst();

    public boolean isLast();

    public SectionParser getNext();

    public SectionSpan create(ArrayList<Span> children);

    public SectionParser[] getParsers();

    public default void headParsing(ArrayList<Span> children,
        SetupPointer pointer, boolean findHead){}

    public String getStarter();
}
