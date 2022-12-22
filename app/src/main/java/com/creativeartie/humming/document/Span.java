package com.creativeartie.humming.document;

import java.util.*;

public interface Span {
    Document getRoot();

    Optional<SpanBranch> getParent();

    public boolean cleanUp();
}
