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
    private int writtenCount, outlineCount;

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
        writtenCount = 0;
        outlineCount = 0;
        while (match.find()) {
            if (SpanLeaf.addLeaf(this, ParaTableRowPattern.SEP.group(match)).isPresent()) {
                TextFormatted text = TextFormatted.newCellText(this, ParaTableRowPattern.TEXT.group(match));
                add(text);
                writtenCount += text.getWrittenCount();
                outlineCount += text.getOutlineCount();
                numberOfColumns++;
                continue;
            }

            if (SpanLeaf.addLeaf(this, ParaTableRowPattern.END.group(match)).isPresent()) {
                return;
            }
        }
    }

    @Override
    public int getOutlineCount() {
        return outlineCount;
    }

    @Override
    public int getWrittenCount() {
        return writtenCount;
    }
}
