package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.creativeartie.humming.schema.TableCellPatterns.*;

public class TableCell extends SpanBranch {
    public static TableCell newHeadingCell(SpanBranch parent, String text) {
        Matcher match = TableCellPatterns.SUBHEAD.matcher(text);
        if (match == null) return null;
        TableCell span = new TableCell(parent, true, SpanStyles.HEADCELL);

        return parseText(match, span);
    }

    public static TableCell newTextCell(SpanBranch parent, String text) {
        Matcher match = TableCellPatterns.TEXT.matcher(text);
        if (match == null) return null;
        TableCell span = new TableCell(parent, false, SpanStyles.TEXTCELL);

        return parseText(match, span);
    }

    private static TableCell parseText(Matcher match, TableCell span) {
        span.add(new SpanLeaf(span, TableCellParts.START.group(match)));

        String raw = null;
        if ((raw = TableCellParts.COLS.group(match)) != null) {
            int colSpan = raw.length();
            span.add(new SpanLeaf(span, colSpan));
            span.colsSpan += colSpan;
        }
        if ((raw = TableCellParts.ROWS.group(match)) != null) {
            int rowSpan = raw.length();
            span.add(new SpanLeaf(span, rowSpan));
            span.rowsSpan += rowSpan;
        }

        span.add(LineText.newCellText(span, TableCellParts.TEXT.group(match)));

        return span;
    }

    private boolean isHeading;
    private int colsSpan;
    private int rowsSpan;

    public TableCell(SpanBranch parent, boolean isHead, SpanStyles style) {
        super(parent, style);
        isHeading = isHead;
        colsSpan = 1;
        rowsSpan = 1;
    }

    public int getColsSpan() {
        return colsSpan;
    }

    public int getRowsSpan() {
        return rowsSpan;
    }

    public boolean isHead() {
        return isHeading;
    }

    public boolean isText() {
        return !isHeading;
    }
}
