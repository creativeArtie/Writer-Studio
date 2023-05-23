package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

/**
 * A list paragraph.
 */
public final class ParaList extends Para implements SpanList {
    static ParaList newLine(SpanBranch parent, Matcher match) {
        if (ParaListPattern.BULLET.group(match) == null) return new ParaList(parent, CssLineStyles.NUMBERED);
        return new ParaList(parent, CssLineStyles.BULLET);
    }

    private int listLevel;
    private int listPosition;
    private Optional<TextFormatted> listText;

    private ParaList(SpanBranch parent, CssLineStyles style) {
        super(parent, style);
        listLevel = 0;
        listPosition = 1;
    }

    @Override
    public int getLevel() {
        return listLevel;
    }

    @Override
    public int getPosition() {
        return listPosition;
    }

    @Override
    public boolean isBullet() {
        return getLineStyle() == CssLineStyles.BULLET;
    }

    @Override
    protected void buildSpan(Matcher match) {
        listLevel = SpanLeaf.addLeaf(this, ParaListPattern.BULLET.group(match))
                .or(() -> SpanLeaf.addLeaf(this, ParaListPattern.NUMBERED.group(match))).map(((raw) -> raw.length()))
                .get();

        listText = addText(match, ParaListPattern.TEXT);

        SpanLeaf.addLeaf(this, ParaListPattern.ENDER.group(match));
    }

    void setPosition(int pos) {
        listPosition = pos;
    }

    @Override
    public int getOutlineCount() {
        return getOutline(listText);
    }

    @Override
    public int getWrittenCount() {
        return getWritten(listText);
    }
}
