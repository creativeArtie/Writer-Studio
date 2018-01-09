package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Research note with headings, ids, and citation. Represented in
 * design/ebnf.txt as {@code Note}
 */
public class NoteCardSpan extends MainSpan {

    NoteCardSpan(List<Span> children){
        super(children);
    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(AuxiliaryType.MAIN_NOTE, getIdStatus());
    }

    public ImmutableListMultimap<InfoFieldType, InfoDataSpan> getSources(){
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
    }

    public int getNoteTotal(){
        int notes = 0;
        for (Span child: this){
            notes += ((LinedSpan)child).getNoteTotal();
        }
        return notes;
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        Optional<LinedSpanNote> id = spanFromFirst(LinedSpanNote.class);
        return Optional.of(id.flatMap(line -> line.buildId())
            .orElseGet(() -> new CatalogueIdentity(Arrays.asList(TYPE_COMMENT),
            this)));
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

    private final int PARSE_START = 0;
    private final int PARSE_ID = 1;
    private final int PARSE_ESCAPE = 2;
    private final int PARSE_CONTENT = 3;

    @Override
    protected SetupParser getParser(String text){
        // text.replace(CHAR_ESCAPE + LINED_END, "")
        int state = PARSE_START;
        for (int i = 0; i < text.length(); i++){
            switch (state){
            case PARSE_START:
                if (text.startsWith(LINED_CITE, i)){
                    i++;
                    state = PARSE_CONTENT;
                } else if (text.startsWith(LINED_NOTE, i)){
                    state = PARSE_ID;
                    i++;
                } else {
                    return null;
                }
                break;
            case PARSE_ID:
                if (! CharMatcher.whitespace().matches(text.get(i))){
                    if (text.startsWith(DIRECTORY_BEGIN, i)){
                        return null;
                    }
                } else {
                    state = PARSE_CONTENT;
                }
                break;
            case PARSE_CONTENT:
                if (text.startsWith(LINED_END, i)){
                    state = PARSE_START;
                } else if (text.startsWith(CHAR_ESCAPE, i)){
                    state = PARSE_ESCAPE;
                }
                break;
            case PARSE_ESCAPE:
                state = PARSE_CONTENT;
            }
        }
        return NoteCardParser.PARSER;
    }

    @Override
    protected void childEdited(){
        // TODO childEdit
    }

    @Override
    protected void docEdited(){
        // TODO docEdited
    }
}
