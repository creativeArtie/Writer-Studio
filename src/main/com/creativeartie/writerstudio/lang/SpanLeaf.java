package com.creativeartie.writerstudio.lang;

import static com.creativeartie.writerstudio.main.Checker.*;

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
        return "\"" + input.replace("\"", "\" \\\" \"")
            .replace("\n", "\" \\n \"").replace("\t", "\" \\t \"") + "\"";
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
