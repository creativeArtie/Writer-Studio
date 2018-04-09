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

    private Optional<List<StyleInfo>> cacheStyles;
    private Optional<Optional<SectionSpanHead>> cacheSection;
    private Optional<Optional<CatalogueIdentity>> cacheId;
    private Optional<Optional<FormattedSpan>> cacheHead;
    private Optional<List<Optional<FormattedSpan>>> cacheContent;
    private Optional<Optional<LinedSpanCite>> cacheInText;
    private Optional<Optional<FormattedSpan>> cacheSource;
    private Optional<String> cacheLookup;
    private Optional<Integer> cacheNote;

    NoteCardSpan(List<Span> children){
        super(children);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        cacheStyles = getCache(cacheStyles, () ->
            ImmutableList.of(AuxiliaryType.MAIN_NOTE, getIdStatus()));
        return cacheStyles.get();
    }

    public Optional<FormattedSpan> getTitle(){
        cacheHead = getCache(cacheHead, () -> spanFromFirst(LinedSpanNote.class)
            .flatMap(span -> span.getFormattedSpan()));
        return cacheHead.get();
    }

    public List<Optional<FormattedSpan>> getContent(){
        cacheContent = getCache(cacheContent, () -> {
            ArrayList<Optional<FormattedSpan>> ans = new ArrayList<>();
            boolean first = true;
            for (Span child: this){
                if (child instanceof LinedSpanNote){
                    if (first){
                        first = false;
                    } else {
                        ans.add(((LinedSpanNote)child).getFormattedSpan());
                    }
                }
            }
            return ImmutableList.copyOf(ans);
        });
        return cacheContent.get();
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

    public Optional<FormattedSpan> getSource(){
        cacheSource = getCache(cacheSource, () -> getSource(this, false));
        return cacheSource.get();
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
                .split(text.replace(CHAR_ESCAPE + LINED_END, ""))){
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
    protected void childEdited(){
        cacheStyles = Optional.empty();
        cacheSection = Optional.empty();
        cacheNote = Optional.empty();
        cacheHead = Optional.empty();
        cacheContent = Optional.empty();
        cacheInText = Optional.empty();
        cacheSource = Optional.empty();
        cacheLookup = Optional.empty();
    }

    @Override
    protected void docEdited(){
        cacheId = Optional.empty();
    }
}
