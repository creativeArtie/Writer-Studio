package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ReferencePointerSpan extends SpanBranch implements IdentitySpan.IdentityHolder {
    public static ReferencePointerSpan createSpan(SpanBranch parent, String text) {
        Matcher match = ReferencePattern.matcher(text);
        if (match == null) return null;

        ReferencePointerSpan span = new ReferencePointerSpan(parent);

        String raw = ReferencePattern.START.group(match);
        span.add(new SpanLeaf(span, raw.length()));
        IdentityGroup group = null;
        if ((raw = ReferencePattern.FOOTNOTE.group(match)) != null) {
            group = IdentityGroup.FOOTNOTE;
        } else if ((raw = ReferencePattern.ENDNOTE.group(match)) != null) {
            group = IdentityGroup.ENDNOTE;
        } else if ((raw = ReferencePattern.SOURCE.group(match)) != null) {
            group = IdentityGroup.SOURCE;
        } else if ((raw = ReferencePattern.REF.group(match)) != null) {
            group = IdentityGroup.REF;
        }
        span.add(new SpanLeaf(span, raw.length()));
        span.addStyle(group.getStyleClass());

        raw = ReferencePattern.ID.group(match);
        IdentitySpan id = IdentitySpan.newPointerId(span, raw, group);
        span.add(id);
        span.idPointer = id;

        raw = ReferencePattern.END.group(match);
        if (raw != null) {
            span.add(new SpanLeaf(span, raw.length()));
        }
        return span;
    }

    private IdentitySpan idPointer;

    private ReferencePointerSpan(SpanBranch parent, StyleClasses... classes) {
        super(parent, classes);
    }

    public IdentitySpan getPointer() {
        return idPointer;
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }
}
