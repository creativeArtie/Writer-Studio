package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

/**
 * A property of note.
 */
public final class ParaNoteField extends Para {
    private String noteKey;
    private String noteValue;

    ParaNoteField(SpanBranch parent) {
        super(parent, CssLineStyles.FIELD);
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
            addStyle(CssSpanStyles.ERROR);
        }

        addLineEnd(match, ParaNotePatterns.NoteLineParts.ENDER);
    }

    /**
     * get the property key
     *
     * @return the key
     */
    public String getKey() {
        return noteKey;
    }

    /**
     * Gets the property value
     *
     * @return property value
     */
    public String getValue() {
        return noteValue;
    }
}
