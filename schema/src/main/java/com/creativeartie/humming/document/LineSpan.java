package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.LineSpanPatterns.*;

public abstract class LineSpan extends SpanBranch {
    static LineSpan newLine(SpanBranch parent, String text) {
        LineSpan returns = null;
        Matcher match;
        // TODO add table rows and heading
        if ((match = NoteLinePatterns.SUMMARY.matcher(text)) != null) returns = new NoteSummaryLine(parent);
        else if ((match = NoteLinePatterns.NOTE.matcher(text)) != null) returns = new NoteDetailLine(parent);
        else if ((match = NoteLinePatterns.FIELD.matcher(text)) != null) returns = new NoteFieldLine(parent);
        else if ((match = ReferenceLinePatterns.ENDNOTE.matcher(text)) != null)
            returns = ReferenceLine.newEndnote(parent);
        else if ((match = ReferenceLinePatterns.FOOTNOTE.matcher(text)) != null)
            returns = ReferenceLine.newFootnote(parent);

        else if ((match = ListLinePattern.matcher(text)) != null) returns = ListLine.newLine(parent, match);

        else if ((match = HeadingLinePattern.matcher(text)) != null) returns = HeadingLine.newLine(parent, match);

        else if ((match = LineSpanPatterns.AGENDA.matcher(text)) != null) returns = new AgendaLine(parent);

        else if ((match = LineSpanPatterns.QUOTE.matcher(text)) != null)
            returns = new LineSpan(parent, LineStyles.QUOTE) {
                @Override
                protected void buildSpan(Matcher match) {
                    add(new SpanLeaf(this, LineSpanParts.QUOTER.group(match)));
                    addText(match, LineSpanParts.FORMATTED);
                    addLineEnd(match, LineSpanParts.ENDER);
                }
            };

        else if ((match = LineSpanPatterns.BREAK.matcher(text)) != null)
            returns = new LineSpan(parent, LineStyles.BREAK) {
                @Override
                protected void buildSpan(Matcher match) {
                    add(new SpanLeaf(this, LineSpanParts.BREAKER.group(match)));
                    addLineEnd(match, LineSpanParts.ENDER);
                }
            };

        else if ((match = LineSpanPatterns.TEXT.matcher(text)) != null)
            returns = new LineSpan(parent, LineStyles.NORMAL) {
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
            add(LineText.newBasicText(this, raw));
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

    private LineStyles lineStyle;

    protected LineSpan(SpanBranch parent, LineStyles style) {
        super(parent, style);
        lineStyle = style;
    }

    protected abstract void buildSpan(Matcher match);

    public LineStyles getLineStyle() {
        return lineStyle;
    }
}
