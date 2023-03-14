package com.creativeartie.humming.document;

public abstract class Division extends SpanBranch {
    protected Division(Manuscript root) {
        super(root);
    }

    protected Division(SpanBranch parent) {
        super(parent);
    }

    protected abstract Division addLine(Para line, StyleLines style);
}
