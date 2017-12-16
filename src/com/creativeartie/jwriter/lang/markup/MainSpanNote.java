package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Research note with headings, ids, and citation.
 */
public class MainSpanNote extends MainSpan {

    MainSpanNote(List<Span> children){
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

    @Override
    protected SetupParser getParser(String text){
        // TODO editRaw
        return null;
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
