package com.creativeartie.writerstudio.export;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

abstract class Content<T extends Number, U extends SpanBranch> {

    private final ContentPolicySplit<T> contentSplitter;
    private final U renderSpan;

    Content(ContentPolicySplit<T> splitter, SpanBranch span){
        contentSplitter = argumentNotNull(splitter, "splitter");
        renderSpan = argumentNotNull(span, "span");
    }

    /** Fit and split the text to a width
     *
     * @param width to fit
     * @see ContentPolicySplit
     * @see Division#addContent(Content)
     */
    final List<Content<T>> split(T width){
        return contentSplitter.splitContent(width, this);
    }

    abstract T getWidth();

    abstract T getHeight();

    abstract T getFillHeight();

    abstract boolean merge(Content<T> content);
}
