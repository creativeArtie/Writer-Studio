package com.creativeartie.humming.document;

import java.util.*;
import java.util.concurrent.*;

import com.google.common.base.*;

public abstract class IdentityBase extends SpanBranch {
    protected IdentityBase(SpanBranch parent, StyleClasses... classes) {
        super(parent, classes);
    }

    public abstract IdentityGroup getIdGroup();

    public abstract List<String> getCategories();

    public abstract String getId();

    public final String getFullId() {
        List<String> ids = getCategories();
        if (ids.isEmpty()) return getId();

        return Joiner.on(":").join(ids) + ":" + getId();
    }

    public String getInternalId() {
        return getIdGroup().name() + ":" + getFullId();
    }

    public abstract boolean isPointer();

    public final boolean isAddress() {
        return !isPointer();
    }

    public abstract int getPosition() throws ExecutionException;
}
