package com.creativeartie.jwriter.lang;

import com.google.common.base.*;

import com.creativeartie.jwriter.main.*;

/**
 * Detail style and status about a {@link SpanBranch}.
 */
public interface DetailStyle{

    /**
     * Returns a name possibly always from {@linkplain Enum#name}.
     */
    public String name();
}
