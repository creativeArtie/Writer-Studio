package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public class SpanBranch extends ForwardingList<Span> implements Span {
    private ArrayList<Span> childrenSpans;
    private final Document spanRoot;
    private ArrayList<StyleClasses> inheritedStyles;

    protected SpanBranch(Document root, StyleClasses... classes) {
        spanRoot = root;
        inheritedStyles = new ArrayList<>();
        inheritedStyles.addAll(Arrays.asList(classes));
        childrenSpans = new ArrayList<>();
    }

    protected SpanBranch(SpanBranch parent, StyleClasses... classes) {
        spanRoot = parent.getRoot();
        inheritedStyles = new ArrayList<>();
        inheritedStyles.addAll(parent.getInheritedStyles());
        inheritedStyles.addAll(Arrays.asList(classes));
        childrenSpans = new ArrayList<>();
    }

    protected boolean addStyle(StyleClasses style) {
        if (inheritedStyles.contains(style)) {
            return false;
        }
        return inheritedStyles.add(style);
    }

    protected boolean removeStyle(StyleClasses style) {
        return inheritedStyles.remove(style);
    }

    @Override
    public Document getRoot() {
        return spanRoot;
    }

    List<StyleClasses> getInheritedStyles() {
        return inheritedStyles;
    }

    @Override
    public boolean add(Span e) {
        return childrenSpans.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends Span> c) {
        return childrenSpans.addAll(c);
    }

    @Override
    protected List<Span> delegate() {
        return childrenSpans;
    }

    public List<SpanLeaf> getLeafs() {
        ImmutableList.Builder<SpanLeaf> children = ImmutableList.builder();
        for (Span child : childrenSpans) {
            if (child instanceof SpanBranch) {
                children.addAll(((SpanBranch) child).getLeafs());
            } else {
                assert child instanceof SpanLeaf;
                children.add((SpanLeaf) child);
            }
        }
        return children.build();
    }

    @Override
    public final boolean cleanUp() {
        boolean isEdited = false;
        for (Span child : childrenSpans) {
            isEdited = child.cleanUp() ? true : isEdited;
        }
        return isEdited;
    }

    protected boolean cleanUpSelf() {
        return false;
    }
}
