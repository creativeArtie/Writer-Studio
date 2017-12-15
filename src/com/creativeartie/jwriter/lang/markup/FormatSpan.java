package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

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

    boolean[] getFormats(){
        return spanFormats;
    }

    public boolean isFormat(FormatType type){
        return spanFormats[type.ordinal()];
    }

    public boolean isBold(){
        return spanFormats[FormatType.BOLD.ordinal()];
    }

    public boolean isItalics(){
        return spanFormats[FormatType.ITALICS.ordinal()];
    }

    public boolean isUnderline(){
        return spanFormats[FormatType.UNDERLINE.ordinal()];
    }

    public boolean isCoded(){
        return spanFormats[FormatType.CODED.ordinal()];
    }

    @Override
    public String toString(){
        StringBuilder ans = new StringBuilder();
        if (spanFormats[0]) ans.append("b");
        if (spanFormats[1]) ans.append("i");
        if (spanFormats[2]) ans.append("u");
        if (spanFormats[3]) ans.append("c");
        return ans.toString() + super.toString();
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
}
