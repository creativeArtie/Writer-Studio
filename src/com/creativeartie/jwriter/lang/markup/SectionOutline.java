package com.creativeartie.jwriter.lang.markup;

import com.creativeartie.jwriter.lang.*;

import java.util.*;
import com.google.common.collect.*;

/**
 * Minor division of a document, sometimes with an outline point.
 */
public class SectionOutline extends Section{

    private SectionHeading parentSection;
    private Optional<LinedSpanLevelSection> outlineHeading;
    private Optional<SectionOutline> parentOutline;
    private ArrayList<SectionOutline> childrenOutline;
    private int outlineLevel;

    SectionOutline(int level, LinedSpanLevelSection heading, SectionOutline parent,
        SectionHeading section)
    {
        parentSection = section;
        outlineHeading = Optional.ofNullable(heading);
        parentOutline = Optional.ofNullable(parent);
        outlineLevel = level;
        childrenOutline = new ArrayList<>();
    }

    Optional<SectionOutline> append(LinedSpanLevelSection section){
        int level = section.getLevel();
        if (level <= outlineLevel){
            if (parentOutline.isPresent()){
                return parentOutline.get().append(section);
            } else {
                return Optional.empty();
            }
        }
        if (level == outlineLevel + 1){
            SectionOutline ans = new SectionOutline(outlineLevel + 1, section,
                this, parentSection);
            childrenOutline.add(ans);
            return Optional.of(ans);
        }
        assert level > outlineLevel + 1;
        SectionOutline ans = new SectionOutline(outlineLevel + 1,
            null, this, parentSection);
        childrenOutline.add(ans);
        return ans.append(section);
    }

    public SectionHeading getHeading(){
        return parentSection;
    }

    @Override
    public Optional<LinedSpanLevelSection> getLine(){
        return outlineHeading;
    }

    public Optional<SectionOutline> getParent() {
        return parentOutline;
    }


    @Override
    public ImmutableList<SectionOutline> getChildren(){
        return ImmutableList.copyOf(childrenOutline);
    }

    @Override
    public String toString(){
        return toString(this, 0);
    }

    private String toString(SectionOutline span, int tabs){
        StringBuilder builder = new StringBuilder();
        if (span.outlineHeading.isPresent()){
            builder.append(SpanLeaf.escapeText(span.outlineHeading.get().getRaw()));
        } else {
            builder.append("(empty)");
        }
        span.childrenOutline.forEach((child) -> {
            builder.append(addLine(tabs));
            builder.append(toString(child, tabs + 1));
        });
        return builder.toString();
    }

    private String addLine(int tabs){
        String ans = "\n";
        for (int i = 0; i <= tabs; i++){
            ans += "  ";
        }
        return ans;
    }

    public int getLevel() {
        return outlineLevel;
    }
}
