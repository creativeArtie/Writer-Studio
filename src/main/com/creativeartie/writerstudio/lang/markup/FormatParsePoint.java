package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

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
     * @see FormatParsePointId#FormatParsePointId(DirectoryType, start, formats)
     * @see FormatParsePointKey#FormatParsePointKey(formats)
     */
    protected FormatParsePoint(String start, boolean[] formats){
        spanStart = argumentNotNull(start, "start");
        formatList = indexEqual(formats.length, "format", FORMAT_TYPES);
    }

    /** Gets the list of formats.
     *
     * @return answer
     * @see FormatSpanPointId#FormatSpanPointId(List, FormatParsePointId)
     * @see FormatSpanPointKey#FormatSpanPointKey(List, FormatParsePointKey)
     */
    boolean[] getFormats(){
        return formatList;
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");
        ArrayList<Span> children = new ArrayList<>();
        if(pointer.startsWith(children, spanStart)){

            parseContent(pointer, children);


            pointer.startsWith(children, CURLY_END);

            return Optional.of(buildSpan(pointer, children));
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
    abstract SpanBranch buildSpan(SetupPointer pointer,
        ArrayList<Span> children);

    boolean canParse(String text){
        argumentNotNull(text, "text");
        return text.startsWith(spanStart) &&
            AuxiliaryChecker.willEndWith(text, CURLY_END);
    }
}
