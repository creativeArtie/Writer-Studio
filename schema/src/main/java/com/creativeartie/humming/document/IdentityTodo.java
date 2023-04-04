package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * In line agenda span.
 *
 * @see IdentityTodoPattern Pattern version
 */
public final class IdentityTodo extends SpanBranch implements IdentityStorage.Identity {
    static IdentityTodo newSpan(SpanBranch parent, String text, CssSpanStyles... classes) {
        IdentityTodo span = new IdentityTodo(parent, classes);
        Matcher matcher = IdentityTodoPattern.matcher(text);

        String raw = IdentityTodoPattern.START.group(matcher);
        span.add(new SpanLeaf(span, raw));
        if ((raw = IdentityTodoPattern.TEXT.group(matcher)) != null) {
            TextSpan test = TextSpan.newSpecial(span, raw, classes);
            span.add(test);
            span.todoText = test.getText();
        }
        if ((raw = IdentityTodoPattern.END.group(matcher)) != null) span.add(new SpanLeaf(span, raw));
        return span;
    }

    private String todoText;

    private IdentityTodo(SpanBranch parent, CssSpanStyles... classes) {
        super(parent, classes);
        addStyle(CssSpanStyles.AGENDA);
        todoText = "";
    }

    /**
     * Get the agenda text
     *
     * @return the text
     */
    public String getAgenda() {
        return CharMatcher.whitespace().trimAndCollapseFrom(todoText, ' ');
    }

    @Override
    public List<String> getCategories() {
        return Lists.newArrayList(getRoot().getFileName(), "phrase");
    }

    @Override
    public String getId() {
        return Integer.toString(getStartIndex());
    }

    @Override
    public IdentityGroup getIdGroup() {
        return IdentityGroup.TODO;
    }

    @Override
    public int getPosition() {
        return getStartIndex();
    }

    @Override
    public boolean isPointer() {
        return true;
    }
}
