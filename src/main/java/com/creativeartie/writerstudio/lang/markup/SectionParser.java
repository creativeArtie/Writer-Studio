package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Template for {@link SectionParseHead} and {@link SectionParseScene}.
 *
 * This includes the implementation of {@code design/ebnf.txt SectionContent}.
 */
interface SectionParser extends SetupParser {
    /** List of line parsers for each sections.
     *
     * @see #parseContent(SetupPointer, ArrayList)
     * @see AuxiliaryData#getSectionParser() how it is produced.
     */
    public static final List<SetupParser> SECTION_PARSERS = getSectionParsers();

    /** parses the content
     *
     * @param pointer
     *      setup pointer
     * @param children
     *      span children
     */
    static void parseContent(SetupPointer pointer, ArrayList<Span> children){
        argumentNotNull(children, "children");
        argumentNotNull(pointer, "pointer");

        while (! pointer.hasNext(LEVEL_HEADINGS) && pointer.hasNext()){
            /// pointer next is not heading/outline and has text
            if (! NoteCardParser.PARSER.parse(pointer, children)){
                /// Not a note
                for (SetupParser parser: SECTION_PARSERS){
                    if (parser.parse(pointer, children)){
                        /// is line of some sort
                        break;
                    }
                }

            }
        }
    }

    static boolean isCurrentLevel(SetupPointer pointer, LinedParseLevel type,
        int level
    ){
        return pointer.hasNext(LEVEL_STARTERS.get(type).get(level)) &&
            ! isChild(pointer, type, level);
    }

    static boolean isChild(SetupPointer pointer, LinedParseLevel type,
        int level
    ){
        if (level + 1 == LEVEL_MAX) {
            return false;
        }
        return pointer.hasNext(LEVEL_STARTERS.get(type).get(level + 1));
    }

    public default void parseChildren(SetupPointer pointer,
        ArrayList<Span> children
    ){
        Optional<SpanBranch> child = parseChild(pointer);
        boolean found = false;
        while (child.isPresent()){
            children.add(child.get());
            child = parseChild(pointer);
        }
    }

    public Optional<SpanBranch> parseChild(SetupPointer pointer);

}
