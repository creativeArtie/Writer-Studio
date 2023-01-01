package com.creativeartie.humming.document;

import java.util.*;
import java.util.regex.*;

import com.creativeartie.humming.schema.*;
import com.google.common.base.*;
import com.google.common.collect.*;

public class IdentitySpan extends IdentityBase {
    private List<String> idCategories;
    private String idName;
    private boolean isPointer;
    private IdentityGroup idGroup;
    private IdentityHolder parentSpan;

    public interface IdentityHolder {
        public int getIdPosition();
    }

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
            TextSpan id = TextSpan.newId(span, name);
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
        span.getRoot().addId(span);
        return span;
    }

    private IdentitySpan(SpanBranch parent, String text, IdentityGroup group, boolean isPtr) {
        super(parent, StyleClasses.ID);
        Preconditions.checkArgument(parent instanceof IdentityHolder);
        isPointer = isPtr;
        idGroup = group;
        parentSpan = (IdentityHolder) parent;
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
    public boolean cleanUpSelf() {
        return getRoot().isIdUnique(this) ? removeStyle(StyleClasses.ERROR) : addStyle(StyleClasses.ERROR);
    }

    @Override
    public int getPosition() {
        return parentSpan.getIdPosition();
    }

    @Override
    public boolean isPointer() {
        return isPointer;
    }
}
