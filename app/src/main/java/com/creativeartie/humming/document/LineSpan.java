package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.BasicLinePatterns.*;

public abstract class LineSpan extends SpanBranch {
    static LineSpan newLine(SpanBranch parent, String text) {
        LineSpan returns = null;
        Matcher match;

        if ((match = BasicLinePatterns.AGENDA.matcher(text)) != null) returns = new AgendaLine(parent);

        else if ((match = BasicLinePatterns.QUOTE.matcher(text)) != null)
            returns = new LineSpan(parent, LineStyles.QUOTE) {
                @Override
                protected void buildSpan(Matcher match) {
                    add(new SpanLeaf(this, BasicLinePart.QUOTER.group(match)));
                    addText(match, BasicLinePart.FORMATTED);
                    addLineEnd(match, BasicLinePart.ENDER);
                }
            };

        else if ((match = BasicLinePatterns.BREAK.matcher(text)) != null)
            returns = new LineSpan(parent, LineStyles.BREAK) {
                @Override
                protected void buildSpan(Matcher match) {
                    add(new SpanLeaf(this, BasicLinePart.BREAKER.group(match)));
                    addLineEnd(match, BasicLinePart.ENDER);
                }
            };

        else if ((match = BasicLinePatterns.TEXT.matcher(text)) != null)
            returns = new LineSpan(parent, LineStyles.NORMAL) {
                @Override
                protected void buildSpan(Matcher match) {
                    addText(match, BasicLinePart.FORMATTED);
                    addLineEnd(match, BasicLinePart.ENDER);
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
