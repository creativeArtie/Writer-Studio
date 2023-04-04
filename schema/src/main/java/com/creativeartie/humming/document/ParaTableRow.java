package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

/**
 * A table row.
 */
public final class ParaTableRow extends Para {
    static ParaTableRow newLine(SpanBranch parent) {
        return new ParaTableRow(parent);
    }

    private int numberOfColumns;

    private ParaTableRow(SpanBranch parent) {
        super(parent, CssLineStyles.ROW);
        numberOfColumns = 0;
    }

    /**
     * get the number of columns of a row
     *
     * @return number columns
     */
    public int getColumnSize() {
        return numberOfColumns;
    }

    @Override
    protected void buildSpan(Matcher match) {

        while (match.find()) {
            String sep = ParaTableRowPattern.SEP.group(match);

            if (sep != null) {
                add(new SpanLeaf(this, sep));
                add(TextFormatted.newCellText(this, ParaTableRowPattern.TEXT.group(match)));
                numberOfColumns++;
                continue;
            }
            String end = ParaTableRowPattern.END.group(match);

            if (end != null) {
                add(new SpanLeaf(this, end));
                return;
            }
        }
    }
}
