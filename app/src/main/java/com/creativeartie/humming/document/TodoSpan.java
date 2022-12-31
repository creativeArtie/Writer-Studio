package com.creativeartie.humming.document;

import java.util.*;
import java.util.concurrent.*;

public class TodoSpan extends IdentityBase {
    private TodoSpan(SpanBranch parent, StyleClasses[] classes) {
        super(parent, classes);
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
    public int getPosition() throws ExecutionException {
        return getStartIndex();
    }
}
