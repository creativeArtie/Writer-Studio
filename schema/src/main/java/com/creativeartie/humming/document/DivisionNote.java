package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

/**
 * Division for a note.
 */
public final class DivisionNote extends Division implements IdentityParent {
    Optional<IdentitySpan> noteId;
    Map<String, String> fields;

    DivisionNote(SpanBranch parent) {
        super(parent);
        noteId = Optional.empty();
        fields = new TreeMap<>();
    }

    /**
     * gets the list of fields
     *
     * @return an {@linkplain ImmutableMap} of fields.
     */
    public Map<String, String> getFields() {
        return ImmutableMap.copyOf(fields);
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
    protected Division addLine(Para line, CssLineStyles style) {
        switch (style) {
            case FIELD:
                ParaNoteField field = (ParaNoteField) line;
                add(field);
                if (field.getKey() != "") fields.put(field.getKey(), field.getValue());
                return this;
            case HEADER:
                if (isEmpty()) {
                    ParaNoteHead head = (ParaNoteHead) line;
                    noteId = head.getPointer();
                    add(head);
                    return this;
                }
                return findParent(Division.class).get().addLine(line, style);
            case NOTE:
                add(line);
                return this;
            default:
                return findParent(Division.class).get().addLine(line, style);
        }
    }
}
