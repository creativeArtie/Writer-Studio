package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ListLine extends LineSpan implements ListSpan {
    static ListLine newLine(SpanBranch parent, Matcher match) {
        if (ListLinePattern.BULLET.group(match) == null) {
            return new ListLine(parent, LineStyles.NUMBERED);
        }
        return new ListLine(parent, LineStyles.BULLET);
    }

    private int listLevel;
    private int listPosition;

    private ListLine(SpanBranch parent, LineStyles style) {
        super(parent, style);
        listLevel = 0;
        listPosition = 1;
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw = ListLinePattern.BULLET.group(match);
        if (raw == null) {
            raw = ListLinePattern.NUMBERED.group(match);
        }
        add(new SpanLeaf(this, raw));
        listLevel = raw.length();
        addText(match, ListLinePattern.TEXT);
        addLineEnd(match, ListLinePattern.ENDER);
    }

    @Override
    public int getLevel() {
        return listLevel;
    }

    void setPosition(int pos) {
        listPosition = pos;
    }

    @Override
    public int getPosition() {
        return listPosition;
    }

    @Override
    public boolean isBullet() {
        return useParent(ListDivision.class).isBullet();
    }
}
