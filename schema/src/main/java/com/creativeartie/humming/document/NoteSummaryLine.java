package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class NoteSummaryLine extends LineSpan implements IdentitySpan.IdentityParent {
    private Optional<IdentitySpan> noteId;

    protected NoteSummaryLine(SpanBranch parent) {
        super(parent, LineStyles.SUMMARY);
        noteId = Optional.empty();
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw;

        add(new SpanLeaf(this, NoteLinePatterns.NoteLineParts.STARTER.group(match)));
        add(new SpanLeaf(this, NoteLinePatterns.NoteLineParts.HEADING.group(match)));
        if ((raw = NoteLinePatterns.NoteLineParts.TITLE.group(match)) != null) {
            add(LineText.newHeadingText(this, raw));
        }
        if ((raw = NoteLinePatterns.NoteLineParts.IDER.group(match)) != null) {
            add(new SpanLeaf(this, raw));
            if ((raw = NoteLinePatterns.NoteLineParts.ID.group(match)) != null) {
                noteId = Optional.of(IdentitySpan.newAddressId(this, raw, IdentityGroup.NOTE));
                add(noteId.get());
            }
        }
        addLineEnd(match, NoteLinePatterns.NoteLineParts.ENDER);
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        return noteId;
    }
}
