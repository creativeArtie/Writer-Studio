package com.creativeartie.jwriter.lang;

import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.jwriter.property.*;
import com.creativeartie.jwriter.main.*;

import static com.creativeartie.jwriter.main.Checker.*;

/**
 * A {@link Span} storing the raw text.
 */
public class SpanLeaf extends Span{
    private final String leafText;
    private SpanBranch leafParent;
    private final StyleInfoLeaf leafStyle;

    /// Don't infer by looking up parents, it is needed before the parent is set
    private Document spanDoc;

    /** create raw text for {@linkplain Object#toString()}. */
    public static String escapeText(String input){
        checkNotNull(input, "input");
        return "\"" + input.replace("\"", "\" \\\" \"") /// replace quote
            .replace("\n", "\" \\n \"") /// replace new line
            .replace("\t", "\" \\t \"") + "\""; ///replace tabs
    }

    SpanLeaf(SetupPointer pointer, StyleInfoLeaf style){
        checkNotNull(pointer, "pointer");
        checkNotNull(style, "style");
        leafText = pointer.getRaw();
        pointer.roll();
        leafStyle = style;
        spanDoc = pointer.getDocument();
    }

    public StyleInfoLeaf getLeafStyle(){
        return leafStyle;
    }

    void setParent(SpanBranch childOf){
        checkNotNull(childOf, "childOf");
        leafParent = childOf;
    }

    /** Gets the parent that the subclasses {@link Class}*/
    public <T> Optional<T> getParent(Class<T> clazz){
        checkNotNull(clazz, "clazz");
        ImmutableList.Builder<SpanBranch> builder = ImmutableList.builder();
        SpanNode<?> parent = getParent();
        while(parent instanceof SpanBranch){
            if (clazz.isInstance(parent)){
                /// Finds a match
                return Optional.of(clazz.cast(parent));
            }
            /// get the parent
            parent = parent.getParent();
        }
        /// Not found.
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
        return spanDoc;
    }

    @Override
    public SpanBranch getParent(){
        return leafParent;
    }

    @Override
    public int getLocalEnd(){
        return leafText.length();
    }

    @Override
    public void setRemove(){}

    @Override
    public void setUpdated(){
        leafParent.setUpdated();
    }

    @Override
    protected void docEdited(){}
}
