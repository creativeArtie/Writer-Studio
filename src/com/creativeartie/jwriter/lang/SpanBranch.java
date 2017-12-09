package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;

/**
 * A {@link Span} storing {@link SpanLeaf} and {@link SpanBranch}.
 */
public abstract class SpanBranch extends SpanNode<Span> {

    private ArrayList<Span> spanChildren;

    private SpanNode<?> spanParent;

    private Optional<Document> spanDoc;

    public SpanBranch(List<Span> spans){
        spanChildren = setParents(spans);
    }

    /**
     * Set the children's parent to this. Helper method of
     * {@link #SpanBranch(List)} and {@link #editRaw(String)}.
     */
    private final ArrayList<Span> setParents(List<Span> spans){
        ArrayList<Span> ans = new ArrayList<>(spans);
        ans.forEach((span) -> {
            if (span instanceof SpanBranch){
                ((SpanBranch)span).setParent(this);
            } else {
                ((SpanLeaf)span).setParent(this);
            }
        });
        return ans;
    }

    @Override
    public final List<Span> delegate(){
        return ImmutableList.copyOf(spanChildren);
    }

    @Override
    public final Document getDocument(){
        return get(0).getDocument(); /// will eventually get to a SpanLeaf
    }

    @Override
    public final SpanNode<?> getParent(){
        return spanParent;
    }

    final void setParent(SpanNode<?> parent){
        spanParent = parent;
    }

    /** Get style information about this {@linkplain SpanBranch}.*/
    public abstract List<DetailStyle> getBranchStyles();

    @Override
    public final List<SpanLeaf> getLeaves(){
        return getDocument().getLeavesCache(this, () -> {
            /// Create the builder
            ImmutableList.Builder<SpanLeaf> builder = ImmutableList.builder();
            for(Span span: this){
                if (span instanceof SpanLeaf){
                    builder.add((SpanLeaf)span);
                } else if (span instanceof SpanBranch){
                    builder.addAll(((SpanBranch)span).getLeaves());
                }
            }
            return builder.build();
        });
    }

    /** Edit the */
    final boolean editRaw(String text){
        SetupParser parser = getParser(text);
        if (parser != null){
            for (Span span: this){
                span.setRemove();
            }
            parser.parse(SetupPointer.updatePointer(text, getDocument()))
                .ifPresent(span -> spanChildren = setParents(span));
            setUpdated();
            return true;
       }
       return false;
    }

    protected abstract SetupParser getParser(String text);

    protected abstract void childEdited();

    public final CatalogueStatus getIdStatus(){
        if (this instanceof Catalogued){
            Catalogued catalogued = (Catalogued) this;
            Optional<CatalogueIdentity> id = catalogued.getSpanIdentity();
            if (id.isPresent()){
                return id.get().getStatus(getDocument().getCatalogue());
            }
        }
        return CatalogueStatus.NO_ID;

    }
}
