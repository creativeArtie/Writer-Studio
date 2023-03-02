package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ParaList extends Para implements SpanList {
    static ParaList newLine(SpanBranch parent, Matcher match) {
        if (ParaListPattern.BULLET.group(match) == null) {
            return new ParaList(parent, StyleLines.NUMBERED);
        }
        return new ParaList(parent, StyleLines.BULLET);
    }

    private int listLevel;
    private int listPosition;

    private ParaList(SpanBranch parent, StyleLines style) {
        super(parent, style);
        listLevel = 0;
        listPosition = 1;
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw = ParaListPattern.BULLET.group(match);
        if (raw == null) {
            raw = ParaListPattern.NUMBERED.group(match);
        }
        add(new SpanLeaf(this, raw));
        listLevel = raw.length();
        addText(match, ParaListPattern.TEXT);
        addLineEnd(match, ParaListPattern.ENDER);
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
        return useParent(DivisionList.class).isBullet();
    }
}
