package com.creativeartie.humming.document;

import java.util.*;

public abstract class Division extends SpanBranch {
    protected Division(Document root) {
        super(root);
    }

    protected Division(SpanBranch parent) {
        super(parent);
    }

    protected abstract Optional<Division> addLine(LineSpan line, LineStyles style);

    protected void setParent(Document document) {}
}
