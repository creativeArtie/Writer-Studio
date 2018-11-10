package com.creativeartie.writerstudio.lang;

import java.util.*;

import com.google.common.base.*;

import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** Pointer for text.
 *
 * Purpose
 * <ul>
 * <li>Marks a location to go roll back later.</li>
 * <li>Create {@link SpanLeaf} under a condition.</li>
 * <li>Other checking methods</li>
 * </ul>
 */
public final class SetupPointer{
    /// %Part 1: Constructors and Fields #######################################
    /// %Part 1.1: Static Constructos ==========================================

    /** Create a {@link SetupPointer}.
     *
     * @param raw
     *      raw text
     * @param doc
     *      span's document
     * @see Document#parseDocument()
     */
    static SetupPointer newPointer(String raw, Document doc){
        argumentNotNull(raw, "raw");
        argumentNotNull(doc, "doc");

        return new SetupPointer(raw, doc, true);
    }

    /** Create a {@link SetupPointer}.
     * @param raw
     *      raw text
     * @param doc
     *      span's document
     * @see SpanBranch#reparseText()
     */
    static SetupPointer updatePointer(String raw, Document doc,
        boolean first
    ){
        argumentNotEmpty(raw, "raw");
        argumentNotNull(doc, "doc");

        return new SetupPointer(raw, doc, first);
    }

    /// %Part 1.1: Instance Constructor ========================================

    private final String rawText;
    private final Document document;
    private final boolean isFirst;

    private int lastMarker; /// marker
    private int matchMarker; /// match from
    private int nextMarker; /// correct to


    /** Creates a {@link SetupPointer}.
     *
     * @param raw
     *      raw text
     * @param doc
     *      span's document
     */
    private SetupPointer(String raw, Document doc, boolean first){
        assert raw != null: "Null raw";
        assert doc != null: "Null doc";

        rawText = raw;
        document = doc;
        isFirst = first;

        matchMarker = 0;
        nextMarker = 0;
        lastMarker = 0;
    }

    /// %Part 2: Marking Location ##############################################

    /** mark the current location. */
    public void mark(){
        lastMarker = nextMarker;
    }

    /** Go back to last point called by {@link #mark()}. */
    public void rollBack(){
        matchMarker = lastMarker;
        nextMarker = lastMarker;
    }

    /// %Part 3: Check and Add Methods #########################################
    /// %Part 3.1: Trim Start With =============================================

    /** Next {@link SpanLeaf} prefixed with spaces matches {@code compare}.
     *
     * The leaf's {@link SpanLeafStyle} sets to
     * {@link SpanLeafStyle#KEYWORD}.
     *
     * @param children
     *      adding children list
     * @param compare
     *      compare text
     * @return success
     */
    public boolean trimStartsWith(ArrayList<Span> children, String compare){
        argumentNotNull(children, "children");
        argumentNotEmpty(compare, "compare");

        return trimStartsWith(children, SpanLeafStyle.KEYWORD, compare);
    }

    /** Next {@link SpanLeaf} prefixed with spaces matches {@code compare}.
     *
     * @param children
     *      adding children list
     * @param style
     *      new span leaf style
     * @param compare
     *      compare text
     * @return success
     */
    public boolean trimStartsWith(ArrayList<Span> children, SpanLeafStyle style,
            String compare){
        argumentNotNull(children, "children");
        argumentNotNull(style, "style");
        argumentNotEmpty(compare, "compare");

        /// End of the document
        if (matchMarker >= rawText.length()){
            return false;
        }

        /// Ignore spaces
        int next = matchMarker;
        while(CharMatcher.whitespace().matches(rawText.charAt(next))){
            next++;
            if (next >= rawText.length()){
                break;
            }
        }

        /// compare here
        if (rawText.startsWith(compare, next)){
            nextMarker = next + compare.length();
            return addChild(children, style);
        }
        return false;
    }

    /// %Part 3.2: Start With Methods ==========================================

    /** Next {@link SpanLeaf} matches {@code compare}.
     *
     * The leaf's {@link SpanLeafStyle} sets to
     * {@link SpanLeafStyle#KEYWORD}.
     *
     * @param children
     *      adding children list
     * @param compare
     *      compare text
     * @return success
     */
    public boolean startsWith(ArrayList<Span> children, String compare){
        argumentNotNull(children, "children");
        argumentNotEmpty(compare, "compare");

        return startsWith(children, SpanLeafStyle.KEYWORD, compare);
    }

    /** Next {@link SpanLeaf} matches {@code compare}.
     *
     * The leaf's {@link SpanLeafStyle} sets to
     * {@link SpanLeafStyle#KEYWORD}.
     *
     * @param children
     *      adding children list
     * @param style
     *      new span leaf style
     * @param compare
     *      compare text
     * @return success
     */
    public boolean startsWith(ArrayList<Span> children, SpanLeafStyle style,
        String compare
    ){
        argumentNotNull(children, "children");
        argumentNotNull(style, "style");
        argumentNotEmpty(compare, "compare");

        int next = matchMarker;
        if (rawText.startsWith(compare, next)){
            nextMarker = next + compare.length();

            return addChild(children, style);
        }
        return false;
    }

    /// %Part 3.3: CharMacher Methods ==========================================

    /** Next {@link SpanLeaf} has characters matches {@link CharMatcher}.
     *
     * The leaf's {@link SpanLeafStyle} sets to
     * {@link SpanLeafStyle#KEYWORDS}.
     *
     * @param children
     *      adding children list
     * @param matcher
     *      characters matcher
     * @return success
     */
    public boolean matches(ArrayList<Span> children, CharMatcher matcher){
        argumentNotNull(children, "children");
        argumentNotNull(matcher, "matcher");

        return matches(children, SpanLeafStyle.KEYWORD, matcher);
    }


    /** Next {@link SpanLeaf} has characters matches {@link CharMatcher}.
     *
     * @param children
     *      adding children list
     * @param style
     *      new span leaf style
     * @param matcher
     *      characters matcher
     * @return success
     */
    public boolean matches(ArrayList<Span> children, SpanLeafStyle style,
        CharMatcher matcher
    ){
        argumentNotNull(children, "children");
        argumentNotNull(style, "style");
        argumentNotNull(matcher, "matcher");

        int next = matchMarker;
        for (; rawText.length() > next; next++){
            if (! matcher.matches(rawText.charAt(next))){
                /// get to the last char + 1 that matches the matcher
                if (next != matchMarker){
                    nextMarker = next;
                    return addChild(children, style);
                } else {
                    return false;
                }
            }
        }

        /// End of document
        if (next != matchMarker){
            /// matchMarker has moved
            nextMarker = next;
            return addChild(children, style);
        }
        return false;
    }

    /// Part 3.4: Get To Methods ===============================================

    /** Next {@link SpanLeaf} continues until one of a the text.
     *
     * The leaf's {@link SpanLeafStyle} sets to {@link SpanLeafStyle#KEYWORD}.
     *
     * @param children
     *      adding children list
     * @param enders
     *      enders text
     * @return success
     */
    public boolean getTo(ArrayList<Span> children, String ... enders){
        argumentNotNull(children, "children");
        argumentNotNull(enders, "enders");

        return getTo(children, SpanLeafStyle.KEYWORD, enders);
    }

    /** Next {@link SpanLeaf} continues until one of a the text.
     *
     * The leaf's {@link SpanLeafStyle} sets to
     * {@link SpanLeafStyle#KEYWORD}.
     *
     * @param children
     *      adding children list
     * @param enders
     *      enders text
     * @return success
     */
    public boolean getTo(ArrayList<Span> children, SpanLeafStyle style,
        String ... enders
    ){
        argumentNotNull(children, "children");
        argumentNotNull(style, "style");
        argumentNotNull(enders, "enders");

        return getTo(children, style, Arrays.asList(enders));
    }


    /** Next {@link SpanLeaf} continues until one of a the text.
     *
     * The leaf's {@link SpanLeafStyle} sets to
     * {@link SpanLeafStyle#KEYWORD}.
     *
     * @param children
     *      adding children list
     * @param enders
     *      enders text
     * @return success
     */
    public boolean getTo(ArrayList<Span> children, List<String> enders){
        argumentNotNull(children, "children");
        argumentNotNull(enders, "enders");

        return getTo(children, SpanLeafStyle.KEYWORD, enders);
    }

    /** Next {@link SpanLeaf} continues until one of a the text.
     *
     * @param children
     *      adding children list
     * @param style
     *      new span leaf style
     * @param enders
     *      enders text
     * @return success
     */
    public boolean getTo(ArrayList<Span> children, SpanLeafStyle style,
        List<String> enders
    ){
        argumentNotNull(children, "children");
        argumentNotNull(style, "style");
        argumentNotNull(enders, "enders");

        /// Set up next
        int next = matchMarker;
        /// going the the text from pointer, looking out for enders strings
        for(;next < rawText.length(); next++){
            for (String ender : enders){
                if (rawText.startsWith(ender, next)){
                    /// Match is found
                    nextMarker = next;
                    if(nextMarker != matchMarker){
                        return addChild(children, style);
                    }
                    return false;
                }
            }
        }

        if (matchMarker != next){
            /// MatchMarker has moved
            nextMarker = next;
            return addChild(children, style);
        }

        return false;
    }

    /// Part 3.4: Next Char Methods ============================================

    /** Next {@link SpanLeaf} with a certain size.
     *
     * The leaf's {@link SpanLeafStyle} sets to
     * {@link SpanLeafStyle#KEYWORD}.
     *
     * @param children
     *      adding children list
     * @param size
     *      character length size
     * @return success
     */
    public boolean nextChars(ArrayList<Span> children, int size){
        argumentNotNull(children, "children");
        argumentAtLeast(size, "size", 1);
        return nextChars(children, SpanLeafStyle.KEYWORD, size);
    }

    /** Next {@link SpanLeaf} with a certain size.
     *
     * @param children
     *      adding children list
     * @param style
     *      new span leaf style
     * @param size
     *      character length size
     * @return success
     */
    public boolean nextChars(ArrayList<Span> children, SpanLeafStyle style,
        int size
    ){
        argumentNotNull(children, "children");
        argumentNotNull(style, "style");
        argumentAtLeast(size, "size", 1);

        if (matchMarker + size <= rawText.length()){
            nextMarker = matchMarker + size;

            return addChild(children, style);
        }
        return false;
    }

    /// %Part 3.4: Span Building ===============================================

    /** Creates a new {@link SpanLeaf} and return true.
     *
     * @return answer
     * @see #trimStartsWith(ArrayList, SpanLeafStyle, String)
     * @see #startsWith(ArrayList, SpanLeafStyle, String)
     * @see #matches(ArrayList, SpanLeafStyle, CharMatcher)
     * @see #getTo(ArrayList, SpanLeafStyle, List)
     * @see nextChars(ArrayList, SpanLeafStyle, int)
     */
    private boolean addChild(ArrayList<Span> children, SpanLeafStyle style){
        children.add(new SpanLeaf(this, style));
        return true;
    }

    /// %Part 4: Check Methods #################################################

    /** CHeck if the point is at the beginning of the document.
     */
    public boolean isFirst(){
        return isFirst && matchMarker == 0;
    }

    /** Checks if there is still text to be parsed.
     *
     * @return answer
     */
    public boolean hasNext(){
        return matchMarker < rawText.length();
    }

    /** Checks if the next text matching in a list of strings.
     *
     * @param strings
     *      strings to match
     * @return answer
     */
    public boolean hasNext(String ... strings){
        return hasNext(Arrays.asList(strings));
    }

    /** Checks if the next text matching in a list of strings.
     *
     * @param strings
     *      strings to match
     * @return answer
     */
    public boolean hasNext(List<String> strings){
        for (String string: strings){
            if (hasNext(string)){
                return true;
            }
        }
        return false;
    }

    /** Checks if the next text matching a string.
     *
     * @param string
     *      string to match
     * @return answer
     */
    public boolean hasNext(String string){
        return rawText.startsWith(string, matchMarker);
    }

    /// %Part 5: Span Leaf Creation Getters ####################################

    /** Gets the raw text for the {@link SpanLeaf}.
     *
     * @return answer
     * @see SpanLeaf#SpanLeaf(SetupPointer, SpanLeafStyle)
     */
    String getRaw(){
        return rawText.substring(matchMarker, nextMarker);
    }

    /** Gets the raw text for the {@link SpanLeaf}.
     *
     * @return answer
     * @see SpanLeaf#SpanLeaf(SetupPointer, SpanLeafStyle)
     */
    Document getDocument(){
        return document;
    }

    /** Move the pointer forwards.
     *
     * @return answer
     * @see SpanLeaf#SpanLeaf(SetupPointer, SpanLeafStyle)
     */
    void roll(){
        matchMarker = nextMarker;
    }

    /// %Part 6: Overriding Methods ============================================

    @Override
    public String toString(){
        return "(" + pointerHelper(lastMarker) + "-" +
            pointerHelper(matchMarker) + "-" +
            pointerHelper(nextMarker) + "): " +
            rawText.substring(matchMarker);
    }

    /**
     * Print either the location or "end" with bracket.
     *
     * @param ptr
     *      interested pointer
     * @return answer;
     */
    private String pointerHelper(int ptr){
        assert ptr >= 0: "Too low ptr: " + ptr;
        assert ptr <= rawText.length(): "Too high ptr: " + ptr;
        if (ptr < rawText.length()){
            return ptr + "(" + rawText.charAt(ptr) + ")";
        }
        return ptr + "(end)";
    }

}
