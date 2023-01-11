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
        if ((raw = RefLineParts.ID.group(match)) != null) {
            idAddress = Optional.of(IdentitySpan.newAddressId(this, raw, group));
            add(new SpanLeaf(this, RefLineParts.SEP.group(match)));
            add(LineText.newNoteText(this, RefLineParts.TEXT.group(match)));
        } else {
            addStyle(SpanStyles.ERROR);
            add(LineText.newBasicText(this, RefLineParts.ERROR.group(match)));
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
