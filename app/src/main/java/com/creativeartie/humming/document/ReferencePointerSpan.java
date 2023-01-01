package com.creativeartie.humming.document;

import java.util.*;
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
        } else if ((raw = ReferencePattern.IMAGE.group(match)) != null) {
            group = IdentityGroup.IMAGE;
        }
        if (group != null) {
            span.add(new SpanLeaf(span, raw.length()));
            span.addStyle(group.getStyleClass());
        } else {
            span.addStyle(StyleClasses.ERROR);
        }
        if ((raw = ReferencePattern.ID.group(match)) != null) {
            IdentitySpan id = IdentitySpan.newPointerId(span, raw, group);
            span.add(id);
            span.idPointer = Optional.of(id);
        } else if ((raw = ReferencePattern.ERROR.group(match)) != null) {
            span.add(TextSpan.newSpecial(span, raw));
        }
        raw = ReferencePattern.END.group(match);
        if (raw != null) {
            span.add(new SpanLeaf(span, raw.length()));
        }
        return span;
    }

    private Optional<IdentitySpan> idPointer;

    private ReferencePointerSpan(SpanBranch parent, StyleClasses... classes) {
        super(parent, classes);
        idPointer = Optional.empty();
    }

    public Optional<IdentitySpan> getPointer() {
        return idPointer;
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }
}
