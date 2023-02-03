package com.creativeartie.humming.document;

import java.util.*;

public class ListDivision extends Division {
    private final boolean isBullet;
    private final int listLevel;

    public ListDivision(SpanBranch parent, ListLine line) {
        super(parent);
        listLevel = 1;
        isBullet = line.getLineStyle() == LineStyles.BULLET;
    }

    private ListDivision(SpanBranch parent, ListLine line, int level) {
        super(parent);
        listLevel = level;
        isBullet = line.getLineStyle() == LineStyles.BULLET;
    }

    protected Optional<Division> addLine(ListLine line) {
        int level = line.getLevel();
        if (level == listLevel) {
            if (line.getLineStyle() == LineStyles.BULLET ? isBullet : !isBullet) {
                // same type + same level = add to this
                add(line);
                return Optional.of(this);
            } else {
                // different type + same level = go to SectionDivision + new list
                Optional<SpanBranch> parent = getParent();
                assert parent.isPresent();
                while (parent.get() instanceof ListDivision) {
                    parent = parent.get().getParent();
                }
                ListDivision newList = new ListDivision(parent.get(), line);
                return newList.addLine(line);
            }
        } else if (level > listLevel) {
            // deeper level = create child
            ListDivision child = new ListDivision(this, line, listLevel + 1);
            if (child.listLevel == line.getLevel()) child.add(line);
            else return child.addLine(line);
            return Optional.of(child);
        }
        // shadower level = go back to parent
        Optional<SpanBranch> parent = getParent();
        assert parent.isPresent();
        if (parent.get() instanceof ListDivision) {
            return ((ListDivision) parent.get()).addLine(line);
        }

        return Optional.empty();
    }

    @Override
    protected Optional<Division> addLine(LineSpan line, LineStyles style) {
        switch (style) {
            case AGENDA:
                add(line);
                return Optional.empty();
            case BULLET:
            case NUMBERED:
                return addLine((ListLine) line);
            default:
                return Optional.of((Division) getParent().get());
        }
    }

    public boolean isBullet() {
        return isBullet;
    }

    public int getListLevel() {
        return listLevel;
    }
}
