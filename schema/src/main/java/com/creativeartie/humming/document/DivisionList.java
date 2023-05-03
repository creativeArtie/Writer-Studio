package com.creativeartie.humming.document;

/**
 * Division for a list.
 */
public final class DivisionList extends Division implements SpanList {
    private final boolean isBullet;
    private final int listLevel;
    private int listPosition;
    private int listSize;

    DivisionList(SpanBranch spanParent, ParaList line) {
        super(spanParent);
        listLevel = 1;
        isBullet = line.getLineStyle() == CssLineStyles.BULLET;
        listPosition = 1;
        listSize = 1;
    }

    private DivisionList(SpanBranch parent, ParaList line, int level) {
        super(parent);
        listLevel = level;
        isBullet = line.getLineStyle() == CssLineStyles.BULLET;
        listPosition = 1;
        listSize = 1;
    }

    @Override
    public int getLevel() {
        return listLevel;
    }

    @Override
    public int getPosition() {
        return listPosition;
    }

    @Override
    public boolean isBullet() {
        return isBullet;
    }

    @Override
    protected Division addLine(Para line, CssLineStyles style) {
        switch (style) {
            case AGENDA:
                add(line);
                return this;
            case BULLET:
            case NUMBERED:
                return addLine((ParaList) line);
            default:
                return findParent(DivisionSec.class).get().addLine(line, style);
        }
    }

    /**
     * Add a new bullet/number list paragraph. This might change on different
     * situations
     *
     * @param line
     *        the line add
     *
     * @return result for {@link #addLine(Para, CssLineStyles)}
     */
    protected Division addLine(ParaList line) {
        int level = line.getLevel();

        if (level == listLevel) {

            if (line.getLineStyle() == CssLineStyles.BULLET ? isBullet : !isBullet) {
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
}
