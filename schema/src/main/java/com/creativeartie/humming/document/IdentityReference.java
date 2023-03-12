package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class IdentityReference extends SpanBranch implements IdentitySpan.IdentityParent {
    static IdentityReference newSpan(SpanBranch parent, String text, StylesSpans... classes) {
        Matcher match = IdentityReferencePattern.matcher(text);
        if (match == null) return null;

        IdentityReference span = new IdentityReference(parent, classes);

        String raw = IdentityReferencePattern.START.group(match);
        span.add(new SpanLeaf(span, raw));
        IdentityGroup group = null;
        if ((raw = IdentityReferencePattern.FOOTREF.group(match)) != null) {
            group = IdentityGroup.FOOTNOTE;
        } else if ((raw = IdentityReferencePattern.ENDREF.group(match)) != null) {
            group = IdentityGroup.ENDNOTE;
        } else if ((raw = IdentityReferencePattern.CITEREF.group(match)) != null) {
            group = IdentityGroup.NOTE;
        } else if ((raw = IdentityReferencePattern.METAREF.group(match)) != null) {
            group = IdentityGroup.META;
        }
        if (group != null) {
            span.add(new SpanLeaf(span, raw));
            span.addStyle(group.getStyleClass());
        } else {
            span.addStyle(StylesSpans.ERROR);
        }
        if ((raw = IdentityReferencePattern.ID.group(match)) != null) {
            IdentitySpan id = IdentitySpan.newPointerId(span, raw, group);
            span.add(id);
            span.idPointer = Optional.of(id);
        } else if ((raw = IdentityReferencePattern.ERROR.group(match)) != null) {
            span.add(TextSpan.newSpecial(span, raw));
        }
        raw = IdentityReferencePattern.END.group(match);
        if (raw != null) {
            span.add(new SpanLeaf(span, raw));
        }
        return span;
    }

    private Optional<IdentitySpan> idPointer;

    private IdentityReference(SpanBranch parent, StylesSpans... classes) {
        super(parent, classes);
        idPointer = Optional.empty();
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        return idPointer;
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }
}
