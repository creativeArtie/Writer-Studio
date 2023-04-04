package com.creativeartie.humming.document;

import java.util.Optional;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;

/**
 * An agenda Paragraph
 */
public final class ParaAgenda extends Para implements IdentityParent {
    private String todoText;

    ParaAgenda(SpanBranch parent) {
        super(parent, CssLineStyles.AGENDA);
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

    /**
     * Gets the agenda text.
     *
     * @return the agenda text
     */
    public String getAgenda() {
        return CharMatcher.whitespace().trimAndCollapseFrom(todoText, ' ');
    }

    @Override
    public Optional<IdentitySpan> getPointer() {
        // TODO Set a identity address here
        return Optional.empty();
    }
}
