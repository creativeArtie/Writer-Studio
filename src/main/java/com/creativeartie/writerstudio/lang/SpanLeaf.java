package com.creativeartie.writerstudio.lang;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A {@link Span} storing the raw text. */
public final class SpanLeaf extends Span{
    /// %Part 1: Non-override Methods ##########################################

    /// %Part 1.1: Constructor & Fields ========================================

    private final String leafText;
    private SpanBranch leafParent;
    private final SpanLeafStyle leafStyle;
    /// Don't infer by looking up parents, it is needed before the parent is set
    private Document spanDoc;

    /** Create a {@link SpanLeaf}.
     *
     * @param pointer
     *      data input pointer
     * @param style
     *      leaf info style
     */
    SpanLeaf(SetupPointer pointer, SpanLeafStyle style){
        argumentNotNull(pointer, "pointer");
        leafText = pointer.getRaw();
        pointer.roll();
        leafStyle = argumentNotNull(style, "style");
        spanDoc = pointer.getDocument();
    }

    /// %Part 1.2: Leaf style ==================================================

    /** Get the leaf style
     *
     * @return answer
     */
    public SpanLeafStyle getLeafStyle(){
        return leafStyle;
    }

    /** create raw text for {@linkplain Object#toString()}.
     *
     * @param input
     *      text to escape
     * @return answer
     */
    public static String escapeText(String input){
        argumentNotNull(input, "input");
        return "\"" + input.replace("\"", "\" \\\" \"")
            .replace("\n", "\" \\n \"").replace("\t", "\" \\t \"") + "\"";
    }

    /// %Part 2: Overrding Methods #############################################

    @Override
    void setParent(SpanNode<?> parent){
        argumentClass(parent, "parent", SpanBranch.class);
        leafParent = (SpanBranch) parent;
    }

    @Override
    public final String getRaw(){
        return leafText;
    }

    @Override
    public int getLocalEnd(){
        return leafText.length();
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
    public String toString(){
        return escapeText(leafText);
    }
}
