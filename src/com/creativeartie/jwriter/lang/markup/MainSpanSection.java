package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.cache.*;
import com.google.common.collect.*;
import java.util.concurrent.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Doucment section that are set for publish.
 */
public class MainSpanSection extends MainSpan {

    private final Cache<String, Optional<LinedSpanSection>> lineCache;
    private final Cache<String, Optional<MainSpanSection>> sectionCache;

    MainSpanSection (List<Span> spanChildren){
        super(spanChildren);
        lineCache = CacheBuilder.newBuilder().build();
        sectionCache = CacheBuilder.newBuilder().build();
    }

    public EditionType getEdition(){
        try {
            MainSpanSection span = sectionCache.get("Edition", () ->{
                MainSpanSection ans = this;
                Optional<MainSpanSection> ptr = getLastPart();
                while (ptr.isPresent()){
                    ans = ptr.get();
                    ptr = ans.getLastPart();
                }
                return Optional.of(ans);
            }).get();
            return span.spanFromFirst(LinedSpanSection.class).map(line ->
                line.getEdition()).orElse(EditionType.NONE);
        } catch (ExecutionException ex){
            throw new RuntimeException(ex);
        }
    }


    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(AuxiliaryType.MAIN_SECTION);
    }

    private Optional<MainSpanSection> next(){
        try {
            return sectionCache.get("next", () -> serach(false));
         } catch (ExecutionException ex){
            throw new RuntimeException(ex);
        }
    }

    private Optional<MainSpanSection> last(){
        try {
            return sectionCache.get("last", () -> serach(true));
         } catch (ExecutionException ex){
            throw new RuntimeException(ex);
        }
    }

    private Optional<MainSpanSection> serach(boolean toLast){
        Document doc = getDocument();
        int idx = doc.indexOf(this);
        SpanBranch siblings;
        do{
            idx += toLast? -1: 1;
            if (idx < 0 || idx >= doc.size()){
                return Optional.empty();
            }
            siblings = doc.get(idx);
        } while(! (siblings instanceof MainSpanSection));
        return Optional.of((MainSpanSection) siblings);
    }

    public Optional<LinedSpanSection> getSelfSection(){
        try {
            return lineCache.get("selfSection", () -> {
                return spanAtFirst(LinedSpanSection.class);
            });
        } catch (ExecutionException ex){
            throw new RuntimeException(ex);
        }
    }

    public Optional<MainSpanSection> getLastPart(){
        try {
            return sectionCache.get("lastPart", () -> {
                if (get(0) instanceof LinedSpanSection){
                    return Optional.empty();
                }
                return last();
            });
        } catch (ExecutionException ex){
            throw new RuntimeException(ex);
        }
    }

    public Optional<MainSpanSection> getNextPart(){
        try {
            return sectionCache.get("nextPart", () -> {
                Optional<MainSpanSection> last = next();
                if (! last.isPresent()){
                    return Optional.empty();
                }
                MainSpanSection span = last.get();
                if (span.get(0) instanceof LinedSpanSection){
                    return Optional.empty();
                }
                return last;
            });
        } catch (ExecutionException ex){
            throw new RuntimeException(ex);
        }
    }

    public Optional<LinedSpanSection> getHeading(){
        try {
            return lineCache.get("heading", () -> {
                Optional<LinedSpanSection> first = spanAtFirst(LinedSpanSection.class);
                if(first.isPresent() && first.get().getLinedType() == LinedType.HEADING){
                    return first;
                }
                Optional<MainSpanSection> last = last();
                if (last.isPresent()){
                    return last.get().getHeading();
                }
                return Optional.empty();
            });
        } catch (ExecutionException ex){
            throw new RuntimeException(ex);
        }
    }

    public Optional<LinedSpanSection> getOutline(){
        try {
            return lineCache.get("outline", () -> {
                Optional<LinedSpanSection> first = spanAtFirst(LinedSpanSection.class);
                if(first.isPresent()){
                    LinedSpanSection level = first.get();
                    if (level.getLinedType() == LinedType.HEADING){
                        return Optional.empty();
                    }
                    assert LinedType.OUTLINE == level.getLinedType();
                    return first;
                }
                Optional<MainSpanSection> last = last();
                if (last.isPresent()){
                    return last.get().getOutline();
                }
                return Optional.empty();
            });
        } catch (ExecutionException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("SECTION:{");
        for(Span s: this){
            output.append("\n\t").append(s);
        }
        output.append("\n}");
        return output.toString();
    }

    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return Optional.ofNullable(getLastPart().isPresent() ? null:
            new CatalogueIdentity(TYPE_SECTION, this));
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
