package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class TableRow extends LineSpan {
    protected static TableRow newLine(SpanBranch parent) {
        return new TableRow(parent);
    }

    private TableRow(SpanBranch parent) {
        super(parent, LineStyles.ROW);
    }

    @Override
    protected void buildSpan(Matcher match) {

        while (match.find()) {
            String sep = TableRowPattern.SEP.group(match);

            if (sep != null) {
                add(new SpanLeaf(this, sep));
                add(LineText.newCellText(this, TableRowPattern.TEXT.group(match)));
                continue;
            }
            String end = TableRowPattern.END.group(match);

            if (end != null) {
                add(new SpanLeaf(this, end));
                return;
            }
        }
    }
}
