package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** Template for {@link FormatParseLinkDirect} and {@link FormatParseLinkRef}. */
abstract class FormatParseLink implements SetupParser {

    /** Create a list {@linkplain FormatParseLink}.
     *
     * @param formats
     *      style formats
     * @return answer
     * @see FormattedParser#parse(SetupPointer)
     */
    static FormatParseLink[] getParsers(boolean[] formats){

        boolean[] setup = Arrays.copyOf(formats, formats.length);
        return new FormatParseLink[]{
            new FormatParseLinkRef(setup),
            new FormatParseLinkDirect(setup)
        };
    }

    private final String spanStart;
    private final boolean[] formatList;


    /** Creates a {@linkplain FormatParseLink}.
     *
     * @param start
     *      start token
     * @param formats
     *      format lists
     * @see #getParsers(boolean[])
     */
    protected FormatParseLink(String start, boolean[] formats){
        spanStart = argumentNotNull(start, "starts");
        argumentNotNull(formats, "formats");
        indexEquals(formats.length, "formats.length", FORMAT_TYPES);
        formatList = formats;
    }

    /** Gets the list of formats.
     *
     * @return answer
     * @see FromatSpanLinkDirect#FromatSpanLinkDirect(List, FormatParseLinkDirect)
     * @see FromatSpanLinkRef#FromatSpanLinkRef(List, FormatParseLinkRef)
     */
    final boolean[] getFormats(){
        return formatList;
    }

    @Override
    public final Optional<SpanBranch> parse(SetupPointer pointer){
        argumentNotNull(pointer, "pointer");

        ArrayList<Span> children = new ArrayList<>();
        if(pointer.startsWith(children, spanStart)){
            return Optional.of(parseSpan(pointer, children));
        }
        return Optional.empty();
    }

    /** Parse a span when it is known.
     *
     * @param pointer
     *      setup pointer
     * @param children
     *      span children
     * @return answer
     * @see #parse(SetupPointer)
     */
    abstract SpanBranch parseSpan(SetupPointer pointer,
        ArrayList<Span> children);

    /** Parse the rest, excluding URL path or reference id.
     *
     * @param pointer
     *      setup pointer
     * @param children
     *      span children
     * @see FormatParseLinkDirect#parseSpan(SetupPointer, ArrayList)
     * @see FormatParseLinkRef#parseSpan(SetupPointer, ArrayList)
     */
    protected final void parseRest(SetupPointer pointer,
            ArrayList<Span> children){
        argumentNotNull(pointer, "pointer");
        argumentNotNull(children, "children");

        /// Create display text if any
        if (pointer.startsWith(children, LINK_TEXT)){
            /// Add the text itself
            CONTENT_LINK.parse(pointer, children);
        }

        /// Add the ">"
        pointer.startsWith(children, LINK_END);
    }
}
