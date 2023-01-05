package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ReferencePointerSpan extends SpanBranch implements IdentitySpan.IdentityHolder {
    public static ReferencePointerSpan createSpan(SpanBranch parent, String text, SpanStyles... classes) {
        Matcher match = ReferencePattern.matcher(text);
        if (match == null) return null;

        ReferencePointerSpan span = new ReferencePointerSpan(parent, classes);

        String raw = ReferencePattern.START.group(match);
        span.add(new SpanLeaf(span, raw));
        IdentityGroup group = null;
        if ((raw = ReferencePattern.FOOTREF.group(match)) != null) {
            group = IdentityGroup.FOOTNOTE;
        } else if ((raw = ReferencePattern.ENDREF.group(match)) != null) {
            group = IdentityGroup.ENDNOTE;
        } else if ((raw = ReferencePattern.CITEREF.group(match)) != null) {
            group = IdentityGroup.CITATION;
        } else if ((raw = ReferencePattern.METAREF.group(match)) != null) {
            group = IdentityGroup.META;
        } else if ((raw = ReferencePattern.IMAGE.group(match)) != null) {
            group = IdentityGroup.IMAGE;
        }
        if (group != null) {
            span.add(new SpanLeaf(span, raw));
            span.addStyle(group.getStyleClass());
        } else {
            span.addStyle(SpanStyles.ERROR);
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
            span.add(new SpanLeaf(span, raw));
        }
        return span;
    }

    private Optional<IdentitySpan> idPointer;

    private ReferencePointerSpan(SpanBranch parent, SpanStyles... classes) {
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
