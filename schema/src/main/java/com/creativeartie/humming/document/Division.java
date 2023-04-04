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
}
