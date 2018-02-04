package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.function.Predicate;
import java.util.Optional;

import com.google.common.collect.*;
import com.google.common.base.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Research note with headings, ids, and citation. Represented in
 * design/ebnf.txt as {@code Note}
 */
public class NoteCardSpan extends SpanBranch implements Catalogued {

    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<Optional<LinedSpanCite>> cacheInText;
    private Optional<Optional<FormatSpanMain>> cacheSource;
    private Optional<String> cacheLookup;
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
    
    public Optional<LinedSpanCite> getInTextLine(){
        cacheInText = getCache(cacheInText, () ->{
            for (Span child: this){
                if(isType(child, type -> type == InfoFieldType.FOOTNOTE ||
                        type == InfoFieldType.IN_TEXT)){
                    return Optional.of((LinedSpanCite) child);
                }
            }
            return Optional.empty();
        });
        
        return cacheInText.get();
    }
    
    public Optional<FormatSpanMain> getSource(){
        cacheSource = getCache(cacheSource, () ->{
            for (Span child: this){
                if (isType(child, type -> type == InfoFieldType.SOURCE)){
                    LinedSpanCite cite = (LinedSpanCite) child;
                    InfoDataSpan data = cite.getData().get();
                    
                    return Optional.of((FormatSpanMain)data.getData());
                }
            }
            return Optional.empty();
        });
        return cacheSource.get();
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
    public boolean isId(){
        return true;
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

    public String getLookupText(){
        cacheLookup = getCache(cacheLookup, () ->
            spanFromFirst(LinedSpanNote.class)
                .map(span -> span.getLookupText())
                .orElse("")
        );
        return cacheLookup.get();
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("NOTE:{\n\t");
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
        if (! AuxiliaryChecker.checkSectionEnd(isLast(), text)){
            return null;
        }
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
        cacheInText = Optional.empty();
        cacheSource = Optional.empty();
        cacheSources = Optional.empty();
        cacheLookup = Optional.empty();
    }

    @Override
    protected void docEdited(){
        cacheId = Optional.empty();
    }
}
