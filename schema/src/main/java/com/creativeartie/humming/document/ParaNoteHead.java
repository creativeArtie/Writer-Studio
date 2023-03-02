package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ParaNoteHead extends Para implements IdentitySpan.IdentityParent {
    private Optional<IdentitySpan> noteId;

    protected ParaNoteHead(SpanBranch parent) {
        super(parent, StyleLines.HEADER);
        noteId = Optional.empty();
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw;

        add(new SpanLeaf(this, ParaNotePatterns.NoteLineParts.STARTER.group(match)));
        add(new SpanLeaf(this, ParaNotePatterns.NoteLineParts.HEADING.group(match)));
        if ((raw = ParaNotePatterns.NoteLineParts.TITLE.group(match)) != null) {
            add(TextFormatted.newHeadingText(this, raw));
        }
        if ((raw = ParaNotePatterns.NoteLineParts.IDER.group(match)) != null) {
            add(new SpanLeaf(this, raw));
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
}
