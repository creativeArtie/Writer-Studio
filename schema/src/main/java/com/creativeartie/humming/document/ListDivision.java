package com.creativeartie.humming.document;

import java.util.*;

public class ListDivision extends Division {
    private boolean isBullet;
    private int listLevel;

    public ListDivision(Document root) {
        super(root);
        listLevel = 1;
    }

    public ListDivision(SpanBranch parent) {
        super(parent);
        listLevel = 1;
    }

    private ListDivision(SpanBranch parent, ListLine line) {
        super(parent);
    }

    @Override
    protected Optional<Division> addLine(LineSpan line, LineStyles style) {
        // TODO Auto-generated method stub
        switch (style) {
            case AGENDA:
                add(line);
            case BULLET:
                break;
            case NUMBERED:
                break;
            default:
                return Optional.of((Division) getParent().get());
        }
        return Optional.empty();
    }
}
