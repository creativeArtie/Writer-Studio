package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

/**
 * A note heading.
 */
public final class ParaNoteHead extends Para implements IdentityParent {
    private Optional<IdentitySpan> noteId;
    private Optional<TextFormatted> noteHeading;

    ParaNoteHead(SpanBranch parent) {
        super(parent, CssLineStyles.HEADER);
        noteId = Optional.empty();
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw;
        SpanLeaf.addLeaf(this, ParaNotePatterns.NoteLineParts.HEADING.group(match));
        TextFormatted text = null;
        if ((raw = ParaNotePatterns.NoteLineParts.TITLE.group(match)) != null) {
            text = TextFormatted.newHeadingText(this, raw);
            add(text);
        }
        noteHeading = Optional.ofNullable(text);
        if ((raw = ParaNotePatterns.NoteLineParts.IDER.group(match)) != null) {
            SpanLeaf.addLeaf(this, raw);
            if ((raw = ParaNotePatterns.NoteLineParts.ID.group(match)) != null) {
                noteId = Optional.of(IdentitySpan.newAddressId(this, raw, IdentityGroup.NOTE));
                add(noteId.get());
            }
        }
        addLineEnd(match, ParaNotePatterns.NoteLineParts.ENDER);
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        return noteId;
    }

    @Override
    public int getOutlineCount() {
        return getWritten(noteHeading) + getOutline(noteHeading);
    }

    @Override
    public int getWrittenCount() {
        return 0;
    }
}
