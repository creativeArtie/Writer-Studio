package com.creativeartie.writer.lang.markup;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Implements {@code design/ebnf.txt FormatNote} and the rule it directly uses.
 *
 * The rules it direct uses are {@code FormatResearch}, {@code FormatFootnote},
 * and {@code FormatEndnote}.
 */
final class FormatParsePointId extends FormatParsePoint {

    private final DirectoryType spanType;
    private final DirectoryParser idParser;

    /** Creates a list of {@linkplain FormatParsePointId}.
     *
     * @param formats
     *      format list
     * @see FormattedParser#parse(SetupPointer)
     */
    static FormatParsePointId[] getParsers(boolean[] formats){
        argumentNotNull(formats, "formats");
        indexEquals(formats.length, "formats.length", FORMAT_TYPES);

        boolean[] setup = Arrays.copyOf(formats, formats.length);
        return new FormatParsePointId[]{
            new FormatParsePointId(DirectoryType.RESEARCH, CURLY_CITE, setup),
            new FormatParsePointId(DirectoryType.FOOTNOTE, CURLY_FOOTNOTE, setup),
            new FormatParsePointId(DirectoryType.ENDNOTE, CURLY_ENDNOTE, setup),
        };
    }

    /** Creates a {@linkplain FormatParsePointId}.
     *
     * @param type
     *      directory type
     * @param start
     *      start token
     * @param formats
     *      format list
     * @see #getParsers(boolean [])
     */
    private FormatParsePointId(DirectoryType type, String start,
            boolean[] formats){
        super(start, formats);
        assert type != null: "Null type.";
        spanType = type;
        idParser = DirectoryParser.getRefParser(type);
    }

    /** Gets the directory type.
     *
     * @return answer
     * @see FormatSpanPointId#FormatSpanPointId(List)
     */
    DirectoryType getDirectoryType(){
        return spanType;
    }

    @Override
    void parseContent(SetupPointer pointer, ArrayList<Span> children){
        argumentNotNull(pointer, "pointer");
        argumentNotNull(children, "children");
        idParser.parse(pointer, children);
    }

    @Override
    SpanBranch buildSpan(ArrayList<Span> children){
        return new FormatSpanPointId(children, getFormats(), this);
    }

}