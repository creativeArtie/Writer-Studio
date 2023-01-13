package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class NoteFieldLine extends LineSpan {
    private String noteKey;
    private String noteValue;

    public NoteFieldLine(SpanBranch parent) {
        super(parent, LineStyles.FIELD);
        noteKey = "";
        noteValue = "";
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw;

        add(new SpanLeaf(this, NoteLinePatterns.NoteLineParts.STARTER.group(match)));
        add(new SpanLeaf(this, NoteLinePatterns.NoteLineParts.FIELD.group(match)));
        if ((raw = NoteLinePatterns.NoteLineParts.KEY.group(match)) != null) {
            TextSpan span = TextSpan.newFieldKey(this, raw);
            noteKey = span.getText();
            add(span);

            add(new SpanLeaf(this, NoteLinePatterns.NoteLineParts.FIELDER.group(match)));

            span = TextSpan.newSimple(this, NoteLinePatterns.NoteLineParts.VALUE.group(match));
            noteValue = span.getText();
            add(span);
        } else {
            add(TextSpan.newSimple(this, NoteLinePatterns.NoteLineParts.ERROR.group(match)));
            addStyle(SpanStyles.ERROR);
        }

        addLineEnd(match, NoteLinePatterns.NoteLineParts.ENDER);
    }

    public String getKey() {
        return noteKey;
    }

    public String getValue() {
        return noteValue;
    }
}
