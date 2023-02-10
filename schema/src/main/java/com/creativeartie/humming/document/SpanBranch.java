package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

public class SpanBranch extends ForwardingList<Span> implements SpanParent {
    private ArrayList<Span> childrenSpans;
    private final Document spanRoot;
    private Optional<SpanParent> spanParent;
    private ArrayList<StyleClass> inheritedStyles;

    protected SpanBranch(Document root, StyleClass... classes) {
        spanRoot = root;
        spanParent = Optional.empty();
        inheritedStyles = new ArrayList<>();
        inheritedStyles.addAll(Arrays.asList(classes));
        childrenSpans = new ArrayList<>();
    }

    protected SpanBranch(SpanBranch parent, StyleClass... classes) {
        spanRoot = parent.getRoot();
        spanParent = Optional.of(parent);
        inheritedStyles = new ArrayList<>();
        inheritedStyles.addAll(Arrays.asList(classes));
        childrenSpans = new ArrayList<>();
    }

    protected boolean addStyle(StyleClass style) {
        if (inheritedStyles.contains(style)) {
            return false;
        }
        return inheritedStyles.add(style);
    }

    protected boolean removeStyle(StyleClass style) {
        return inheritedStyles.remove(style);
    }

    @Override
    public Document getRoot() {
        return spanRoot;
    }

    @Override
    public List<StyleClass> getInheritedStyles() {
        ImmutableList.Builder<StyleClass> classes = ImmutableList.builder();
        if (spanParent.isPresent()) {
            classes.addAll(spanParent.get().getInheritedStyles());
        }
        return classes.addAll(inheritedStyles).build();
    }

    @Override
    public boolean add(Span e) {
        if (e instanceof SpanBranch) ((SpanBranch) e).spanParent = Optional.of(this);
        return childrenSpans.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends Span> c) {
        c.forEach((span) -> {
            if (span instanceof SpanBranch) ((SpanBranch) span).spanParent = Optional.of(this);
        });
        return childrenSpans.addAll(c);
    }

    @Override
    protected List<Span> delegate() {
        return childrenSpans;
    }

    @Override
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
        return cleanUpSelf() ? isEdited : false;
    }

    protected boolean cleanUpSelf() {
        return false;
    }

    @Override
    public Optional<SpanParent> getParent() {
        if (spanParent.isEmpty()) {
            return Optional.of(spanRoot);
        }
        return spanParent;
    }

    @Override
    public List<Integer> findChild(Span span) {
        return getRoot().getFindChildCache(span, this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpanBranch) {
            return hashCode() == obj.hashCode();
        }
        return false;
    }

    @Override
    public int getLength() {
        return getRoot().getCacheLength(this);
    }

    protected int getCacheLength() {
        int len = 0;
        for (Span child : this) {
            len += child.getLength();
        }
        return len;
    }

    @Override
    public String toString() {
        String simpleName = getClass().getSimpleName();
        if (simpleName == "") {
            simpleName = "LineSpan";
        }
        return simpleName + super.toString();
    }
}
