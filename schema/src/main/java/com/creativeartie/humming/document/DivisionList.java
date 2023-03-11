package com.creativeartie.humming.document;

public class DivisionList extends Division implements SpanList {
    private final boolean isBullet;
    private final int listLevel;
    private int listPosition;
    private int listSize;

    public DivisionList(SpanBranch spanParent, ParaList line) {
        super(spanParent);
        listLevel = 1;
        isBullet = line.getLineStyle() == StyleLines.BULLET;
        listPosition = 1;
        listSize = 1;
    }

    private DivisionList(SpanBranch parent, ParaList line, int level) {
        super(parent);
        listLevel = level;
        isBullet = line.getLineStyle() == StyleLines.BULLET;
        listPosition = 1;
        listSize = 1;
    }

    protected Division addLine(ParaList line) {
        int level = line.getLevel();

        if (level == listLevel) {

            if (line.getLineStyle() == StyleLines.BULLET ? isBullet : !isBullet) {
                // same type + same level = add to this
                add(line);
                line.setPosition(listSize++);
                return this;
            } else {
                // different type + same level = go to SectionDivision + new list
                DivisionSec parent = findParent(DivisionSecChapter.class).get();
                DivisionList newList = new DivisionList(parent, line);
                parent.add(newList);
                return newList.addLine(line);
            }
        } else if (level > listLevel) {
            // deeper level = create child
            DivisionList child = new DivisionList(this, line, listLevel + 1);
            add(child);
            child.listPosition = listSize - 1;
            return child.addLine(line);
        }
        // shadower level = go back to parent
        return findParent(DivisionList.class).get().addLine(line);
    }

    @Override
    protected Division addLine(Para line, StyleLines style) {

        switch (style) {
            case AGENDA:
                add(line);
                return this;
            case BULLET:
            case NUMBERED:
                return addLine((ParaList) line);
            default:
                return findParent(DivisionSecChapter.class).get().addLine(line, style);
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
