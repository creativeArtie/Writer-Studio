package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ListLine extends LineSpan {
    static ListLine newLine(SpanBranch parent, Matcher match) {
        if (ListLinePattern.BULLET.group(match) == null) {
            return new ListLine(parent, LineStyles.NUMBERED);
        }
        return new ListLine(parent, LineStyles.BULLET);
    }

    private int nestLevel;

    private ListLine(SpanBranch parent, LineStyles style) {
        super(parent, style);
        nestLevel = 0;
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw = ListLinePattern.BULLET.group(match);
        if (raw == null) {
            raw = ListLinePattern.NUMBERED.group(match);
        }
        add(new SpanLeaf(this, raw));
        nestLevel = raw.length();
        addText(match, ListLinePattern.TEXT);
        addLineEnd(match, ListLinePattern.ENDER);
    }

    public int getLevel() {
        return nestLevel;
    }
}
