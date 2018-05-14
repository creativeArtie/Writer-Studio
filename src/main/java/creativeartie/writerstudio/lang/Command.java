package com.creativeartie.writerstudio.lang;

import java.util.*;

/** An interface to replace text of a {@link SpanBranch}. */
public interface Command{

    /** Parse and get the result string.
     *
     * @return answer; null or empty to delete.
     */
    public String getResult();
}