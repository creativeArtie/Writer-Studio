package com.creativeartie.writerstudio.export;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

interface ContentPolicyFont<T extends Number>
    extends ContentPolicySplit<T>
{

    @Override
    public default List<Content<T>> splitContent(T width, Content<T> from){
        return splitContent(width,
            argumentClass(from, "from", ContentChildText.class).getText()
        );
    }

    public List<Content<T>> splitContent(T width, String text);

    public T getWidth(String text);

    public T getHeight(String text);
}
