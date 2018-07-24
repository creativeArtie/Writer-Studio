package com.creativeartie.writerstudio.export;

import java.util.*;

interface ContentPolicySplit<T extends Number> {

    /** Split the content so that the first half fits a division.
     *
     * @param width
     *      the width to fill from
     * @param from
     *      the content to fit
     * @return a list of 2 item (fitted content and overflow)
     * @see Content#split
     */
    public List<Content<T>> splitContent(T width, Content<T> from);
}
