package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

/**
 * Main note text paragraph.
 */
public final class ParaNoteDetail extends Para {
    private Optional<TextFormatted> noteText;

    ParaNoteDetail(SpanBranch parent) {
        super(parent, CssLineStyles.NOTE);
    }

    @Override
    protected void buildSpan(Matcher match) {
        SpanLeaf.addLeaf(this, ParaNotePatterns.NoteLineParts.NOTE.group(match));
        noteText = addText(match, ParaNotePatterns.NoteLineParts.TEXT);
        SpanLeaf.addLeaf(this, ParaNotePatterns.NoteLineParts.ENDER.group(match));
    }

    @Override
    public int getOutlineCount() {
        return getWritten(noteText) + getOutline(noteText);
    }

    @Override
    public int getWrittenCount() {
        return 0;
    }
}
