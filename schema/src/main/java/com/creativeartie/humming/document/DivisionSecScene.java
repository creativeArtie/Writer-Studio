package com.creativeartie.humming.document;

import java.util.Optional;

import com.google.common.base.*;

/**
 * A scene in a chapter.
 */
public final class DivisionSecScene extends DivisionSec {
    DivisionSecScene(SpanBranch parent) {
        super(parent, 1);
    }

    DivisionSecScene(SpanBranch parent, int level) {
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
        Preconditions.checkState(parent.isPresent(), "Parent not found"); //$NON-NLS-1$
        parent.get().add(child);
    }

    @Override
    protected Division addHeadingLine(ParaHeading line) {
        return findParent(DivisionSecChapter.class).get().addLine(line, line.getLineStyle());
    }

    @Override
    protected Division addOutlineLine(ParaHeading line) {
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
        throw new IllegalStateException("Division not found."); //$NON-NLS-1$
    }

    /**
     * Get the chapter of this scene
     *
     * @return the chapter
     */
    public DivisionSecChapter getChapter() {
        return findParent(DivisionSecChapter.class).get();
    }
}
