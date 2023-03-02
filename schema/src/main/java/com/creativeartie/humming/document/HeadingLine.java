package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

public class HeadingLine extends Para {
    static HeadingLine newLine(SpanBranch parent, Matcher match) {
        if (ParaHeadingPattern.OUTLINE.group(match) == null) {
            return new HeadingLine(parent, StyleLines.HEADING);
        }
        return new HeadingLine(parent, StyleLines.OUTLINE);
    }

    private HeadingLine(SpanBranch parent, StyleLines style) {
        super(parent, style);
    }

    private Status draftStatus;
    private int headingLevel;

    public enum DraftStatus {
        NONE, STUB, OUTLINE, DRAFT, FINAL, OTHERS;
    }

    private class Status {
        private final DraftStatus currentStatus;
        private final String statusDetail;

        private Status(Matcher matcher) {
            String raw = ParaHeadingPattern.STATUS.group(matcher);
            ParaHeadingPattern.StatusPattern status = ParaHeadingPattern.StatusPattern.getStatus(raw);
            switch (status) {
                case DRAFT:
                    currentStatus = DraftStatus.DRAFT;
                    break;
                case FINAL:
                    currentStatus = DraftStatus.FINAL;
                    break;
                case OTHERS:
                    currentStatus = DraftStatus.OTHERS;
                    break;
                case OUTLINE:
                    currentStatus = DraftStatus.OUTLINE;
                    break;
                case STUB:
                    currentStatus = DraftStatus.STUB;
                    break;
                default:
                    assert false : "new Status(Matcher) called without status";
                    currentStatus = DraftStatus.NONE;
            }
            add(new SpanLeaf(HeadingLine.this, raw));
            if ((raw = ParaHeadingPattern.DETAILS.group(matcher)) != null) {
                TextSpan detail = TextSpan.newSimple(HeadingLine.this, raw);
                add(detail);
                statusDetail = detail.getText();
            } else statusDetail = "";
        }

        private Status() {
            currentStatus = DraftStatus.NONE;
            statusDetail = "";
        }
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw;
        if ((raw = ParaHeadingPattern.OUTLINE.group(match)) != null) {
            add(new SpanLeaf(this, raw));
        }
        if ((raw = ParaHeadingPattern.LEVEL.group(match)) != null) {
            add(new SpanLeaf(this, raw));
            headingLevel = raw.length();
        }
        if ((raw = ParaHeadingPattern.TEXT.group(match)) != null) {
            add(TextFormatted.newHeadingText(this, raw));
        }
        if ((raw = ParaHeadingPattern.STATUS.group(match)) != null) {
            draftStatus = new Status(match);
        } else {
            draftStatus = new Status();
        }
        addLineEnd(match, ParaHeadingPattern.ENDER);
    }

    public DraftStatus getStatus() {
        return draftStatus.currentStatus;
    }

    public String getDetail() {
        return CharMatcher.whitespace().trimAndCollapseFrom(draftStatus.statusDetail, ' ');
    }

    public int getLevel() {
        return headingLevel;
    }
}
