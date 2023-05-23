package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.files.*;
import com.creativeartie.humming.schema.*;
import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * An agenda Paragraph
 */
public final class ParaAgenda extends Para implements IdentityStorage.Identity {
    private String todoText;

    ParaAgenda(SpanBranch parent) {
        super(parent, CssLineStyles.AGENDA);
        todoText = new String();
    }

    @Override
    protected void buildSpan(Matcher match) {

        SpanLeaf.addLeaf(this, ParaBasicPatterns.LineSpanParts.TODOER.group(match));

        String raw;
        if ((raw = ParaBasicPatterns.LineSpanParts.TEXT.group(match)) != null) {
            TextSpan child = TextSpan.newSimple(this, raw);
            add(child);
            todoText = child.getText();
        }
        SpanLeaf.addLeaf(this, ParaBasicPatterns.LineSpanParts.ENDER.group(match));
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
    public List<String> getCategories() {
        return Lists.newArrayList(Literals.TODO_LINE_ID.getText());
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
        return false;
    }

    @Override
    public int getOutlineCount() {
        return Splitter.on(' ').omitEmptyStrings().trimResults().splitToList(todoText).size();
    }

    @Override
    public int getWrittenCount() {
        return 0;
    }
}
