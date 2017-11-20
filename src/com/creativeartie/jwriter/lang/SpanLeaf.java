package com.creativeartie.jwriter.lang;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.property.*;
import com.creativeartie.jwriter.main.*;

/**
 * A {@link Span} handling markup editor highlighting, export node creation and
 * formatting for GUI editor. The constructor will get the pointer to roll (move
 * {@linkplain start} pointer to {@linkplain end} pointer).
 */
public class SpanLeaf extends Span{
    private final String leafText;
    private SpanBranch leafParent;
    private final SetupLeafStyle leafStyle;

    public static String escapeText(String input){
        return "\"" + input.replace("\"", "\" \\\" \"")
            .replace("\n", "\" \\n \"").replace("\t", "\" \\t \"") + "\"";
    }

    SpanLeaf(SetupPointer pointer, SetupLeafStyle style){
        leafText = pointer.getRaw();
        pointer.roll();
        leafStyle = style;
    }

    public SetupLeafStyle getLeafStyle(){
        return leafStyle;
    }

    public boolean containsStyle(DetailStyle style){
        if (leafStyle != style){
            if (! leafParent.getBranchStyles().contains(style)){
                SpanNode<?> parent = leafParent.getParent();
                while(parent instanceof SpanBranch){
                    if(((SpanBranch)parent).getBranchStyles().contains(style)){
                        return true;
                    }
                    parent = parent.getParent();
                }
            }
            return false;
        }
        return true;
    }

    void setParent(SpanBranch childOf){
        leafParent = childOf;
    }

    public <T> Optional<T> getParent(Class<T> clazz){
        ImmutableList.Builder<SpanBranch> builder = ImmutableList.builder();
        SpanNode<?> parent = getParent();
        while(parent instanceof SpanBranch){
            if (clazz.isInstance(parent)){
                return Optional.of(clazz.cast(parent));
            }
            parent = parent.getParent();
        }
        return Optional.empty();
    }

    @Override
    public final String getRaw(){
        return leafText;
    }

    @Override
    public String toString(){
        return escapeText(leafText);
    }

    @Override
    public Document getDocument(){
        SpanNode<?> span = getParent();
        while (! (span instanceof Document)){
            span = span.getParent();
        }
        return (Document)span;
    }

    @Override
    public SpanBranch getParent(){
        return leafParent;
    }

    @Override
    public int getLength(){
        return leafText.length();
    }

    @Override
    public void setRemove(){}

    @Override
    public void setEdit(){
        leafParent.setEdit();
    }
}
