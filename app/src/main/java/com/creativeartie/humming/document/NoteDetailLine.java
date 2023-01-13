package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class NoteDetailLine extends LineSpan {
    protected NoteDetailLine(SpanBranch parent) {
        super(parent, LineStyles.NOTE);
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw;

        add(new SpanLeaf(this, NoteLinePatterns.NoteLineParts.STARTER.group(match)));
        add(new SpanLeaf(this, NoteLinePatterns.NoteLineParts.NOTE.group(match)));
        if ((raw = NoteLinePatterns.NoteLineParts.TEXT.group(match)) != null) {
            add(LineText.newBasicText(this, raw));
        }
        addLineEnd(match, NoteLinePatterns.NoteLineParts.ENDER);
    }
}
