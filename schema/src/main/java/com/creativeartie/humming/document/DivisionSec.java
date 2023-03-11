package com.creativeartie.humming.document;

import java.util.*;

public abstract class DivisionSec extends Division {
    protected int sectionLevel;

    protected DivisionSec(Document root) {
        super(root);
    }

    protected DivisionSec(SpanBranch parent, int level) {
        super(parent);
        sectionLevel = level;
    }

    protected abstract Class<? extends DivisionSec> getSectionClass();

    protected abstract DivisionSec newDivision(SpanBranch parent, int level);

    protected abstract void addDivisionAtRoot(DivisionSec child);

    protected Division addHeading(HeadingLine heading) {
        if (sectionLevel == heading.getLevel()) {
            if (isEmpty()) {
                add(heading);
                return this;
            }
            DivisionSec division = newDivision(this, heading.getLevel());

            findParent(getSectionClass())
                    .ifPresentOrElse((span) -> span.add(division), () -> addDivisionAtRoot(division));

            division.add(heading);
            return division;
        } else if (sectionLevel < heading.getLevel()) {
            DivisionSec division = newDivision(this, sectionLevel + 1);
            add(division);
            return division.addHeading(heading);
        } // else if (sectionLevel > heading.getLevel()
        return useParent(getSectionClass()).addHeading(heading);
    }

    public int getLevel() {
        return sectionLevel;
    }

    protected abstract Division addHeadingLine(HeadingLine line);

    protected abstract Division addOutlineLine(HeadingLine line);

    @Override
    protected Division addLine(Para line, StyleLines style) {
        switch (style) {
            case BULLET:
            case NUMBERED:
                DivisionList list = new DivisionList(this, (ParaList) line);
                add(list);
                return list.addLine(line, style);
            case HEADING:
                return addHeadingLine((HeadingLine) line);
            case OUTLINE:
                return addOutlineLine((HeadingLine) line);
            case ROW:
                DivisionTable table = new DivisionTable(this);
                add(table);
                return table.addLine(line, style);
            case HEADER:
            case FIELD:
            case NOTE:
                DivisionNote note = new DivisionNote(this);
                add(note);
                return note.addLine(line, style);
            default:
                add(line);
                return this;
        }
    }

    protected abstract String getLocationAtRoot();

    public String getLocation() {
        Optional<? extends DivisionSec> parent = findParent(getSectionClass());

        if (parent.isEmpty()) {
            return getLocationAtRoot();
        } else {
            int position = 1;
            for (Span span : parent.get()) {
                if (getSectionClass().isInstance(span)) {
                    if (span == this) {
                        break;
                    }
                    position++;
                }
            }
            return parent.get().getLocation() + ":" + Integer.toString(position);
        }
    }
}
