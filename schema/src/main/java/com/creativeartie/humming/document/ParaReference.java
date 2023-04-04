package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.ParaReferencePatterns.*;

/**
 * Footnote/endnote/image reference line.
 */
public final class ParaReference extends Para implements IdentityParent {
    static ParaReference newEndnote(SpanBranch parent) {
        return new ParaReference(parent, CssLineStyles.ENDNOTE);
    }

    static ParaReference newFootnote(SpanBranch parent) {
        return new ParaReference(parent, CssLineStyles.FOOTNOTE);
    }

    static ParaReference newImage(SpanBranch parent) {
        return new ParaReference(parent, CssLineStyles.IMAGE);
    }

    private Optional<IdentitySpan> idAddress;

    private ParaReference(SpanBranch parent, CssLineStyles style) {
        super(parent, style);
        idAddress = Optional.empty();
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        return idAddress;
    }

    @Override
    protected void buildSpan(Matcher match) {
        RefLineParts pattern;
        IdentityGroup group;
        boolean isAddress = true;
        if (getLineStyle() == CssLineStyles.FOOTNOTE) {
            pattern = RefLineParts.FOOTNOTE;
            group = IdentityGroup.FOOTNOTE;
        } else if (getLineStyle() == CssLineStyles.ENDNOTE) {
            pattern = RefLineParts.ENDNOTE;
            group = IdentityGroup.ENDNOTE;
        } else {
            pattern = RefLineParts.IMAGE;
            group = IdentityGroup.IMAGE;
            isAddress = false;
        }

        add(new SpanLeaf(this, RefLineParts.START.group(match)));
        add(new SpanLeaf(this, pattern.group(match)));
        String raw;
        if ((raw = RefLineParts.ID.group(match)) != null) {

            idAddress = Optional.of(
                    isAddress ? IdentitySpan.newAddressId(this, raw, group) :
                            IdentitySpan.newPointerId(this, raw, group)
            );
            add(idAddress.get());
            String sep = RefLineParts.SEP.group(match);
            if (sep != null) {
                add(new SpanLeaf(this, RefLineParts.SEP.group(match)));
                add(TextFormatted.newNoteText(this, RefLineParts.TEXT.group(match)));
            }
        } else {
            addStyle(CssSpanStyles.ERROR);
            add(TextFormatted.newBasicText(this, RefLineParts.ERROR.group(match)));
        }

        addLineEnd(match, RefLineParts.ENDER);
    }
}
