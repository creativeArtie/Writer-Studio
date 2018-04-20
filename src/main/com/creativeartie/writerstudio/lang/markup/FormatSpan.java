package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // (many)

import com.google.common.collect.*; // ImmutableList

import com.creativeartie.writerstudio.lang.*; // Span, SpanBranch, StyleInfo

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * {@link Span} for several formatted {@code FormatSpan*} classes.
 */
public abstract class FormatSpan extends SpanBranch {
    private final boolean[] spanFormats;
    private Optional<List<StyleInfo>> cacheStyles;
    FormatSpan(List<Span> spanChildren, boolean[] formats){
        super(spanChildren);
        checkEqual(formats.length, "formats.length", FORMAT_TYPES);
        spanFormats = Arrays.copyOf(formats, formats.length);
    }

    FormatSpan(){
        this(new ArrayList<>(), new boolean[FORMAT_TYPES]);
    }

    final boolean[] getFormats(){
        return spanFormats;
    }

    public final boolean isFormat(FormatType type){
        return spanFormats[type.ordinal()];
    }

    public final boolean isBold(){
        return spanFormats[FormatType.BOLD.ordinal()];
    }

    public final boolean isItalics(){
        return spanFormats[FormatType.ITALICS.ordinal()];
    }

    public final boolean isUnderline(){
        return spanFormats[FormatType.UNDERLINE.ordinal()];
    }

    public final boolean isCoded(){
        return spanFormats[FormatType.CODED.ordinal()];
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () -> {
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
        return cacheStyles.get();
    }

    @Override
    protected void childEdited(){
        cacheStyles = Optional.empty();
    }

    /** Gets the parsed text for display. */
    public abstract String getOutput();


    @Override
    public String toString(){
        StringBuilder ans = new StringBuilder();
        if (spanFormats[0]) ans.append("b");
        if (spanFormats[1]) ans.append("i");
        if (spanFormats[2]) ans.append("u");
        if (spanFormats[3]) ans.append("c");
        return ans.toString() + "(" + toChildString() + ")";
    }

    protected abstract String toChildString();
}
