package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public class DivisionNote extends Division implements IdentitySpan.IdentityParent {
    Optional<IdentitySpan> noteId;
    Map<String, String> fields;

    protected DivisionNote(SpanBranch parent) {
        super(parent);
        noteId = Optional.empty();
        fields = new TreeMap<>();
    }

    @Override
    protected Optional<Division> addLine(Para line, StyleLines style) {
        switch (style) {
            case FIELD:
                ParaNoteField field = (ParaNoteField) line;
                add(field);
                if (field.getKey() != "") fields.put(field.getKey(), field.getValue());
                return Optional.of(this);
            case HEADER:
                if (isEmpty()) {
                    ParaNoteHead head = (ParaNoteHead) line;
                    noteId = head.getPointer();
                    add(head);
                    return Optional.of(this);
                }
                return useParent(Division.class).addLine(line, style);
            case NOTE:
                add(line);
                return Optional.of(this);
            default:
                return useParent(Division.class).addLine(line, style);
        }
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        return noteId;
    }

    public Map<String, String> getFields() {
        return ImmutableMap.copyOf(fields);
    }
}
