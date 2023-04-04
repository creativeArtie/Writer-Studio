package com.creativeartie.humming.document;

/**
 * A table with row and columns.
 */
public final class DivisionTable extends Division {
    private int maxTableColumns;

    DivisionTable(SpanBranch parent) {
        super(parent);
    }

    @Override
    protected Division addLine(Para line, CssLineStyles style) {
        if (style == CssLineStyles.ROW) {
            ParaTableRow row = (ParaTableRow) line;
            if (row.getColumnSize() > maxTableColumns) maxTableColumns = row.getColumnSize();
            row.addStyle(size() == 0 ? CssSpanStyles.HEADCELL : CssSpanStyles.TEXTCELL);
            add(row);
            return this;
        }
        return findParent(Division.class).get().addLine(line, style);
    }

    /**
     * Get the number of columns in the table
     *
     * @return the number of columns
     */
    public int getColumnSize() {
        return maxTableColumns;
    }
}
