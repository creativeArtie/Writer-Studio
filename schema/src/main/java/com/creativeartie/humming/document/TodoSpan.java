package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

public class TodoSpan extends SpanBranch implements IdentityStorage.Identity {
    public static TodoSpan newSpan(SpanBranch parent, String text, SpanStyles... classes) {
        TodoSpan span = new TodoSpan(parent, classes);
        Matcher matcher = TodoPattern.matcher(text);

        String raw = TodoPattern.START.group(matcher);
        span.add(new SpanLeaf(span, raw));
        if ((raw = TodoPattern.TEXT.group(matcher)) != null) {
            TextSpan test = TextSpan.newSpecial(span, raw, classes);
            span.add(test);
            span.todoText = test.getText();
        }
        if ((raw = TodoPattern.END.group(matcher)) != null) {
            span.add(new SpanLeaf(span, raw));
        }
        return span;
    }

    private String todoText;

    private TodoSpan(SpanBranch parent, SpanStyles... classes) {
        super(parent, classes);
        addStyle(SpanStyles.TODO);
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
