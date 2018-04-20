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

    private Optional<Document> spanDoc;

    private Optional<CatalogueStatus> spanStatus;

    public SpanBranch(List<Span> spans){
        spanChildren = setParents(spans);
        spanStatus = Optional.empty();
        childEdited();
        docEdited();
    }

    /**
     * Set the children's parent to this. Helper method of
     * {@link #SpanBranch(List)} and {@link #editRaw(String)}.
     */
    private final ArrayList<Span> setParents(List<Span> spans){
        checkNotEmpty(spans, "spans");
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

        // fire listeners
        getDocument().updateEdit(this);
    }

    private void reparseText(String text, SetupParser parser){
        /// Removes the children
        for (Span span: this){
            span.setRemove();
        }

        SetupPointer pointer = SetupPointer.updatePointer(text,
            getDocument());
        parser.parse(pointer).ifPresent(span ->
            spanChildren = setParents(span)
        );
        /// There are text left over.
        if (pointer.hasNext()){
            throw new IllegalStateException("Has left over characters.");
        }
        spanStatus = Optional.empty();
    }

    public final CatalogueStatus getIdStatus(){
        spanStatus = getCache(spanStatus, () -> {
            if (this instanceof Catalogued){
                Catalogued catalogued = (Catalogued) this;
                Optional<CatalogueIdentity> id = catalogued.getSpanIdentity();
                if (id.isPresent()){
                    return id.get().getStatus(getDocument().getCatalogue());
                }
            }
            return CatalogueStatus.NO_ID;
        });
        return spanStatus.get();
    }

    /** A simple cache method that make use of {@link Optional}.*/
    protected <T> Optional<T> getCache(Optional<T> found, Supplier<T> maker){
        // TODO needs a better system
        checkNotNull(found, "found");
        checkNotNull(maker, "maker");
        if (found.isPresent()){
            return found;
        }
        return Optional.of(maker.get());
    }


    @Override
    public final List<Span> delegate(){
        return ImmutableList.copyOf(spanChildren);
    }

    List<Span> getChildren(){
        return spanChildren;
    }
}
