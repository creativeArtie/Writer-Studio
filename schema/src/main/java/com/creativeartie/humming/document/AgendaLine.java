package com.creativeartie.humming.document;

import java.util.Optional;
import java.util.regex.*;

import com.creativeartie.humming.document.IdentitySpan.*;
import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

public class AgendaLine extends LineSpan implements IdentityParent {
    private String todoText;

    protected AgendaLine(SpanBranch parent) {
        super(parent, LineStyles.AGENDA);
        todoText = "";
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw = LineSpanPatterns.LineSpanParts.TODOER.group(match);
        add(new SpanLeaf(this, raw));
        if ((raw = LineSpanPatterns.LineSpanParts.TEXT.group(match)) != null) {
            TextSpan child = TextSpan.newSimple(this, raw);
            add(child);
            todoText = child.getText();
        }
        addLineEnd(match, LineSpanPatterns.LineSpanParts.ENDER);
    }

    public String getAgenda() {
        return CharMatcher.whitespace().trimAndCollapseFrom(todoText, ' ');
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        // TODO Auto-generated method stub
        return null;
    }
}