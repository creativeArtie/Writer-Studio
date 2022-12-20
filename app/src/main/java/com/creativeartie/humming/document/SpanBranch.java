package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public class SpanBranch extends ForwardingList<Span> implements Span {
    private ArrayList<Span> childrenSpans;
    private final Document spanRoot;
    private ImmutableList<StyleClasses> inheritedStyles;

    protected SpanBranch(Document root) {
        spanRoot = root;
        inheritedStyles = ImmutableList.of();
        childrenSpans = new ArrayList<>();
    }

    protected SpanBranch(SpanBranch parent) {
        spanRoot = parent.getRoot();
        inheritedStyles = ImmutableList.copyOf(parent.getInheritedStyles());
        childrenSpans = new ArrayList<>();
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
}
