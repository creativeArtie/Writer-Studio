package com.creativeartie.humming.document;

import java.util.*;

/**
 * A Division Section
 */
public abstract class DivisionSec extends Division {
    private int sectionLevel;

    DivisionSec(Manuscript root) {
        super(root);
        sectionLevel = 1;
    }

    DivisionSec(SpanBranch parent, int level) {
        super(parent);
        sectionLevel = level;
    }

    /**
     * get the Section class
     *
     * @return the {@linkplain Class} object
     *
     * @see #addHeading(ParaHeading)
     */
    protected abstract Class<? extends DivisionSec> getSectionClass();

    /**
     * create a new section division
     *
     * @param parent
     *        the parent span
     * @param level
     *        the level of this division
     *
     * @return the {@linkplain DivsionSec} object
     *
     * @see #addHeading(ParaHeading)
     */
    protected abstract DivisionSec newDivision(SpanBranch parent, int level);

    /**
     * Add the division at either {@link DivsionSecChapter} or {@link Manuscript}.
     *
     * @param child
     *        the child to add
     *
     * @see #addHeading(ParaHeading)
     */
    protected abstract void addDivisionAtRoot(DivisionSec child);

    /**
     * Adds a heading paragraph. This do different action on different situation
     *
     * @param heading
     *        heading line to add
     *
     * @return the next or this division to add the next line to
     *
     * @see #getSectionClass()
     * @see #newDivision(SpanBranch, int)
     * @see #addDivisionAtRoot(DivisionSec)
     */
    protected Division addHeading(ParaHeading heading) {
        if (sectionLevel == heading.getLevel()) {
            if (isEmpty()) {
                // first line + match level
                add(heading);
                return this;
            }

            DivisionSec division = newDivision(this, heading.getLevel());
            findParent(getSectionClass()).ifPresentOrElse(
                    // not first line + match level + not level 1
                    (span) -> span.add(division),
                    // not first line + match level + is level 1
                    () -> addDivisionAtRoot(division)
            );
            division.add(heading);
            return division;

        } else if (sectionLevel < heading.getLevel()) {
            // higher level (smaller number)
            DivisionSec division = newDivision(this, sectionLevel + 1);
            add(division);
            return division.addHeading(heading);
        } // else if (sectionLevel > heading.getLevel()
        return findParent(getSectionClass()).get().addHeading(heading);
    }

    /**
     * get the Section level
     *
     * @return the section level
     */
    public int getLevel() {
        return sectionLevel;
    }

    /**
     * What to do with a heading paragraph.
     *
     * @param line
     *        the to added
     *
     * @return the next or this division to add the next line to
     */
    protected abstract Division addHeadingLine(ParaHeading line);

    /**
     * What to do with a outline paragraph.
     *
     * @param line
     *        the to added
     *
     * @return the next or this division to add the next line to
     */
    protected abstract Division addOutlineLine(ParaHeading line);

    @Override
    protected Division addLine(Para line, CssLineStyles style) {
        switch (style) {
            case BULLET:
            case NUMBERED:
                DivisionList list = new DivisionList(this, (ParaList) line);
                add(list);
                return list.addLine(line, style);

            case HEADING:
                return addHeadingLine((ParaHeading) line);
            case OUTLINE:
                return addOutlineLine((ParaHeading) line);

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

    /**
     * Gets the positions of the section at the root level.
     *
     * @return the location string
     */
    protected abstract String getLocationAtRoot();

    /**
     * Gets the positions of the section.
     *
     * @return the location string
     */
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
            return parent.get().getLocation() + ":" + Integer.toString(position); //$NON-NLS-1$
        }
    }
}
