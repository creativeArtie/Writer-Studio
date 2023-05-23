package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

/**
 * A span for pointing references. Such as {@code {^note}}. List of pointers
 * used:
 * <ul>
 * <li>{@link IdentityGroup#FOOTNOTE} footnote
 * <li>{@link IdentityGroup#NOTE} research note
 * <li>{@link IdentityGroup#META} meta data
 * </ul>
 */
public final class IdentityReference extends SpanBranch implements IdentityParent {
    static IdentityReference newSpan(SpanBranch parent, String text, CssSpanStyles... classes) {
        Matcher match = IdentityReferencePattern.matcher(text);
        if (match == null) return null;

        IdentityReference span = new IdentityReference(parent, classes);

        String raw = IdentityReferencePattern.START.group(match);
        SpanLeaf.addLeaf(span, raw);
        IdentityGroup group = null;
        if ((raw = IdentityReferencePattern.FOOTREF.group(match)) != null) {
            group = IdentityGroup.FOOTNOTE;
        } else if ((raw = IdentityReferencePattern.CITEREF.group(match)) != null) {
            group = IdentityGroup.NOTE;
        } else if ((raw = IdentityReferencePattern.METAREF.group(match)) != null) {
            group = IdentityGroup.META;
        }
        if (group != null) {
            SpanLeaf.addLeaf(span, raw);
            span.addStyle(group.getStyleClass());
        } else {
            span.addStyle(CssSpanStyles.ERROR);
        }
        if ((raw = IdentityReferencePattern.ID.group(match)) != null) {
            IdentitySpan id = IdentitySpan.newPointerId(span, raw, group);
            span.add(id);
            span.idPointer = Optional.of(id);
        } else if ((raw = IdentityReferencePattern.ERROR.group(match)) != null) {
            span.add(TextSpan.newSpecial(span, raw));
        }
        raw = IdentityReferencePattern.END.group(match);
        SpanLeaf.addLeaf(span, raw);
        return span;
    }

    private Optional<IdentitySpan> idPointer;

    private IdentityReference(SpanBranch parent, CssSpanStyles... classes) {
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
