package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/**
 * Parser for {@code MainSpan*} classes. See {@code design/SectionParser.txt}
 * for details
 *
 */
interface SectionParser extends SetupParser {

    static final String[] HEAD_STARTERS = SetupParser.combine(
        getLevelTokens(LinedParseLevel.HEADING),
        getLevelTokens(LinedParseLevel.OUTLINE));

    static void parseContent(ArrayList<Span> children,
            SetupPointer pointer){
        checkNotNull(children, "children");
        checkNotNull(pointer, "pointer");
        while (! pointer.hasNext(HEAD_STARTERS) && pointer.hasNext()){
            /// pointer next is not heading/outline and has text
            if (! NoteCardParser.PARSER.parse(children, pointer)){
                /// Not a note
                for (SetupParser parser: SECTION_PARSERS){
                    if (parser.parse(children, pointer)){
                        /// is line of some sort
                        break;
                    }
                }

            }
        }
    }

    public default boolean hasChild(SetupPointer pointer, SectionParser current){
        checkNotNull(pointer, "pointer");
        checkNotNull(current, "current");
        boolean checking = false;
        for (SectionParser parser: getParsers()){
            if (! pointer.hasNext(parser.getStarter())){
                /// not a child at this level
                return false;
            }
            if (checking){
                /// find a child
                return true;
            } else if (parser == current){
                /// from this point on this is a child
                checking = true;
            }
        }
        return false;
    }

    @Override
    public default Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        boolean isReady = true;
        if (pointer.hasNext(getStarter())){
            /// has an heading/outline
            if (! isLast() && ! pointer.hasNext(getNext().getStarter())){
                /// parse heading/outline line
                getHeadLineParser().parse(children, pointer);
                /// parse the content
                parseContent(children, pointer);
            }
        } else {
            isReady = false;
        }

        /// parse subsection and subscenes
        headParsing(children, pointer, isReady);

        return Optional.ofNullable(children.isEmpty()? null:
            create(children));
    }

    /** Is {@code oridnal() == 0}. */
    public boolean isFirst();

    /** Is {@code oridnal() == values().length}. */
    public boolean isLast();

    /** Returns {@code values()[ordinal() + 1]}*/
    public SectionParser getNext();

    /** Creates either a {@link SectionSpanHead} or {@link SectionSpanScene}. */
    public SectionSpan create(ArrayList<Span> children);

    /** Returns {@code values()}*/
    public SectionParser[] getParsers();

    /**
     * Returns {@link LinedParseLevel.HEADING} or
     * {@link LinedParseLevel.OUTLINE}
     */
    public LinedParseLevel getHeadLineParser();

    /** Extra steps for parsing {@link SectionSpanHead}. */
    public void headParsing(ArrayList<Span> children,
        SetupPointer pointer, boolean findHead);

    /** Gets the line starter, to see if line is a heading or outline. */
    public String getStarter();
}