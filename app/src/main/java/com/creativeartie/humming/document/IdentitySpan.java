package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;
import com.google.common.collect.*;

public class IdentitySpan extends SpanBranch implements Identity {
    private List<String> idCategories;
    private String idName;
    private boolean isPointer;
    private IdentityGroup idGroup;

    public static IdentitySpan newPointerId(SpanBranch parent, String text, IdentityGroup group) {
        IdentitySpan span = new IdentitySpan(parent, text, group, true);
        return parseText(span, text);
    }

    public static IdentitySpan newAddressId(SpanBranch parent, String text, IdentityGroup group) {
        IdentitySpan span = new IdentitySpan(parent, text, group, false);
        return parseText(span, text);
    }

    private static IdentitySpan parseText(IdentitySpan span, String text) {
        Matcher matcher = IdentityPattern.matcher(text);
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        String name = "";
        while (matcher.find()) {
            name = IdentityPattern.NAME.group(matcher);
            SpanText id = SpanText.newId(span, name);
            name = id.getText();
            span.add(id);
            if (matcher.find()) {
                String sep = IdentityPattern.SEP.group(matcher);
                builder.add(name);
                span.add(new SpanLeaf(span, sep.length()));
            }
        }
        span.idCategories = builder.build();
        span.idName = name;
        if (!span.isPointer) span.getRoot().putId(span);
        return span;
    }

    private IdentitySpan(SpanBranch parent, String text, IdentityGroup group, boolean isPtr) {
        super(parent, StyleClasses.ID);
        isPointer = isPtr;
        idGroup = group;
    }

    @Override
    public IdentityGroup getIdGroup() {
        return idGroup;
    }

    @Override
    public List<String> getCategories() {
        return idCategories;
    }

    @Override
    public String getId() {
        return idName;
    }

    @Override
    public String getFullId() {
        if (idCategories.isEmpty()) return idName;

        return Joiner.on(":").join(idCategories) + ":" + idName;
    }

    @Override
    public String getInternalId() {
        return idGroup.name() + ":" + getFullId();
    }

    @Override
    public boolean cleanUpSelf() {
        return getRoot().isCorrect(this) ? removeStyle(StyleClasses.ERROR) : addStyle(StyleClasses.ERROR);
    }
}
