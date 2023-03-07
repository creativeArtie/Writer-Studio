package com.creativeartie.humming.document;

import java.util.Optional;

import com.google.common.base.*;

public class DivisionSecScene extends DivisionSec {
    public DivisionSecScene(SpanBranch parent) {
        super(parent, 1);
    }

    public DivisionSecScene(SpanBranch parent, int level) {
        super(parent, level);
    }

    @Override
    protected Class<? extends DivisionSec> getSectionClass() {
        return DivisionSecScene.class;
    }

    @Override
    protected DivisionSec newDivision(SpanBranch parent, int level) {
        return new DivisionSecScene(parent, level);
    }

    @Override
    protected void addDivisionAtRoot(DivisionSec child) {
        Optional<DivisionSecChapter> parent = findParent(DivisionSecChapter.class);
        Preconditions.checkState(parent.isPresent(), "Parent not found");
        parent.get().add(child);
    }

    @Override
    protected Optional<Division> addHeadingLine(HeadingLine line) {
        return findParent(DivisionSecChapter.class).get().addLine(line, line.getLineStyle());
    }

    @Override
    protected Optional<Division> addOutlineLine(HeadingLine line) {
        return addHeading(line);
    }

    @Override
    protected String getLocationAtRoot() {
        int position = 1;
        for (Span div : findParent(DivisionSecChapter.class).get()) {
            if (div instanceof DivisionSecScene) {
                if (div == this) {
                    return Integer.toString(position);
                }
                position++;
            }
        }
        throw new IllegalStateException("Division not found.");
    }

    public DivisionSecChapter getChapter() {
        return findParent(DivisionSecChapter.class).get();
    }
}
