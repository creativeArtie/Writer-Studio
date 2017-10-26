package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.collect.*;

/**
 * A {@link Span} handling {@link SpanLeaf}
 */
public abstract class SpanBranch extends SpanNode<Span> {

    private final ArrayList<Span> spanChildren;

    private SpanNode<?> spanParent;

    public SpanBranch(List<Span> spans){
        spanChildren = new ArrayList<>(spans);
        setParents(spanChildren);
    }

    private void setParents(List<Span> spans){
        spans.forEach((span) -> {
            if (span instanceof SpanBranch){
                ((SpanBranch)span).setParent(this);
            } else {
                ((SpanLeaf)span).setParent(this);
            }
        });
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
    Span removeChild(int index){
        Span span = spanChildren.remove(index);
        span.setRemove();
        setEdit();
        return span;
    }

    @Override
    void addChildren(int index, List<Span> spans){
        spanChildren.addAll(index, spans);
        setParents(spans);
        setEdit();
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

    void getStyles(List<DetailStyle> styles){
        if (spanParent instanceof SpanBranch){
            ((SpanBranch)spanParent).getStyles(styles);
        }
        styles.addAll(getBranchStyles());
    }

    /* // TODO Speed up preformance by edit only some of the text
    protected DetailUpdater getUpdater(int index, String raw){
        return DetailUpdater.unable();
    }
    */

    boolean editRaw(String text){
       return editRaw(spanChildren, text);
    }
    protected boolean editRaw(ArrayList<Span> children, String text){
        return false;
    }

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
