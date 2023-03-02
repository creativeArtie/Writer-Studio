package com.creativeartie.humming.document;

import java.util.*;

public class OutlineDivision extends Division {
    public OutlineDivision(SpanBranch parent, HeadingLine line) {
        super(parent);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected Optional<Division> addLine(LineSpan line, LineStyles style) {
        switch (style) {
            case BULLET:
            case NUMBERED:
                ListDivision division = new ListDivision(this, (ListLine) line);
                add(division);
                return division.addLine(line, style);
            case OUTLINE:
                break;
            case HEADING:
                return findParent(SectionDivision.class).get().addLine(line, style);
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
