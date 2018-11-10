package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** Template for {@link FormatParsePointId} and {@link FormatParsePointKey}. */
abstract class FormatParsePoint implements SetupParser {

    private final String spanStart;
    private final boolean[] formatList;

    /** Creates a {@linkplain FormatParsePoint}.
     *
     * @param start
     *      start token
     * @param formats
     *      format lists
     */
    protected FormatParsePoint(String start, boolean[] formats){
        spanStart = argumentNotNull(start, "start");

        indexEquals(formats.length, "format.length", FORMAT_TYPES);
        formatList = formats;
    }

    /** Check if the text can be parsed.
     *
     * @param text
     *      new text
     * @return answer
     * @see FormatSpanPointId#getParser(String)
     * @see FormatSpanPointKey#getParser(String)
     */
    final boolean canParse(String text){
        argumentNotNull(text, "text");
        return text.startsWith(spanStart) &&
            AuxiliaryChecker.willEndWith(text, CURLY_END);
    }

    /** Gets the list of formats.
     *
     * @return answer
     * @see FormatSpanPointId#FormatSpanPointId(List, FormatParsePointId)
     * @see FormatSpanPointKey#FormatSpanPointKey(List, FormatParsePointKey)
     */
    final boolean[] getFormats(){
        return formatList;
    }

    @Override
    public final Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();

        if(pointer.startsWith(children, spanStart)){

            parseContent(pointer, children);


            pointer.startsWith(children, CURLY_END);

            return Optional.of(buildSpan(children));
        }
        return Optional.empty();
    }

    /** Parse the span content.
     *
     * @param pointer
     *      setup pointer
     * @param children
     *      span children
     * @see #parse(SetupPointer)
     */
    abstract void parseContent(SetupPointer pointer, ArrayList<Span> children);

    /** Creates the Span after the parsing complete.
     *
     * @param children
     *    span children
     * @return answer
     * @see #parse(SetupPointer)
     */
    abstract SpanBranch buildSpan(ArrayList<Span> children);
}
