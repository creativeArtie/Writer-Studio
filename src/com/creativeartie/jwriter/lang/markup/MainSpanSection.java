package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.cache.*;
import com.google.common.collect.*;
import java.util.concurrent.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Doucment section that are set for publish. Represented in design/ebnf.txt as
 * {@code Section}
 */
public class MainSpanSection extends MainSpan {

    MainSpanSection (List<Span> spanChildren){
        super(spanChildren);
    }

    public Optional<LinedSpanLevelSection> getHeading(){
        return findHeading(this);
    }
    private static Optional<LinedSpanLevelSection> findHeading(
            MainSpanSection from){
        Optional<LinedSpanLevelSection> found = spanAtFirst(
            LinedSpanLevelSection.class);
        if (! found.isPresent()){
            /// No section heading == top with no heading
            return Optional.empty();
        }
        found = found.filter(span -> span.getLinedType() == LinedType.HEADING);
        if (found.isPresent()){
            /// Found the heading, doesn't matter if it's top or not
            return found;
        }
        /// So this is an outline search last,
        /// unless the is no last, meaning top with outline
        return from.getLastSection().flatMap(MainSpanSection::findHeading);
    }

    public Optional<LinedSpanLevelSection> getOutline(){
        /// Either is heading (which result to none), outline or no heading
        return spanAtFirst(LinedSpanLevelSection.class)
            .filter(span -> span.getLinedType() == LinedType.OUTLINE);
    }

    public Optional<MainSpanSection> getMainSection(){
        return findMainSection(this);
    }

    private static Optional<MainSpanSection> findMainSection(
            MainSpanSection from){
        Optional<LinedSpanLevelSection> found = from.spanAtFirst(
            LinedSpanLevelSection.class);
        if (! found.isPresent()){
            /// No LinedSpanLevelSection == top
            return Optional.empty();
        }
        if (found.filter(span -> span.getLinedType() == LinedType.HEADING)
                .isPresent()){
            /// Find a heading == answer
            return Optional.of(from);
        }
        assert found.get().getLinedType() == LinedType.OUTLINE: "Wrong type";
        /// Is an outline type, so search last
        /// If there is no last then the top must be a Outline section
        return from.getLastSection().flatMap(MainSpanSection::findMainSection);
    }

    public Optional<MainSpanSection> getParentOutline(){
        if (isOutline()){

        }
    }

    public Optional<MainSpanSection> getParentHeading(){

    }

    @Override
    public List<StyleInfo> getBranchStyles(){
        return ImmutableList.of(AuxiliaryType.MAIN_SECTION);
    }

    public int getLevel(){
        return spanAtFirst(LinedSpanLevelSection.class)
            .map(span -> span.getLevel())
            .orElse(0);
    }

    public boolean isOutline(){
        return spanAtFirst(LinedSpanLevelSection.class)
            .map(span -> span.getLinedType() == LinedType.OUTLINE)
            .orElse(false);
    }

    public boolean isHeading(){
        return spanAtFirst(LinedSpanLevelSection.class)
            .map(span -> span.getLinedType() == LinedType.HEADING)
            .orElse(false);
    }

    public boolean isEmpty(){
        return ! spanAtFirst(LinedSpanLevelSection.class).isPresent();
    }

    public Optional<MainSpanSection> getLastSection(){
        SpanNode<?> parent = getParent();
        int index = parent.indexOf(this);
        assert index != -1;
        if (index == 0) {
            return Optional.empty();
        }
        return Optional.of((MainSpanSection)parent.get(index - 1));
    }


    @Override
    public Optional<CatalogueIdentity> getSpanIdentity(){
        return Optional.of(new CatalogueIdentity(TYPE_SECTION, this));
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
