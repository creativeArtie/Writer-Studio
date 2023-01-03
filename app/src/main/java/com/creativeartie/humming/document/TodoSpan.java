package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;

public class TodoSpan extends IdentityBase {
    public static TodoSpan newSpan(SpanBranch parent, String text, StyleClasses... classes) {
        TodoSpan span = new TodoSpan(parent, classes);
        Matcher matcher = TodoPattern.matcher(text);

        String raw = TodoPattern.START.group(matcher);
        span.add(new SpanLeaf(span, raw.length()));
        if ((raw = TodoPattern.TEXT.group(matcher)) != null) {
            TextSpan test = TextSpan.builder(span).setPattern(BasicTextPatterns.ID).build(raw);
            span.add(test);
            span.todoText = test.getText();
        }
        if ((raw = TodoPattern.END.group(matcher)) != null) {
            span.add(new SpanLeaf(span, raw.length()));
        }
        return span;
    }

    private String todoText;

    private TodoSpan(SpanBranch parent, StyleClasses... classes) {
        super(parent, classes);
        addStyle(StyleClasses.TODO);
    }

    @Override
    public IdentityGroup getIdGroup() {
        return IdentityGroup.TODO;
    }

    @Override
    public List<String> getCategories() {
        return new ArrayList<>();
    }

    @Override
    public String getId() {
        return Integer.toString(getStartIndex());
    }

    @Override
    public boolean isPointer() {
        return true;
    }

    @Override
    public int getPosition() {
        return getStartIndex();
    }

    public String getTodoText() {
        return todoText;
    }
}
