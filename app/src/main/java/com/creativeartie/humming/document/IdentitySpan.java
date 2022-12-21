package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;
import com.google.common.collect.*;

public class IdentitySpan extends SpanBranch {
    public enum IdGroup {
        FOOTNOTE, ENDNOTE
    }

    private List<String> idCategories;
    private String idName;
    private boolean isAnId;
    private IdGroup idGroup;

    protected IdentitySpan(SpanBranch parent, String text, IdGroup group, boolean isId) {
        super(parent, StyleClasses.ID);
        Matcher matcher = IdentityPattern.matcher(text);
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        String name = "";
        while (matcher.find()) {
            name = IdentityPattern.NAME.group(matcher);
            SpanText id = new SpanText(this, BasicTextPatterns.ID, name);
            name = id.getText();
            add(id);
            if (matcher.find()) {
                String sep = IdentityPattern.SEP.group(matcher);
                builder.add(name);
                add(new SpanLeaf(this, sep.length(), StyleClasses.OPERATOR));
            }
        }
        idCategories = builder.build();
        idName = name;
        isAnId = isId;
        idGroup = group;
        if (isAnId) getRoot().putId(this);
    }

    public List<String> getCategories() {
        return idCategories;
    }

    public String getId() {
        return idName;
    }

    public String getFullId() {
        if (idCategories.isEmpty()) return idName;

        return Joiner.on(":").join(idCategories) + ":" + idName;
    }

    public String getInternalId() {
        return idGroup.name() + ":" + getFullId();
    }

    @Override
    public boolean cleanUpSelf() {
        return getRoot().isCorrect(this) ? removeStyle(StyleClasses.ERROR) : addStyle(StyleClasses.ERROR);
    }
}
