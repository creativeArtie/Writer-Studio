package com.creativeartie.humming.document;

import java.util.*;

public interface Span {
    Document getRoot();

    Optional<SpanBranch> getParent();

    public boolean cleanUp();

    public default int getStartIndex() {
        return getRoot().getStartIndex(this);
    }

    default int getEndIndex() {
        return getRoot().getEndIndex(this);
    }

    public String getId();
}
