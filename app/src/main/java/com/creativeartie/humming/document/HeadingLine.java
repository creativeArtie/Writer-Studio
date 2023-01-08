package com.creativeartie.humming.document;

import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class HeadingLine extends LineSpan {
    static HeadingLine newLine(SpanBranch parent, Matcher match) {
        if (HeadingLinePattern.OUTLINE.group(match) == null) {
            return new HeadingLine(parent, LineStyles.HEADING);
        }
        return new HeadingLine(parent, LineStyles.OUTLINE);
    }

    private HeadingLine(SpanBranch parent, LineStyles style) {
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
            String raw = HeadingLinePattern.STATUS.group(matcher);
            HeadingLinePattern.StatusPattern status = HeadingLinePattern.StatusPattern.getStatus(raw);
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
            if ((raw = HeadingLinePattern.DETAILS.group(matcher)) != null) {
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
        if ((raw = HeadingLinePattern.OUTLINE.group(match)) != null) {
            add(new SpanLeaf(this, raw));
        }
        if ((raw = HeadingLinePattern.LEVEL.group(match)) != null) {
            add(new SpanLeaf(this, raw));
            headingLevel = raw.length();
        }
        if ((raw = HeadingLinePattern.TEXT.group(match)) != null) {
            add(LineText.newHeadingText(this, raw));
        }
        if ((raw = HeadingLinePattern.STATUS.group(match)) != null) {
            draftStatus = new Status(match);
        } else {
            draftStatus = new Status();
        }
        addLineEnd(match, HeadingLinePattern.ENDER);
    }

    public DraftStatus getStatus() {
        return draftStatus.currentStatus;
    }

    public String getDetail() {
        return draftStatus.statusDetail.trim();
    }

    public int getLevel() {
        return headingLevel;
    }
}
