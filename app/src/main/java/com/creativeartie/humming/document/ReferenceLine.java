package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.ReferenceLinePatterns.*;

public class ReferenceLine extends LineSpan implements IdentitySpan.IdentityParent {
    public static ReferenceLine newFootnote(SpanBranch parent) {
        return new ReferenceLine(parent, LineStyles.FOOTNOTE);
    }

    public static ReferenceLine newEndnote(SpanBranch parent) {
        return new ReferenceLine(parent, LineStyles.ENDNOTE);
    }

    private Optional<IdentitySpan> idAddress;

    private ReferenceLine(SpanBranch parent, LineStyles style) {
        super(parent, style);
        idAddress = Optional.empty();
    }

    @Override
    protected void buildSpan(Matcher match) {
        RefLineParts pattern;
        IdentityGroup group;
        if (getLineStyle() == LineStyles.FOOTNOTE) {
            pattern = RefLineParts.FOOTNOTE;
            group = IdentityGroup.FOOTNOTE;
        } else {
            pattern = RefLineParts.ENDNOTE;
            group = IdentityGroup.ENDNOTE;
        }

        add(new SpanLeaf(this, RefLineParts.START.group(match)));
        add(new SpanLeaf(this, pattern.group(match)));
        String raw;
        if ((raw = RefLineParts.ERROR.group(match)) != null) {
            addStyle(SpanStyles.ERROR);
            add(LineText.newBasicText(this, raw));
        } else {
            idAddress = Optional.of(IdentitySpan.newAddressId(this, RefLineParts.ID.group(match), group));
            add(new SpanLeaf(this, RefLineParts.SEP.group(match)));
        }
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        return idAddress;
    }
}
