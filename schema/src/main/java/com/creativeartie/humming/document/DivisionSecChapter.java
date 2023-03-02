package com.creativeartie.humming.document;

import java.util.*;

public class DivisionSecChapter extends DivisionSec {
    DivisionSecChapter(Document root) {
        super(root);
        sectionLevel = 1;
    }

    @Override
    protected DivisionSec newDivision(SpanBranch parent, int level) {
        return new DivisionSecChapter(parent, level);
    }

    @Override
    protected Class<? extends DivisionSec> getSectionClass() {
        return getClass();
    }

    private DivisionSecChapter(SpanBranch parent, int level) {
        super(parent, level);
        sectionLevel = level;
    }

    @Override
    protected void addDivisionAtRoot(DivisionSec child) {
        DivisionSecChapter div = (DivisionSecChapter) child;
        getRoot().add(div);
    }

    @Override
    protected String getLocationAtRoot() {
        int position = 1;
        for (DivisionSecChapter div : getRoot()) {
            if (div == this) {
                return Integer.toString(position);
            }
            position++;
        }
        throw new IllegalStateException("Division not found.");
    }

    @Override
    protected Optional<Division> addHeadingLine(HeadingLine line) {
        return addHeading(line);
    }

    @Override
    protected Optional<Division> addOutlineLine(HeadingLine line) {
        DivisionSecScene outline = new DivisionSecScene(this);
        add(outline);
        return outline.addHeading(line);
    }
}
