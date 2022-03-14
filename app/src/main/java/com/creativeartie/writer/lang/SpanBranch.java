package com.creativeartie.writer.lang;

import java.util.*;

import com.google.common.collect.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** A {@link Span} storing {@link SpanLeaf} and {@link SpanBranch}.
 *
 * Purpose
 * <ul>
 * <li> Updating and editing text </li>
 * <li> Common get methods </li>
 * </ul>
 */
public abstract class SpanBranch extends SpanNode<Span> {

    /// %Part 1: Constructor & Fields ##########################################

    private ArrayList<Span> spanChildren;

    private SpanNode<?> spanParent;

    private CacheKeyMain<Document> spanDoc;

    private CacheKeyMain<CatalogueStatus> spanStatus;

    /** Creates a {@link SpanBranch}.
     *
     * @param spans
     *      list of children
     */
    protected SpanBranch(List<Span> spans){
        spanChildren = new ArrayList<>();
        setChildren(argumentNotEmpty(spans, "spans"));

        spanDoc = new CacheKeyMain<>(Document.class);
        spanStatus = new CacheKeyMain<>(CatalogueStatus.class);
    }

    /// %Part 2: Updating and Editing ##########################################

    @Override
    protected synchronized final void runCommand(Command command){
        argumentNotNull(command, "command");
        String text = command.getResult();

        /// no text = delete
        if (text == null || text.isEmpty()){
            StringBuilder builder = new StringBuilder();
            removeSpan();
            return;
        }

        /// replace text
        SetupParser parser = getParser(text);
        if (parser == null){
            throw new IllegalArgumentException(
                "Command does not return a parseable answer");
        }

        /// next step
        stateCheck(reparseText(text, parser),
            "Has left over characters when reparsing: " +
            getClass().getSimpleName()
        );
    }


    /** Check if this text can be replaced and replace as neccessary.
     *
     * @param text
     *      the text to replace with
     * @return answer
     */
    final boolean editRaw(String text){
        argumentNotEmpty(text, "text");

        SetupParser parser = getParser(text);
        if (parser != null){
            /// It might be fully parsed.
            return reparseText(text, parser);
       }
       return false;
    }

    /** Gets the parser only if it can reparsed the whole text.
     *
     * @param text
     *      replacing text
     * @return answer; nullable
     * @see #runCommand(Command)
     * @see #editRaw(String)
     */
    protected abstract SetupParser getParser(String text);

    /** Reparse the text.
     *
     * @param text
     *      replacing text
     * @param parser
     *      span parser
     * @return is successful
     */
    private final boolean reparseText(String text, SetupParser parser){
        assert text != null && text.length() > 0: "Empty text";
        assert parser != null: "Null parser";

        SetupPointer pointer = SetupPointer.updatePointer(text,
            getDocument(), isDocumentFirst());
        Optional<SpanBranch> span = parser.parse(pointer);
        if (pointer.hasNext()){
            return false;
        }
        assert span.isPresent(): "Null span";
        updateSpan((List<Span>)span.get());
        return true;
    }

    protected synchronized final void removeSpan(){
        SpanNode<?> parent = getParent();
        int index = parent.indexOf(this);
        argumentCheck(index != -1, "Parameter 'span' is not a child: " + this);
        setRemove();
        parent.removeChild(index);
        parent.updateSpan();
    }

    @Override
    protected final void removeChild(int index){
        spanChildren.remove(index);
    }

    @Override
    final void addChild(SpanBranch span, int index){
        spanChildren.add(index, span);
        span.setParent(this);
    }

    @Override
    protected final void setChildren(List<Span> spans){
        for (Span span: spans) span.setParent(this);
        for (Span child: spanChildren){
            if (child instanceof SpanBranch) ((SpanBranch)child).setRemove();
        }
        spanChildren.clear();
        spanChildren.addAll(spans);
    }

    @Override
    final void clearDocCache(){
        super.clearDocCache();
        for (Span child: spanChildren){
            if (child instanceof SpanBranch){
                ((SpanBranch)child).clearDocCache();
            }
        }
    }

    /// %Part 3: Common Get Methods ############################################

    /** Get the catalogue status.
     *
     * @return answer
     */
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

    /// %Part 4: Overriding Methods ############################################

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

    @Override
    public final Document getDocument(){
        /// will eventually get to a SpanLeaf
        return getLocalCache(spanDoc, () -> get(0).getDocument());
    }

    @Override
    public final SpanNode<?> getParent(){
        return spanParent;
    }

    @Override
    final void setParent(SpanNode<?> parent){
        spanParent = parent;
    }

    @Override
    public final Range<Integer> getRange(){
        /// make it final in the package
        return super.getRange();
    }

    @Override
    public final List<Span> delegate(){
        return ImmutableList.copyOf(spanChildren);
    }
}
