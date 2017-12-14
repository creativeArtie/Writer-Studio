package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

/**
 * Major division of a document, sometimes with a heading.
 */
public class SectionHeading extends Section {
    private Optional<SectionHeading> parentSection;
    private Optional<LinedSpanSection> sectionHeading;
    private ArrayList<SectionOutline> sectionOutlines;
    private ArrayList<SectionHeading> sectionChildren;
    private int sectionLevel;

    SectionHeading(){
        this (0, null, null);
    }

    private SectionHeading(int level, LinedSpanSection heading,
        SectionHeading parent)
    {
        sectionLevel = level;
        sectionHeading = Optional.ofNullable(heading);
        parentSection = Optional.ofNullable(parent);
        sectionOutlines = new ArrayList<>();
        sectionChildren = new ArrayList<>();
    }

    SectionHeading appendHeading(LinedSpanSection line){
        int add = line.getLevel();
        if (add <= sectionLevel){
            return parentSection.get().appendHeading(line);
        }
        if (add == sectionLevel + 1){
            SectionHeading ans = new SectionHeading(sectionLevel + 1, line, this);
            sectionChildren.add(ans);
            return ans;
        }
        assert add > sectionLevel + 1;
        SectionHeading ans = new SectionHeading(sectionLevel + 1, null, this);
        sectionChildren.add(ans);
        return ans.appendHeading(line);
    }

    SectionOutline appendOutline(LinedSpanSection line){
        int depth = line.getLevel();
        if (depth == 1){
            SectionOutline ans = new SectionOutline(depth, line, null, this);
            sectionOutlines.add(ans);
            return ans;
        }
        SectionOutline parent = new SectionOutline(1, null, null, this);
        sectionOutlines.add(parent);
        for(int i = 2; i < depth; i++){
            parent = new SectionOutline(i, null, parent, this);
        }
        parent.append(line);
        return parent;

    }

    public int getLevel(){
        return sectionLevel;
    }

    public Optional<LinedSpanSection> getHeading(){
        return sectionHeading;
    }

    @Override
    public Optional<LinedSpanSection> getLine(){
        return sectionHeading;
    }

    @Override
    public List<SectionHeading> getChildren(){
        return ImmutableList.copyOf(sectionChildren);
    }

    public List<SectionOutline> getOutlines(){
        return ImmutableList.copyOf(sectionOutlines);
    }

    public int getStart(){
        if (sectionHeading.isPresent()){
            return sectionHeading.get().getStart();
        }
        assert sectionChildren.size() > 0;
        return sectionChildren.get(0).getStart();
    }

    public int getEnd(){
        if (sectionHeading.isPresent()){
            return sectionHeading.get().getEnd();
        }
        assert sectionChildren.size() > 0;
        return sectionChildren.get(sectionChildren.size() - 1).getEnd();
    }

    public Optional<SectionHeading> findChild(Optional<LinedSpanSection> search)
    {
        if (! search.isPresent()){
            if (parentSection.isPresent()){
                return parentSection.get().findChild(search);
            } else {
                return Optional.of(this);
            }
        }
        if (sectionHeading.isPresent()){
            if (sectionHeading.get() == search.get()){
                return Optional.of(this);
            }
        }
        for (SectionHeading child: sectionChildren){
            Optional<SectionHeading> gotten = child.findChild(search);
            if (gotten.isPresent()){
                return gotten;
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString(){
        return toString(this, "");
    }
    private static String toString(SectionHeading span, String level){
        /// Set up main prefix:
        String prefix = level + " ";

        /// Add sectionHeading
        StringBuilder builder = new StringBuilder(prefix);
        builder.append("(").append(span.sectionLevel).append(")    ");
        if (span.sectionHeading.isPresent()){
            builder.append(span.sectionHeading.get()).append("\n");
        } else {
            builder.append("(empty)\n");
        }

        /// Add outlines
        String outlinePrefix = prefix + "- ";
        String postfix = "\n";
        String replace = postfix + outlinePrefix;
        if (! span.sectionOutlines.isEmpty()){
            builder.append(outlinePrefix);
            int i = 0;
            for (SectionOutline child: span.sectionOutlines){
                builder.append(child.toString().replace("\n ", replace));
                builder.append(++i == span.sectionOutlines.size()? postfix : replace);
            }
        }

        String levelSetup = level + (level.isEmpty()? "": ".");
        int i = 0;
        for (SectionHeading child : span.sectionChildren){
            builder.append(toString(child, levelSetup + i));
            i++;
            assert i <= span.sectionChildren.size() : span;
        }
        return builder.toString();
    }
}
