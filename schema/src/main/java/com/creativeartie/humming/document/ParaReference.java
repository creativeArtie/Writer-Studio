package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.ParaReferencePatterns.*;

/**
 * Footnote/endnote/image reference line.
 */
public final class ParaReference extends Para implements IdentityParent {
    static ParaReference newFootnote(SpanBranch parent) {
        return new ParaReference(parent, CssLineStyles.FOOTNOTE);
    }

    static ParaReference newImage(SpanBranch parent) {
        return new ParaReference(parent, CssLineStyles.IMAGE);
    }

    private Optional<IdentitySpan> idAddress;
    private Optional<TextFormatted> refText;

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
        } else { // if getLineStyle() == CssLineStyles.IMAGE
            pattern = RefLineParts.IMAGE;
            group = IdentityGroup.IMAGE;
            isAddress = false;
        }
        SpanLeaf.addLeaf(this, RefLineParts.START.group(match));
        SpanLeaf.addLeaf(this, pattern.group(match));

        String raw;
        TextFormatted text = null;
        if ((raw = RefLineParts.ID.group(match)) != null) {

            idAddress = Optional.of(
                    isAddress ? IdentitySpan.newAddressId(this, raw, group) :
                            IdentitySpan.newPointerId(this, raw, group)
            );
            add(idAddress.get());
            if (SpanLeaf.addLeaf(this, RefLineParts.SEP.group(match)).isPresent()) {
                text = TextFormatted.newNoteText(this, RefLineParts.TEXT.group(match));
                add(text);
            }
        } else {
            addStyle(CssSpanStyles.ERROR);
            add(TextFormatted.newBasicText(this, RefLineParts.ERROR.group(match)));
        }
        refText = Optional.ofNullable(text);

        SpanLeaf.addLeaf(this, RefLineParts.ENDER.group(match));
    }

    @Override
    public int getOutlineCount() {
        return getOutline(refText);
    }

    @Override
    public int getWrittenCount() {
        return getWritten(refText);
    }
}
