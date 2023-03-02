package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class ParaTableRow extends Para {
    protected static ParaTableRow newLine(SpanBranch parent) {
        return new ParaTableRow(parent);
    }

    private ParaTableRow(SpanBranch parent) {
        super(parent, StyleLines.ROW);
    }

    @Override
    protected void buildSpan(Matcher match) {

        while (match.find()) {
            String sep = ParaTableRowPattern.SEP.group(match);

            if (sep != null) {
                add(new SpanLeaf(this, sep));
                add(TextFormatted.newCellText(this, ParaTableRowPattern.TEXT.group(match)));
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
