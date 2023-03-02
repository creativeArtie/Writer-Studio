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

    protected Optional<Division> addHeading(HeadingLine heading) {
        if (sectionLevel == heading.getLevel()) {
            if (isEmpty()) {
                add(heading);
                return Optional.of(this);
            }
            SectionDivision division = new SectionDivision(this, heading.getLevel());

            findParent(SectionDivision.class)
                    .ifPresentOrElse((span) -> span.add(division), () -> getRoot().add(division));

            division.add(heading);
            return Optional.of(division);
        } else if (sectionLevel < heading.getLevel()) {
            SectionDivision division = new SectionDivision(this, sectionLevel + 1);
            add(division);
            return division.addHeading(heading);
        } // else if (sectionLevel > heading.getLevel()
        return useParent(SectionDivision.class).addHeading(heading);
    }

    @Override
    protected Optional<Division> addLine(LineSpan line, LineStyles style) {
        switch (style) {
            case BULLET:
            case NUMBERED:
                ListDivision list = new ListDivision(this, (ListLine) line);
                add(list);
                return list.addLine(line, style);
            case HEADING:
                return addHeading((HeadingLine) line);
            case OUTLINE:
                OutlineDivision outline = new OutlineDivision(this, (HeadingLine) line);
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
        Optional<SectionDivision> parent = findParent(SectionDivision.class);
        int position = 1;

        if (parent.isEmpty()) {
            for (SectionDivision div : getRoot()) {
                if (div == this) {
                    return Integer.toString(position);
                }
                position++;
            }
        } else {
            for (Span span : parent.get()) {
                if (span instanceof SectionDivision) {
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
