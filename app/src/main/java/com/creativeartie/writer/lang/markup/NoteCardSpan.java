package com.creativeartie.writer.lang.markup;

import java.util.*;
import java.util.function.Predicate;
import java.util.Optional;

import com.google.common.collect.*;
import com.google.common.base.*;

import com.creativeartie.writer.lang.*;
import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writer.main.ParameterChecker.*;

/** Research note with headings, ids, and citation. */
public class NoteCardSpan extends SpanBranch implements Catalogued {

    private final CacheKeyOptional<FormattedSpan> cacheHead;
    private final CacheKeyList<FormattedSpan> cacheContent;
    private final CacheKeyOptional<LinedSpanCite> cacheInText;
    private final CacheKeyOptional<FormattedSpan> cacheSource;

    private final CacheKeyMain<Integer> cacheNote;

    private final CacheKeyMain<String> cacheLookup;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;

    /** Creates a {@linkplain NoteCardSpan}.
     *
     * @param children
     *      span children
     * @see NoteCardPaser#parse(SetupParser)
     */
    NoteCardSpan(List<Span> children){
        super(children);

        cacheHead = new CacheKeyOptional<>(FormattedSpan.class);
        cacheContent = new CacheKeyList<>(FormattedSpan.class);
        cacheInText = new CacheKeyOptional<>(LinedSpanCite.class);
        cacheSource = new CacheKeyOptional<>(FormattedSpan.class);

        cacheNote = CacheKeyMain.integerKey();

        cacheLookup = CacheKeyMain.stringKey();
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
    }

    /** Gets the formatted note title
     *
     * @return answer
     */
    public Optional<FormattedSpan> getTitle(){
        return getLocalCache(cacheHead, () -> spanFromFirst(LinedSpanNote.class)
            .flatMap(s -> s.getFormattedSpan()));
    }

    /** Gets the formatted content lines
     *
     * @return answer
     */
    public List<FormattedSpan> getContent(){
        return getLocalCache(cacheContent, () -> {
            ArrayList<FormattedSpan> ans = new ArrayList<>();
            boolean first = true;
            for (LinedSpanNote child: getChildren(LinedSpanNote.class)){
                if (first){
                    first = false;
                } else /** if (not heading line) */{
                    child.getFormattedSpan().ifPresent(c -> ans.add(c));
                }
            }
            return ImmutableList.copyOf(ans);
        });
    }

    /** Gets the formatted in text citations
     *
     * @return answer
     */
    public Optional<LinedSpanCite> getInTextLine(){
        return getLocalCache(cacheInText, () ->{
            for (LinedSpanCite child: getChildren(LinedSpanCite.class)){
                if(isType(child, t -> t == InfoFieldType.FOOTNOTE ||
                        t == InfoFieldType.IN_TEXT)){
                    return Optional.of(child);
                }
            }
            return Optional.empty();
        });

    }

    /** Gets the formatted Work Cited page citation.
     *
     * @return answer
     */
    public Optional<FormattedSpan> getSource(){
        return getLocalCache(cacheSource, () -> getSource(this, false));
    }

    /** Recursivly find the formatted Work Cited page citation.
     *
     * @param start
     *      starting note card
     * @param loop
     *      could be loop?
     * @return answer
     * @see #getSource
     */
    private Optional<FormattedSpan> getSource(NoteCardSpan start, boolean loop){
        assert start != null: "Null start";

        /// the reference ultimately points back to itself.
        if (this == start && loop){/// base case
            return Optional.empty();
        }
        for (LinedSpanCite child: getChildren(LinedSpanCite.class)){
            if (isType(child, type -> type == InfoFieldType.SOURCE)){
                /// Finds the citation
                return child.getData().map(s -> (FormattedSpan)s);

            } else if (isType(child, t -> t == InfoFieldType.REF)){
                /// finds a reference
                return child.getData()
                    /// s == SpanBranch
                    .map(s -> (DirectorySpan) s)
                    /// s == DirectorySpan
                    .map(d -> d.buildId())
                    /// i == CatalogueIdentity
                    .map(i -> getDocument().getCatalogue().get(i))
                    /// d == CatalogueData
                    .filter(d -> d.isReady())
                    .map(d -> d.getTarget())
                    /// s == SpanBranch
                    .filter(s -> s instanceof NoteCardSpan)
                    .map(s -> (NoteCardSpan) s)
                    /// s == NoteCardSpan
                    .flatMap(s -> s.getSource(start, true));
            }
        }
        return Optional.empty();
    }

    /** Check span {@link InfoFieldType}.
     *
     * @param child
     *      checking child
     * @param filter
     *      test filter
     * @return answer
     */
    private boolean isType(LinedSpanCite child, Predicate<InfoFieldType> filter){
        return Optional.ofNullable(child)
            /// c == LindSpanCite
            .filter(c -> c.getData().isPresent())
            .map(c -> c.getInfoFieldType())
            /// c == InfoFieldType
            .filter(filter)
            .isPresent();
    }

    /** Gets note total
     *
     * @return answer
     */
    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> stream()
            /// s == SpanBranch
            .filter(s -> s instanceof LinedSpan)
            .map(s -> (LinedSpan) s)
            /// s == LinedSpan
            .mapToInt(
                c -> c.getNoteTotal()
            ).sum());
    }

    /** Gets the user reference help text.
     *
     * @return answer
     */
    public String getLookupText(){
        return getLocalCache(cacheLookup, () ->
            spanFromFirst(LinedSpanNote.class)
                .map(span -> span.getLookupText())
                .orElse("")
        );
    }

    @Override
    public boolean isId(){
        return true;
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return getDocCache(cacheId, () -> Optional.of(
            spanFromFirst(LinedSpanNote.class)
                .flatMap(line -> line.buildId())
                .orElseGet(() ->
                    new CatalogueIdentity(Arrays.asList(TYPE_NOTE), this)
                )
        ));
    }

    @Override
    protected SetupParser getParser(String text){
        argumentNotNull(text, "text");

        /// check if ending merge with next
        if (! AuxiliaryChecker.checkSectionEnd(text, isDocumentLast())){
            return null;
        }

        /// remove the line end
        if (text.endsWith(LINED_END)){
            text = text.substring(0, text.length() - LINED_END.length());
        }

        /// Check line (with escaped line end removed.
        boolean isFirst = true;
        for (String str : Splitter.on(LINED_END)
                .split(text.replace(TOKEN_ESCAPE + LINED_END, ""))){
            if (isFirst){
                /// First line
                if (LinedSpanNote.checkFirstLine(str) || LinedSpanCite
                        .checkLine(str)){
                    isFirst = false;
                } else {
                    return null;
                }
            } else {
                /// Middle line
                if (! LinedSpanNote.checkMiddleLine(str) && ! LinedSpanCite
                        .checkLine(str)){
                    return null;
                }
            }
        }

        /// return answer
        return NoteCardParser.PARSER;
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("CARD:{\n\t");
        boolean isFirst = true;
        for(Span span: this){
            if(isFirst){
                isFirst = false;
            } else {
                output.append("\t,");
            }
            output.append(span.toString());
        }
        output.append("}");
        return output.toString();
    }

}
