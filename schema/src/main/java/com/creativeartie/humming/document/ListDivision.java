package com.creativeartie.humming.document;

import java.util.*;

public class ListDivision extends Division implements ListSpan {
    private final boolean isBullet;
    private final int listLevel;
    private int listPosition;
    private int listSize;

    public ListDivision(SpanBranch spanParent, ListLine line) {
        super(spanParent);
        listLevel = 1;
        isBullet = line.getLineStyle() == LineStyles.BULLET;
        listPosition = 1;
        listSize = 1;
    }

    private ListDivision(SpanBranch parent, ListLine line, int level) {
        super(parent);
        listLevel = level;
        isBullet = line.getLineStyle() == LineStyles.BULLET;
        listPosition = 1;
        listSize = 1;
    }

    protected Optional<Division> addLine(ListLine line) {
        int level = line.getLevel();

        if (level == listLevel) {

            if (line.getLineStyle() == LineStyles.BULLET ? isBullet : !isBullet) {
                // same type + same level = add to this
                add(line);
                line.setPosition(listSize++);
                return Optional.of(this);
            } else {
                // different type + same level = go to SectionDivision + new list
                SectionDivision parent = findParent(SectionDivision.class).get();
                ListDivision newList = new ListDivision(parent, line);
                parent.add(newList);
                return newList.addLine(line);
            }
        } else if (level > listLevel) {
            // deeper level = create child
            ListDivision child = new ListDivision(this, line, listLevel + 1);
            add(child);
            child.listPosition = listSize - 1;
            return child.addLine(line);
        }
        // shadower level = go back to parent
        return findParent(ListDivision.class).flatMap((span) -> span.addLine(line));
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
                return findParent(SectionDivision.class).map((span) -> (Division) span);
        }
    }

    @Override
    public boolean isBullet() {
        return isBullet;
    }

    @Override
    public int getPosition() {
        return listPosition;
    }

    @Override
    public int getLevel() {
        return listLevel;
    }
}
