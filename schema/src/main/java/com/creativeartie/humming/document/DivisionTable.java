package com.creativeartie.humming.document;

public class DivisionTable extends Division {
    private int maxTableColumns;

    public DivisionTable(SpanBranch parent) {
        super(parent);
    }

    @Override
    protected Division addLine(Para line, StyleLines style) {
        if (style == StyleLines.ROW) {
            ParaTableRow row = (ParaTableRow) line;
            if (row.getColumnSize() > maxTableColumns) maxTableColumns = row.getColumnSize();
            row.addStyle(size() == 0 ? StylesSpans.HEADCELL : StylesSpans.TEXTCELL);
            add(row);
            return this;
        }
        return findParent(Division.class).get();
    }

    public int getColumnSize() {
        return maxTableColumns;
    }
}
