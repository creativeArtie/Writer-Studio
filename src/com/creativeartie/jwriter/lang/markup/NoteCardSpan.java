package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.Optional;

import com.google.common.collect.*;
import com.google.common.base.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Research note with headings, ids, and citation. Represented in
 * design/ebnf.txt as {@code Note}
 */
public class NoteCardSpan extends MainSpan {

    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<Integer> cacheNote;
    private Optional<ImmutableListMultimap<InfoFieldType, InfoDataSpan>>
        cacheSources;

    NoteCardSpan(List<Span> children){
        super(children);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () ->
            ImmutableList.of(AuxiliaryType.MAIN_NOTE, getIdStatus()));
        return cacheStyles.get();
    }

    public ImmutableListMultimap<InfoFieldType, InfoDataSpan> getSources(){
        cacheSources = getCache(cacheSources, () -> {
            ImmutableListMultimap.Builder<InfoFieldType, InfoDataSpan> data =
                ImmutableListMultimap.builder();
            for (Span child: this){
                if (child instanceof LinedSpanCite){
                    LinedSpanCite cite = (LinedSpanCite) child;
                    if (cite.getData().isPresent()){
                        data.put(cite.getFieldType(), cite.getData().get());
                    }
                }
            }
            return data.build();
        });
        return cacheSources.get();
    }

    public int getNoteTotal(){
        cacheNote = getCache(cacheNote, () -> {
            int notes = 0;
            for (Span child: this){
                notes += ((LinedSpan)child).getNoteTotal();
            }
            return notes;
        });
        return cacheNote.get();
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        cacheId = getCache(cacheId, () ->
            Optional.of(spanFromFirst(LinedSpanNote.class)
                .flatMap(line -> line.buildId())
                .orElseGet(() ->
                    new CatalogueIdentity(Arrays.asList(TYPE_COMMENT), this)
                )
            )
        );
        return cacheId.get();
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("NOTE:{");
        for(Span span: this){
            output.append("\n\t").append(span.toString());
        }
        output.append("\n}");
        return output.toString();
    }

    @Override
    protected SetupParser getParser(String text){
        boolean isFirst = true;
        if (text.endsWith(LINED_END)){
            text = text.substring(0, text.length() - LINED_END.length());
        }
        for (String str : Splitter.on(LINED_END)
                .split(text.replace(CHAR_ESCAPE + LINED_END, ""))){
            if (isFirst){
                if (LinedSpanNote.canParse(str) || LinedSpanCite.canParse(str)){
                    isFirst = false;
                } else {
                    return null;
                }
            } else {
                if (!LinedSpanNote.canParseWithoutId(str) &&
                        ! LinedSpanCite.canParse(str)){
                    return null;
                }
            }
        }
        return NoteCardParser.PARSER;
    }

    @Override
    protected void childEdited(){
        cacheStyles = Optional.empty();
        cacheNote = Optional.empty();
        cacheSources = Optional.empty();
    }

    @Override
    protected void docEdited(){
        cacheId = Optional.empty();
    }
}
