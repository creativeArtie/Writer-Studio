package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ParaNoteDetail extends Para {
    protected ParaNoteDetail(SpanBranch parent) {
        super(parent, StyleLines.NOTE);
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw;

        add(new SpanLeaf(this, ParaNotePatterns.NoteLineParts.STARTER.group(match)));
        add(new SpanLeaf(this, ParaNotePatterns.NoteLineParts.NOTE.group(match)));
        if ((raw = ParaNotePatterns.NoteLineParts.TEXT.group(match)) != null) {
            add(TextFormatted.newBasicText(this, raw));
        }
        addLineEnd(match, ParaNotePatterns.NoteLineParts.ENDER);
    }
}
