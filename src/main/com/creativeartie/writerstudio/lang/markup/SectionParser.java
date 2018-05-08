package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.main.Checker.*;

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

    @Override
    public default Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        /// parse section content
        if (pointer.hasNext(getStarter())){
            /// at least == current
            boolean child = getNextStarter().map(s -> pointer.hasNext(s))
                .orElse(false);
            if (! child){
                /// parse heading + content
                getHeadParser().parse(pointer, children);
                parseContent(pointer, children);
            }
        } else if (this == SectionParseHead.SECTION_1){
            if (! pointer.hasNext(LEVEL_STARTERS.get(LinedParseLevel.OUTLINE)
                    .get(0))){
                /// content only (section 1 with heading done above)
                parseContent(pointer, children);
            }
        }

        /// parse section scenes, (head only)
        if (this instanceof SectionParseHead){
            SectionParseHead.parseOutline(pointer, children);
        }

        /// parse sub section heads or secnes
        if (getNextStarter().map(s -> pointer.hasNext(s)).orElse(false)){
            nextParser().parse(pointer, children);
        }

        return Optional.ofNullable(children.isEmpty()? null:
            buildSpan(children));
    }

    /** Get the starter token.
     *
     * Use for checking if the heading is at least this level or a child
     *
     * @return answer
     * @see parse(SetupPointer)
     */
    public String getStarter();

    /** Get the next starter token.
     *
     * Use for checking if the heading is a child.
     *
     * @return answer
     * @see parse(SetupPointer)
     */
    public Optional<String> getNextStarter();

    /** Gets the heading parser.
     *
     * @return answer
     * @see parse(SetupPointer)
     */
    public LinedParseLevel getHeadParser();

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

    /** Gets the next parser.
     *
     * Can return {@code null} if
     * {@link #getNextPointer() getNextPointer().isPresent())} returns
     * {@code false}.
     *
     * @return answer
     */
    public SectionParser nextParser();

    /** Creates a new {@link SectionSpan}.
     *
     * @param children
     *      span children
     * @return answer
     */
    public SectionSpan buildSpan(ArrayList<Span> children);

}
