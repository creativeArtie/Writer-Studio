package com.creativeartie.humming.document;

import java.util.*;

public abstract class Division extends SpanBranch {
    protected Division(Document root, StyleClass... classes) {
        super(root, classes);
    }

    protected Division(SpanBranch parent, StyleClass... classes) {
        super(parent, classes);
    }

    protected abstract Optional<Division> addLine(String text);
}
