package com.creativeartie.humming.document;

/**
 * A document division.
 */
public abstract class Division extends SpanBranch {
    Division(Manuscript root) {
        super(root);
    }

    Division(SpanBranch parent) {
        super(parent);
    }

    /**
     * Adds a line with its style. The style should be the same as
     * {@link Para#getLineStyle()}.
     *
     * @param line
     *        the line to add
     * @param style
     *        the line style
     *
     * @return the division to add the next line in
     */
    protected abstract Division addLine(Para line, CssLineStyles style);

    /**
     * Gets the outline word count
     *
     * @return the current word count
     */
    public int getOutlineCount() {
        int count = 0;
        for (Span child : this) {
            if (child instanceof Division) {
                count += ((Division) child).getOutlineCount();
            } else if (child instanceof Para) {
                count += ((Para) child).getOutlineCount();
            }
        }
        return count;
    }

    /**
     * Gets the writing word count
     *
     * @return the current word count
     */
    public int getWritingCount() {
        int count = 0;
        for (Span child : this) {
            if (child instanceof Division) {
                count += ((Division) child).getWritingCount();
            } else if (child instanceof Para) {
                count += ((Para) child).getWrittenCount();
            }
        }
        return count;
    }
}
