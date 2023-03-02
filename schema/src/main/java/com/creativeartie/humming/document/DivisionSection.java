package com.creativeartie.humming.document;

import java.util.*;

public class DivisionSection extends Division {
    private int sectionLevel;

    DivisionSection(Document root) {
        super(root);
        sectionLevel = 1;
    }

    private DivisionSection(SpanBranch parent, int level) {
        super(parent);
        sectionLevel = level;
    }

    protected Optional<Division> addHeading(HeadingLine heading) {
        if (sectionLevel == heading.getLevel()) {
            if (isEmpty()) {
                add(heading);
                return Optional.of(this);
            }
            DivisionSection division = new DivisionSection(this, heading.getLevel());

            findParent(DivisionSection.class)
                    .ifPresentOrElse((span) -> span.add(division), () -> getRoot().add(division));

            division.add(heading);
            return Optional.of(division);
        } else if (sectionLevel < heading.getLevel()) {
            DivisionSection division = new DivisionSection(this, sectionLevel + 1);
            add(division);
            return division.addHeading(heading);
        } // else if (sectionLevel > heading.getLevel()
        return useParent(DivisionSection.class).addHeading(heading);
    }

    @Override
    protected Optional<Division> addLine(Para line, StyleLines style) {
        switch (style) {
            case BULLET:
            case NUMBERED:
                DivisionList list = new DivisionList(this, (ParaList) line);
                add(list);
                return list.addLine(line, style);
            case HEADING:
                return addHeading((HeadingLine) line);
            case OUTLINE:
                DivisionOutline outline = new DivisionOutline(this, (HeadingLine) line);
                add(outline);
                return outline.addLine(line, style);
            case ROW:
                break;
            case HEADER:
            case FIELD:
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

    public String getLocation() {
        Optional<DivisionSection> parent = findParent(DivisionSection.class);
        int position = 1;

        if (parent.isEmpty()) {
            for (DivisionSection div : getRoot()) {
                if (div == this) {
                    return Integer.toString(position);
                }
                position++;
            }
        } else {
            for (Span span : parent.get()) {
                if (span instanceof DivisionSection) {
                    if (span == this) {
                        break;
                    }
                    position++;
                }
            }
            return parent.get().getLocation() + ":" + Integer.toString(position);
        }

        throw new IllegalStateException("Division not found.");
    }
}
