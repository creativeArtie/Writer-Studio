package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

/**
 * Main note text paragraph.
 */
public final class ParaNoteDetail extends Para {
    ParaNoteDetail(SpanBranch parent) {
        super(parent, CssLineStyles.NOTE);
    }

    @Override
    protected void buildSpan(Matcher match) {
        add(new SpanLeaf(this, ParaNotePatterns.NoteLineParts.NOTE.group(match)));
        addText(match, ParaNotePatterns.NoteLineParts.TEXT);
        addLineEnd(match, ParaNotePatterns.NoteLineParts.ENDER);
    }
}
