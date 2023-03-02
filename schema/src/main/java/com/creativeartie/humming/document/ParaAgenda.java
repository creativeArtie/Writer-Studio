package com.creativeartie.humming.document;

import java.util.Optional;
import java.util.regex.*;

import com.creativeartie.humming.document.IdentitySpan.*;
import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

public class ParaAgenda extends Para implements IdentityParent {
    private String todoText;

    protected ParaAgenda(SpanBranch parent) {
        super(parent, StyleLines.AGENDA);
        todoText = "";
    }

    @Override
    public int getIdPosition() {
        return getStartIndex();
    }

    @Override
    protected void buildSpan(Matcher match) {
        String raw = ParaBasicPatterns.LineSpanParts.TODOER.group(match);
        add(new SpanLeaf(this, raw));
        if ((raw = ParaBasicPatterns.LineSpanParts.TEXT.group(match)) != null) {
            TextSpan child = TextSpan.newSimple(this, raw);
            add(child);
            todoText = child.getText();
        }
        addLineEnd(match, ParaBasicPatterns.LineSpanParts.ENDER);
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
