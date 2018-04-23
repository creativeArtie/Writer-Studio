package com.creativeartie.writerstudio.lang;

import java.util.*; // ArrayList, List, Optional
import java.util.function.*; // Supplier

import com.google.common.collect.*; // ImmutableList

import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * A {@link Span} storing {@link SpanLeaf} and {@link SpanBranch}.
 */
public abstract class SpanBranch extends SpanNode<Span> {

    private ArrayList<Span> spanChildren;

    private SpanNode<?> spanParent;

    private CacheKeyMain<Document> spanDoc;

    private CacheKeyMain<CatalogueStatus> spanStatus;

    public SpanBranch(List<Span> spans){
        spanChildren = new ArrayList<>();
        setChildren(spans);
        spanDoc = new CacheKeyMain<>(Document.class);
        spanStatus = new CacheKeyMain<>(CatalogueStatus.class);
    }

    protected final void clearCache(){
        super.clearCache();
        spanChildren.stream().filter(s -> s instanceof SpanBranch)
            .forEach(s -> ((SpanBranch)s).clearCache());
    }

    @Override
    protected final void setChildren(List<Span> spans){
        spans.forEach((span) -> {
            if (span instanceof SpanBranch){
                ((SpanBranch)span).setParent(this);
            } else {
                ((SpanLeaf)span).setParent(this);
            }
        });
        spanChildren.stream().filter(s -> s instanceof SpanBranch)
            .forEach(s -> ((SpanBranch)s).setRemove());
        spanChildren.clear();
        spanChildren.addAll(spans);
    }

    @Override
    public final Document getDocument(){
        /// will eventually get to a SpanLeaf
        return getLocalCache(spanDoc, () -> get(0).getDocument());
    }

    @Override
    public final SpanNode<?> getParent(){
        return spanParent;
    }

    final void setParent(SpanNode<?> parent){
        spanParent = parent;
    }

    /** Get style information about this {@linkplain SpanBranch}.*/
    public abstract List<StyleInfo> getBranchStyles();

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

    /** Edit the children if this can hold the entire text. */
    final boolean editRaw(String text){
        checkNotEmpty(text, "text");
        SetupParser parser = getParser(text);
        if (parser != null){
            /// It can be fully parsed.

            reparseText(text, parser);
            return true;
       }
       return false;
    }

    /** Gets the parser only if it can reparsed the whole text. */
    protected abstract SetupParser getParser(String text);

    protected final void runCommand(Command command){
        String text = command.getResult();
        if (text == null || text.isEmpty()){
            StringBuilder builder = new StringBuilder();
            getDocument().delete(getStart(), getEnd());
            return;
        }
        SetupParser parser = getParser(text);
        if (parser == null){
            throw new IllegalArgumentException(
                "Command does not return a parseable answer");
        }
        reparseText(text, parser);
    }

    private final void reparseText(String text, SetupParser parser){

        SetupPointer pointer = SetupPointer.updatePointer(text,
            getDocument());
        Optional<SpanBranch> span = parser.parse(pointer);
        span.ifPresent(s -> updateSpan((List<Span>)s));
        /// There are text left over.
        if (pointer.hasNext()){
            throw new IllegalStateException("Has left over characters.");
        }
    }

    public final CatalogueStatus getIdStatus(){
        return getDocCache(spanStatus, () -> {
            if (this instanceof Catalogued){
                Catalogued catalogued = (Catalogued) this;
                Optional<CatalogueIdentity> id = catalogued.getSpanIdentity();
                if (id.isPresent()){
                    return id.get().getStatus(getDocument().getCatalogue());
                }
            }
            return CatalogueStatus.NO_ID;
        });
    }


    @Override
    public final List<Span> delegate(){
        return ImmutableList.copyOf(spanChildren);
    }

    List<Span> getChildren(){
        return spanChildren;
    }
}
