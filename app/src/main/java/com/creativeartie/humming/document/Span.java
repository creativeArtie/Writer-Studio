package com.creativeartie.humming.document;

import java.util.*;
import java.util.concurrent.*;

public interface Span {
    Document getRoot();

    Optional<SpanBranch> getParent();

    public boolean cleanUp();

    public default int getStartIndex() throws ExecutionException {
        return getRoot().getCacheStart(this);
    }

    public int getLength() throws ExecutionException;

    default int getEndIndex() throws ExecutionException {
        return getRoot().getCacheEnd(this);
    }
}
