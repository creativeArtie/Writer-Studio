package com.creativeartie.humming.document;

import java.util.*;

public interface Span {
    Document getRoot();

    Optional<SpanBranch> getParent();

    public boolean cleanUp();

    public default int getStartIndex() {
        return getRoot().getCacheStart(this);
    }

    public int getLength();

    default int getEndIndex() {
        return getRoot().getCacheEnd(this);
    }
}
