package com.creativeartie.writer.lang;

/** An interface to replace text of a {@link SpanBranch}. */
public interface Command{

    /** Parse and get the result string.
     *
     * @return answer; null or empty to delete.
     */
    public String getResult();
}