package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

public class IdentityTodo extends SpanBranch implements IdentityStorage.Identity {
    public static IdentityTodo newSpan(SpanBranch parent, String text, StylesSpans... classes) {
        IdentityTodo span = new IdentityTodo(parent, classes);
        Matcher matcher = IdentityTodoPattern.matcher(text);

        String raw = IdentityTodoPattern.START.group(matcher);
        span.add(new SpanLeaf(span, raw));
        if ((raw = IdentityTodoPattern.TEXT.group(matcher)) != null) {
            TextSpan test = TextSpan.newSpecial(span, raw, classes);
            span.add(test);
            span.todoText = test.getText();
        }
        if ((raw = IdentityTodoPattern.END.group(matcher)) != null) {
            span.add(new SpanLeaf(span, raw));
        }
        return span;
    }

    private String todoText;

    private IdentityTodo(SpanBranch parent, StylesSpans... classes) {
        super(parent, classes);
        addStyle(StylesSpans.TODO);
        todoText = "";
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

    public String getAgenda() {
        return CharMatcher.whitespace().trimAndCollapseFrom(todoText, ' ');
    }
}
