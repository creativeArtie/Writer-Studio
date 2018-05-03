package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/** A text with formats. */
public abstract class FormatSpan extends SpanBranch {

    private final boolean[] spanFormats;
    private final CacheKeyList<StyleInfo> cacheStyles;

    /** Creates an instance of {@linkplain FormatSpan}.
     *
     * @param children
     *      span children
     * @param formats
     *      format list
     */
    FormatSpan(List<Span> children, boolean[] formats){
        super(children);
        checkEqual(formats.length, "formats.length", FORMAT_TYPES);
        spanFormats = Arrays.copyOf(formats, formats.length);

        cacheStyles = new CacheKeyList<>(StyleInfo.class);
    }

    /** Checks if text has the fromat type.
     *
     * @param type
     *      format type
     * @return answer
     */
    public final boolean isFormat(FormatType type){
        return spanFormats[type.ordinal()];
    }

    /** Check if text is bold.
     *
     * @return answer
     */
    public final boolean isBold(){
        return spanFormats[FormatType.BOLD.ordinal()];
    }

    /** Check if text is itlalics.
     *
     * @return answer
     */
    public final boolean isItalics(){
        return spanFormats[FormatType.ITALICS.ordinal()];
    }

    /** Check if text is underlined.
     *
     * @return answer
     */
    public final boolean isUnderline(){
        return spanFormats[FormatType.UNDERLINE.ordinal()];
    }

    /** Check if text is coded.
     *
     * @return answer
     */
    public final boolean isCoded(){
        return spanFormats[FormatType.CODED.ordinal()];
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () -> {
            ImmutableList.Builder<StyleInfo> list = ImmutableList.builder();
            int i = 0;
            FormatType[] values = FormatType.values();
            for (boolean format: spanFormats){
                if (format){
                    list.add(values[i]);
                }
                i++;
            }
            return list.build();
        });
    }

    @Override
    public final String toString(){
        StringBuilder ans = new StringBuilder();
        if (spanFormats[FormatType.BOLD.ordinal()]) ans.append("b");
        if (spanFormats[FormatType.ITALICS.ordinal()]) ans.append("i");
        if (spanFormats[FormatType.UNDERLINE.ordinal()]) ans.append("u");
        if (spanFormats[FormatType.CODED.ordinal()]) ans.append("c");
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
