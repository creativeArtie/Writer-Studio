package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.*;

/** Text with {@link BasicTextEscape escape characters}.
 *
 * Dec 27,2017: it was decided that subclassing of this class will <b>not</b>
 * do any local reparsing, because it is deem to be too much work.
 */
interface BasicText{

    /** Get a list of children {@link Span} and implemented by
     * {@link SpanNode#delegate()}.
     *
     * If classes can have multiple inheritance, this method would not have been
     * existed.
     *
     * @return answer
     * @see #getRendered()
     */
    public List<Span> delegate();

    /** Get the text with space collapsed, and escape character removed.
     *
     * Therefore "{@code   12\\3  ad  }" ->  "{@code 123  ad}".
     *
     * @return answer
     * @see #getTrimmed() but also trimed
     * @see Span#getRaw() all text verison
     */
    public default String getRendered(){
        StringBuilder builder = new StringBuilder();
        for (Span child: delegate()){
            if (child instanceof BasicTextEscape){
                builder.append(((BasicTextEscape)child).getEscape());
            } else if (child instanceof SpanLeaf){
                /// Add text from a basic span
                builder.append(child.getRaw());
            } else {
                assert false: child.getClass();
            }
        }
        return CharMatcher.whitespace().collapseFrom(builder, ' ');
    }

    /** Get text from {@link #getRendered()}, but also trimmed.
     *
     * Therefore "{@code   12\\3  ad  }" ->  "{@code 123 ad}".
     *
     * @return answer
     * @see #getRendered() also collapse space version
     * @see Span#getRaw() all text verison
     */
    public default String getTrimmed(){
        return CharMatcher.whitespace().trimFrom(getRendered());
    }

    /** Check if the text starts with a whitespace.
     * @return answer
     */
    public default boolean isSpaceBegin(){
        String output = getRendered();
        if (output.isEmpty()){
            return false;
        }
        return CharMatcher.whitespace().matches(output.charAt(0));
    }

    /** Check if the text ends with a whitespace.
     *
     * @return answer
     */
    public default boolean isSpaceEnd(){
        String output = getRendered();
        if (output.isEmpty()){
            return false;
        }
        return CharMatcher.whitespace()
            .matches(output.charAt(output.length() - 1));
    }

}
