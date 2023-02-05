package com.creativeartie.humming.document;

import java.util.*;

public class SectionDivision extends Division {
    private int sectionLevel;

    SectionDivision(Document root) {
        super(root);
        sectionLevel = 1;
    }

    private SectionDivision(SpanBranch parent, int level) {
        super(parent);
        sectionLevel = level;
    }

    private Optional<Division> addHeading(HeadingLine heading) {
        if (heading.getLevel() <= sectionLevel) {
            if (isEmpty() && heading.getLevel() == sectionLevel) {
                add(heading);
                return Optional.of(this);
            }
            if (sectionLevel == 1) {
                SectionDivision sibling = new SectionDivision(getRoot());
                getRoot().add(sibling);
                return sibling.addHeading(heading);
            }
            return ((SectionDivision) getParent().get()).addHeading(heading);
        }

        SectionDivision child = new SectionDivision(this, sectionLevel + 1);
        add(child);
        if (child.sectionLevel == sectionLevel + 1) child.add(heading);
        else return child.addHeading(heading);
        return Optional.of(child);
    }

    @Override
    protected Optional<Division> addLine(LineSpan line, LineStyles style) {
        switch (style) {
            case BULLET:
            case NUMBERED:
                ListDivision division = new ListDivision(this, (ListLine) line);
                return division.addLine(line, style);
            case HEADER:
                break;
            case HEADING:
                return addHeading((HeadingLine) line);
            case OUTLINE:
                break;
            case ROW:
                break;
            case SUMMARY:
                break;
            case FIELD:
                break;
            case NOTE:
                break;
            default:
                add(line);
        }
        return Optional.empty();
    }

    public int getLevel() {
        return sectionLevel;
    }
}
