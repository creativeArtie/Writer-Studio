package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ParaNoteField extends Para {
    private String noteKey;
    private String noteValue;

    public ParaNoteField(SpanBranch parent) {
        super(parent, StyleLines.FIELD);
        noteKey = "";
        noteValue = "";
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw;
        add(new SpanLeaf(this, ParaNotePatterns.NoteLineParts.FIELD.group(match)));
        if ((raw = ParaNotePatterns.NoteLineParts.KEY.group(match)) != null) {
            TextSpan span = TextSpan.newFieldKey(this, raw);
            noteKey = span.getText();
            add(span);

            add(new SpanLeaf(this, ParaNotePatterns.NoteLineParts.FIELDER.group(match)));

            span = TextSpan.newSimple(this, ParaNotePatterns.NoteLineParts.VALUE.group(match));
            noteValue = span.getText();
            add(span);
        } else {
            add(TextSpan.newSimple(this, ParaNotePatterns.NoteLineParts.ERROR.group(match)));
            addStyle(StylesSpans.ERROR);
        }

        addLineEnd(match, ParaNotePatterns.NoteLineParts.ENDER);
    }

    public String getKey() {
        return noteKey;
    }

    public String getValue() {
        return noteValue;
    }
}
