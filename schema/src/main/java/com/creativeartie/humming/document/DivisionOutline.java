package com.creativeartie.humming.document;

import java.util.*;

public class DivisionOutline extends Division {
    public DivisionOutline(SpanBranch parent, HeadingLine line) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Optional<Division> addLine(Para line, StyleLines style) {
        switch (style) {
            case BULLET:
            case NUMBERED:
                DivisionList division = new DivisionList(this, (ParaList) line);
                add(division);
                return division.addLine(line, style);
            case OUTLINE:
                break;
            case HEADING:
                return findParent(DivisionSection.class).get().addLine(line, style);
            case FIELD:
            case HEADER:
            case NOTE:
                break;
            case ROW:
                break;
            default:
                add(line);
                return Optional.empty();
        }
        // TODO Auto-generated method stub
        return Optional.empty();
    }
}
