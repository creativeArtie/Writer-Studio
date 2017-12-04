package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;

/**
 * A {@link Span} handling {@link SpanLeaf}
 */
public abstract class SpanBranch extends SpanNode<Span> {

    private ArrayList<Span> spanChildren;

    private SpanNode<?> spanParent;

    public SpanBranch(List<Span> spans){
        spanChildren = setParents(spans);
    }

    private ArrayList<Span> setParents(List<Span> spans){
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

    protected int search(String raw, String escape, List<String> find){
        return search(raw, escape, find.toArray(new String[0]));
    }

    protected int search(String raw, String escape, String ... find){
        boolean isEscape = false;
        for(int i = 0; i < raw.length(); i++){
            if (isEscape){
                isEscape = false;
            } else {
                if (raw.startsWith(escape, i)){
                    isEscape = true;
                } else {
                    for(String str: find){
                        if (raw.startsWith(str, i)){
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public final List<Span> delegate(){
        return ImmutableList.copyOf(spanChildren);
    }

    @Override
    public Document getDocument(){
        return get(0).getDocument();
    }

    @Override
    public SpanNode<?> getParent(){
        return spanParent;
    }

    void setParent(SpanNode<?> parent){
        spanParent = parent;
    }

    public abstract List<DetailStyle> getBranchStyles();

    public List<SpanLeaf> getLeaves(){
        return getDocument().getLeavesCache(this, () -> {
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

    boolean editRaw(String text){
        SetupParser parser = getParser(text);
        if (parser != null){
            for (Span span: this){
                span.setRemove();
            }
            parser.parse(SetupPointer.updatePointer(text, getDocument()))
                .ifPresent(span -> spanChildren = setParents(span));
            setEdit();
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
