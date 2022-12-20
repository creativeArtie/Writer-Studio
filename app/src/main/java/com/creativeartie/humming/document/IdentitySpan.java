package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class IdentitySpan extends SpanBranch {
    private List<String> idCategories;
    private String idName;

    protected IdentitySpan(Document root, SpanBranch parent, String text) {
        super(parent);
        Matcher matcher = IdentityPattern.matcher(text);
        String find = IdentityPattern.NAME.group(matcher);
        if (find != null) {
            SpanText name = new SpanText(this, BasicTextPatterns.ID, find);
            add(name);
        }
    }
}
