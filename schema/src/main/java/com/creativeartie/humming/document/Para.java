package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.ParaBasicPatterns.*;

/**
 * A single paragraph ending with {@code \n}.
 */
public abstract class Para extends SpanBranch {
    static Para newLine(SpanBranch parent, String text) {
        Para returns = null;
        Matcher match;
        if ((match = ParaNotePatterns.SUMMARY.matcher(text)) != null) returns = new ParaNoteHead(parent);
        else if ((match = ParaNotePatterns.FIELD.matcher(text)) != null) returns = new ParaNoteField(parent);
        else if ((match = ParaNotePatterns.NOTE.matcher(text)) != null) returns = new ParaNoteDetail(parent);

        else if ((match = ParaReferencePatterns.FOOTNOTE.matcher(text)) != null)
            returns = ParaReference.newFootnote(parent);
        else if ((match = ParaReferencePatterns.IMAGE.matcher(text)) != null) returns = ParaReference.newImage(parent);

        else if ((match = ParaListPattern.matcher(text)) != null) returns = ParaList.newLine(parent, match);

        else if ((match = ParaHeadingPattern.matcher(text)) != null) returns = ParaHeading.newLine(parent, match);

        else if ((match = ParaBasicPatterns.AGENDA.matcher(text)) != null) returns = new ParaAgenda(parent);

        else if ((match = ParaTableRowPattern.matcher(text)) != null) returns = ParaTableRow.newLine(parent);

        else if ((match = ParaBasicPatterns.QUOTE.matcher(text)) != null)
            returns = new Para(parent, CssLineStyles.QUOTE) {
                private Optional<TextFormatted> formatted = null;

                @Override
                protected void buildSpan(Matcher match) {
                    SpanLeaf.addLeaf(this, LineSpanParts.QUOTER.group(match));
                    formatted = addText(match, LineSpanParts.FORMATTED);
                    addLineEnd(match, LineSpanParts.ENDER);
                }

                @Override
                public int getWrittenCount() {
                    return getWritten(formatted);
                }

                @Override
                public int getOutlineCount() {
                    return getOutline(formatted);
                }
            };

        else if ((match = ParaBasicPatterns.BREAK.matcher(text)) != null)
            returns = new Para(parent, CssLineStyles.BREAK) {
                @Override
                protected void buildSpan(Matcher match) {
                    SpanLeaf.addLeaf(this, LineSpanParts.BREAKER.group(match));
                    addLineEnd(match, LineSpanParts.ENDER);
                }

                @Override
                public int getWrittenCount() {
                    return 0;
                }

                @Override
                public int getOutlineCount() {
                    return 0;
                }
            };
        else if ((match = ParaBasicPatterns.TEXT.matcher(text)) != null)
            returns = new Para(parent, CssLineStyles.NORMAL) {
                private Optional<TextFormatted> formatted = null;

                @Override
                protected void buildSpan(Matcher match) {
                    formatted = addText(match, LineSpanParts.FORMATTED);
                    addLineEnd(match, LineSpanParts.ENDER);
                }

                @Override
                public int getOutlineCount() {
                    return getOutline(formatted);
                }

                @Override
                public int getWrittenCount() {
                    return getWritten(formatted);
                }
            };
        else {
            throw new IllegalStateException("Line type not found."); //$NON-NLS-1$
        }

        returns.buildSpan(match);
        return returns;
    }

    static int getOutline(Optional<TextFormatted> formatted) {
        return formatted.map((text) -> text.getOutlineCount()).orElse(0);
    }

    static int getWritten(Optional<TextFormatted> formatted) {
        return formatted.map((text) -> text.getWrittenCount()).orElse(0);
    }

    Optional<TextFormatted> addText(Matcher match, PatternEnum textPattern) {
        String raw;
        TextFormatted formatted = null;

        if ((raw = textPattern.group(match)) != null) {
            formatted = TextFormatted.newBasicText(this, raw);
            add(formatted);
        }
        return Optional.ofNullable(formatted);
    }

    void addLineEnd(Matcher match, PatternEnum endPattern) {
        String raw;

        if ((raw = endPattern.group(match)) != null) {
            SpanLeaf.addLeaf(this, raw);
        }
    }

    private CssLineStyles lineStyle;

    Para(SpanBranch parent, CssLineStyles style) {
        super(parent, style);
        lineStyle = style;
    }

    /**
     * Builds the paragraph
     *
     * @param match
     */
    protected abstract void buildSpan(Matcher match);

    /**
     * Get the line style
     *
     * @return line style.
     */
    public CssLineStyles getLineStyle() {
        return lineStyle;
    }

    abstract int getOutlineCount();

    abstract int getWrittenCount();
}
