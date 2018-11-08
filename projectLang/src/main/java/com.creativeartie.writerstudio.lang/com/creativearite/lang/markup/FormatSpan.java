package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A text with formats. */
public abstract class FormatSpan extends SpanBranch {

    private final boolean[] spanFormats;

    /** Creates an instance of {@linkplain FormatSpan}.
     *
     * @param children
     *      span children
     * @param formats
     *      format list
     */
    FormatSpan(List<Span> children, boolean[] formats){
        super(children);
        indexEquals(formats.length, "formats.length", FORMAT_TYPES);
        spanFormats = Arrays.copyOf(formats, formats.length);
    }

    /** Checks if text has the fromat type.
     *
     * @param type
     *      format type
     * @return answer
     */
    public final boolean isFormat(FormatTypeStyle type){
        return spanFormats[type.ordinal()];
    }

    /** Check if text is bold.
     *
     * @return answer
     */
    public final boolean isBold(){
        return spanFormats[FormatTypeStyle.BOLD.ordinal()];
    }

    /** Check if text is itlalics.
     *
     * @return answer
     */
    public final boolean isItalics(){
        return spanFormats[FormatTypeStyle.ITALICS.ordinal()];
    }

    /** Check if text is underlined.
     *
     * @return answer
     */
    public final boolean isUnderline(){
        return spanFormats[FormatTypeStyle.UNDERLINE.ordinal()];
    }

    /** Check if text is coded.
     *
     * @return answer
     */
    public final boolean isCoded(){
        return spanFormats[FormatTypeStyle.CODED.ordinal()];
    }

    @Override
    public final String toString(){
        StringBuilder ans = new StringBuilder();
        if (spanFormats[FormatTypeStyle.BOLD.ordinal()]) ans.append("b");
        if (spanFormats[FormatTypeStyle.ITALICS.ordinal()]) ans.append("i");
        if (spanFormats[FormatTypeStyle.UNDERLINE.ordinal()]) ans.append("u");
        if (spanFormats[FormatTypeStyle.CODED.ordinal()]) ans.append("c");
        return toChildName() + "(" + ans.toString() + toChildString() + ")";
    }

    /** Gets the child class name.
     *
     * @return answer
     * @see #toString()
     */
    protected abstract String toChildName();

    /** Gets the child content text.
     *
     * @return answer
     * @see #toString()
     */
    protected abstract String toChildString();
}
