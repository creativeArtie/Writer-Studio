package com.creativeartie.humming.document;

import java.util.*;

import com.google.common.collect.*;

/**
 * A span of line or division or parts of a line.
 */
public class SpanBranch extends ForwardingList<Span> implements SpanParent {
    private final ArrayList<Span> childrenSpans;
    private final Manuscript spanRoot;
    private SpanParent spanParent;
    private final ArrayList<CssStyle> inheritedStyles;

    SpanBranch(Manuscript root, CssStyle... classes) {
        spanRoot = root;
        spanParent = root;
        inheritedStyles = new ArrayList<>();
        inheritedStyles.addAll(Arrays.asList(classes));
        childrenSpans = new ArrayList<>();
    }

    SpanBranch(SpanBranch parent, CssStyle... classes) {
        spanRoot = parent.getRoot();
        spanParent = parent;
        inheritedStyles = new ArrayList<>();
        inheritedStyles.addAll(Arrays.asList(classes));
        childrenSpans = new ArrayList<>();
    }

    @Override
    public boolean add(Span e) {
        if (e instanceof SpanBranch) ((SpanBranch) e).spanParent = this;
        return childrenSpans.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends Span> c) {
        c.forEach(span -> {
            if (span instanceof SpanBranch) ((SpanBranch) span).spanParent = this;
        });
        return childrenSpans.addAll(c);
    }

    @Override
    public final boolean cleanUp() {
        boolean isEdited = false;
        for (Span child : childrenSpans) isEdited = child.cleanUp() ? true : isEdited;
        return cleanUpSelf() ? isEdited : false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpanBranch) return hashCode() == obj.hashCode();
        return false;
    }

    @Override
    public List<Integer> findChild(Span span) {
        return getRoot().getFindChildCache(span, this);
    }

    @Override
    public List<CssStyle> getInheritedStyles() {
        ImmutableList.Builder<CssStyle> classes = ImmutableList.builder();

        classes.addAll(spanParent.getInheritedStyles());

        return classes.addAll(inheritedStyles).build();
    }

    @Override
    public List<SpanLeaf> getLeafs() {
        ImmutableList.Builder<SpanLeaf> children = ImmutableList.builder();
        for (Span child : childrenSpans)
            if (child instanceof SpanBranch) children.addAll(((SpanBranch) child).getLeafs());
            else {
                assert child instanceof SpanLeaf;
                children.add((SpanLeaf) child);
            }
        return children.build();
    }

    @Override
    public int getLength() {
        return getRoot().getCacheLength(this);
    }

    @Override
    public Optional<SpanParent> getParent() {
        return Optional.ofNullable(spanParent);
    }

    @Override
    public Manuscript getRoot() {
        return spanRoot;
    }

    @Override
    public String toString() {
        String simpleName = getClass().getSimpleName();
        if (this instanceof Para) simpleName = ((Para) this).getLineStyle().name();
        return simpleName + super.toString().replace('\n', '␤');
    }

    /**
     * Adds a new style
     *
     * @param style
     *        the style to add
     *
     * @return {@code true} if added.
     */
    protected boolean addStyle(CssStyle style) {
        if (inheritedStyles.contains(style)) return false;
        return inheritedStyles.add(style);
    }

    /**
     * Clean up itself
     *
     * @return {@code true} if something changed
     */
    protected boolean cleanUpSelf() {
        return false;
    }

    @Override
    protected List<Span> delegate() {
        return childrenSpans;
    }

    /**
     * Get the length to insert into cache
     *
     * @return the length
     */
    protected int getCacheLength() {
        int len = 0;
        for (Span child : this) len += child.getLength();
        return len;
    }

    /**
     * Remove style
     *
     * @param style
     *        the style to remove
     *
     * @return {@code true} if remove.
     */
    protected boolean removeStyle(CssStyle style) {
        return inheritedStyles.remove(style);
    }

    /**
     * Set the parent for {@link DivisionSecChapter}.
     *
     * @param document
     *        the document to add to
     */
    protected void setParent(Manuscript document) {
        spanParent = document;
    }
}
