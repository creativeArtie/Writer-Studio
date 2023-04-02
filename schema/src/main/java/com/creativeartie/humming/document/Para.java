package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.ParaBasicPatterns.*;

public abstract class Para extends SpanBranch {
    static Para newLine(SpanBranch parent, String text) {
        Para returns = null;
        Matcher match;
        if ((match = ParaNotePatterns.SUMMARY.matcher(text)) != null) returns = new ParaNoteHead(parent);
        else if ((match = ParaNotePatterns.FIELD.matcher(text)) != null) returns = new ParaNoteField(parent);
        else if ((match = ParaNotePatterns.NOTE.matcher(text)) != null) returns = new ParaNoteDetail(parent);

        else if ((match = ParaReferencePatterns.ENDNOTE.matcher(text)) != null)
            returns = ParaReference.newEndnote(parent);
        else if ((match = ParaReferencePatterns.FOOTNOTE.matcher(text)) != null)
            returns = ParaReference.newFootnote(parent);
        else if ((match = ParaReferencePatterns.IMAGE.matcher(text)) != null) returns = ParaReference.newImage(parent);

        else if ((match = ParaListPattern.matcher(text)) != null) returns = ParaList.newLine(parent, match);

        else if ((match = ParaHeadingPattern.matcher(text)) != null) returns = ParaHeading.newLine(parent, match);

        else if ((match = ParaBasicPatterns.AGENDA.matcher(text)) != null) returns = new ParaAgenda(parent);

        else if ((match = ParaTableRowPattern.matcher(text)) != null) returns = ParaTableRow.newLine(parent);

        else if ((match = ParaBasicPatterns.QUOTE.matcher(text)) != null) returns = new Para(parent, StyleLines.QUOTE) {
            @Override
            protected void buildSpan(Matcher match) {
                add(new SpanLeaf(this, LineSpanParts.QUOTER.group(match)));
                addText(match, LineSpanParts.FORMATTED);
                addLineEnd(match, LineSpanParts.ENDER);
            }
        };

        else if ((match = ParaBasicPatterns.BREAK.matcher(text)) != null) returns = new Para(parent, StyleLines.BREAK) {
            @Override
            protected void buildSpan(Matcher match) {
                add(new SpanLeaf(this, LineSpanParts.BREAKER.group(match)));
                addLineEnd(match, LineSpanParts.ENDER);
            }
        };
        else if ((match = ParaBasicPatterns.TEXT.matcher(text)) != null) returns = new Para(parent, StyleLines.NORMAL) {
            @Override
            protected void buildSpan(Matcher match) {
                addText(match, LineSpanParts.FORMATTED);
                addLineEnd(match, LineSpanParts.ENDER);
            }
        };
        else return null;

        returns.buildSpan(match);
        return returns;
    }

    protected void addText(Matcher match, PatternEnum textPattern) {
        String raw;

        if ((raw = textPattern.group(match)) != null) {
            add(TextFormatted.newBasicText(this, raw));
        }
    }

    protected void addLineEnd(Matcher match, PatternEnum endPattern) {
        String raw;

        if ((raw = endPattern.group(match)) != null) {

            if (!raw.isEmpty()) {
                add(new SpanLeaf(this, raw));
            }
        }
    }

    private StyleLines lineStyle;

    protected Para(SpanBranch parent, StyleLines style) {
        super(parent, style);
        lineStyle = style;
    }

    protected abstract void buildSpan(Matcher match);

    public StyleLines getLineStyle() {
        return lineStyle;
    }
}
