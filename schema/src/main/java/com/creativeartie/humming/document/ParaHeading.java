package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

/**
 * A heading paragraph with id
 */
public final class ParaHeading extends Para implements IdentityParent {
    static ParaHeading newLine(SpanBranch parent, Matcher match) {
        if (ParaHeadingPattern.OUTLINE.group(match) == null) {
            return new ParaHeading(parent, CssLineStyles.HEADING);
        }
        return new ParaHeading(parent, CssLineStyles.OUTLINE);
    }

    private ParaHeading(SpanBranch parent, CssLineStyles style) {
        super(parent, style);
    }

    private Optional<IdentitySpan> headingId;
    private int headingLevel;
    private Optional<TextFormatted> headingText;

    @Override
    protected void buildSpan(Matcher match) {
        String raw;
        if ((raw = ParaHeadingPattern.OUTLINE.group(match)) != null) {
            SpanLeaf.addLeaf(this, raw);

        }
        if ((raw = ParaHeadingPattern.LEVEL.group(match)) != null) {
            SpanLeaf.addLeaf(this, raw);
            headingLevel = raw.length();
        }
        TextFormatted text = null;
        if ((raw = ParaHeadingPattern.TEXT.group(match)) != null) {
            text = TextFormatted.newHeadingText(this, raw);
            add(text);
        }
        headingText = Optional.ofNullable(text);
        headingId = Optional.empty();
        if ((raw = ParaHeadingPattern.IDER.group(match)) != null) {
            SpanLeaf.addLeaf(this, raw);
            if ((raw = ParaHeadingPattern.ID.group(match)) != null) {
                headingId = Optional.of(IdentitySpan.newAddressId(this, raw, IdentityGroup.HEADING));
                add(headingId.get());
            } else if ((raw = ParaHeadingPattern.ERROR.group(match)) != null) {
                add(TextSpan.newSimple(this, raw, CssSpanStyles.ERROR));
            }
        }
        addLineEnd(match, ParaHeadingPattern.ENDER);
    }

    /**
     * Get heading level
     *
     * @return heading level
     */
    public int getLevel() {
        return headingLevel;
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        return headingId;
    }

    @Override
    public int getOutlineCount() {
        return getOutline(headingText) + (getLineStyle() == CssLineStyles.OUTLINE ? getWritten(headingText) : 0);
    }

    @Override
    public int getWrittenCount() {
        if (getLineStyle() == CssLineStyles.OUTLINE) {
            return 0;
        }
        return getWritten(headingText);
    }
}
