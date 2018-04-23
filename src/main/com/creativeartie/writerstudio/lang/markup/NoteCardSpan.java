package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.Predicate;
import java.util.Optional;

import com.google.common.collect.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * Research note with headings, ids, and citation. Represented in
 * design/ebnf.txt as {@code Note}
 */
public class NoteCardSpan extends SpanBranch implements Catalogued {

    private final CacheKeyList<StyleInfo> cacheStyles;
    private final CacheKeyOptional<SectionSpanHead> cacheSection;
    private final CacheKeyOptional<CatalogueIdentity> cacheId;
    private final CacheKeyOptional<FormattedSpan> cacheHead;
    private final CacheKeyList<FormattedSpan> cacheContent;
    private final CacheKeyOptional<LinedSpanCite> cacheInText;
    private final CacheKeyOptional<FormattedSpan> cacheSource;
    private final CacheKeyMain<String> cacheLookup;
    private final CacheKeyMain<Integer> cacheNote;

    NoteCardSpan(List<Span> children){
        super(children);

        cacheStyles = new CacheKeyList<>(StyleInfo.class);
        cacheSection = new CacheKeyOptional<>(SectionSpanHead.class);
        cacheId = new CacheKeyOptional<>(CatalogueIdentity.class);
        cacheHead = new CacheKeyOptional<>(FormattedSpan.class);
        cacheContent = new CacheKeyList<>(FormattedSpan.class);
        cacheInText = new CacheKeyOptional<>(LinedSpanCite.class);
        cacheSource = new CacheKeyOptional<>(FormattedSpan.class);
        cacheLookup = CacheKey.stringKey();
        cacheNote = CacheKey.integerKey();
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return getLocalCache(cacheStyles, () ->
            ImmutableList.of(AuxiliaryType.MAIN_NOTE, getIdStatus()));
    }

    public Optional<FormattedSpan> getTitle(){
        return getLocalCache(cacheHead, () -> spanFromFirst(LinedSpanNote.class)
            .flatMap(span -> span.getFormattedSpan()));
    }

    public List<FormattedSpan> getContent(){
        return getLocalCache(cacheContent, () -> {
            ArrayList<FormattedSpan> ans = new ArrayList<>();
            boolean first = true;
            for (Span child: this){
                if (child instanceof LinedSpanNote){
                    if (first){
                        first = false;
                    } else {
                        ((LinedSpanNote) child).getFormattedSpan()
                            .ifPresent(c -> ans.add(c));
                    }
                }
            }
            return ImmutableList.copyOf(ans);
        });
    }

    public Optional<LinedSpanCite> getInTextLine(){
        return getLocalCache(cacheInText, () ->{
            for (Span child: this){
                if(isType(child, type -> type == InfoFieldType.FOOTNOTE ||
                        type == InfoFieldType.IN_TEXT)){
                    return Optional.of((LinedSpanCite) child);
                }
            }
            return Optional.empty();
        });

    }

    public Optional<FormattedSpan> getSource(){
        return getLocalCache(cacheSource, () -> getSource(this, false));
    }

    private Optional<FormattedSpan> getSource(NoteCardSpan start, boolean loop){
        if (this == start && loop){
            return Optional.empty();
        }
        for (Span child: this){
            if (isType(child, type -> type == InfoFieldType.SOURCE)){
                LinedSpanCite cite = (LinedSpanCite) child;
                InfoDataSpan data = cite.getData().get();
                return Optional.of((FormattedSpan)data.getData());

            } else if (isType(child, t -> t == InfoFieldType.REF)){
                LinedSpanCite cite = (LinedSpanCite) child;
                InfoDataSpan found = cite.getData().get();
                DirectorySpan id = (DirectorySpan)found.getData();
                CatalogueData data = getDocument().getCatalogue().get(id);
                if (data.isReady()){
                    Span span = data.getTarget();
                    assert span instanceof NoteCardSpan;
                    return ((NoteCardSpan)span).getSource(start, true);
                }
            }
        }
        return Optional.empty();
    }

    private boolean isType(Span child, Predicate<InfoFieldType> filter){
        return Optional.ofNullable(child instanceof LinedSpanCite?
                (LinedSpanCite) child: null)
            .filter(cite -> cite.getData().isPresent())
            .map(cite -> cite.getFieldType())
            .filter(filter)
            .isPresent();
    }

    public int getNoteTotal(){
        return getLocalCache(cacheNote, () -> {
            int notes = 0;
            for (Span child: this){
                notes += ((LinedSpan)child).getNoteTotal();
            }
            return notes;
        });
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
                    new CatalogueIdentity(Arrays.asList(TYPE_COMMENT), this)
                )
        ));
    }

    public String getLookupText(){
        return getLocalCache(cacheLookup, () ->
            spanFromFirst(LinedSpanNote.class)
                .map(span -> span.getLookupText())
                .orElse("")
        );
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

    @Override
    protected SetupParser getParser(String text){
        checkNotNull(text, "text");

        /// check if ending merge with next
        if (! AuxiliaryChecker.checkSectionEnd(text, isLast())){
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

}
