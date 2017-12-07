package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.base.*;

import static com.google.common.base.Preconditions.*;

/**
 * SetupPointer for the rawText. Stores two points: {@linkplain start} for where the
 * pointer is currently checking from and {@linkplain end} for where is the
 * next matching data.
 */
public final class SetupPointer{
    /// ========================================================================
    /// Part 1: Static Methods -------------------------------------------------

    /** Create a {@link SetupPointer} with the purpose to parse full text. */
    static SetupPointer newPointer(String raw, Document doc){
        checkNotNull(Strings.emptyToNull(raw), "Raw cannot be empty or null.");
        checkNotNull(doc, "Document object cannot be null.");

        return new SetupPointer(raw, doc);
    }

    /** Create a {@link SetupPointer} with the purpose to reparse text. */
    static SetupPointer updatePointer(String raw, Document doc){
        checkNotNull(Strings.emptyToNull(raw), "Raw cannot be empty or null.");
        checkNotNull(doc, "Document object cannot be null.");

        return new SetupPointer(raw, doc);
    }

    /// ========================================================================
    /// Part 2: Object Fields --------------------------------------------------

    private final String rawText;
    private final Document document;

    /// Points to where the pointer would roll back to
    private int lastMarker;
    /// Points to the matching start
    private int matchMarker;
    /// Points to the last success (but not start at) check
    private int nextMarker;

    private SetupPointer(String raw, Document doc){
        assert raw != null && ! raw.isEmpty(): "Empty or Null raw";
        assert doc != null: "Null doc";
        rawText = raw;
        document = doc;
        matchMarker = 0;
        nextMarker = 0;
        lastMarker = 0;
    }

    /// ========================================================================
    /// Part 3: Marking Location -----------------------------------------------

    public void mark(){
        lastMarker = nextMarker;
    }

    /** Go back to last point called by {@link #mark()}. */
    public void rollBack(){
        matchMarker = lastMarker;
        nextMarker = lastMarker;
    }

    /**
     * Create a {@link SpanLeaf} with spaces and then {@code compare} text. The
     * created {@linkplain SpanLeaf} will have the
     * {@link SetupLeafStyle#KEYWORD}.
     */
    public boolean trimStartsWith(ArrayList<Span> children, String compare){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(compare, "Compare string cannot be null.");

        return trimStartsWith(children, SetupLeafStyle.KEYWORD, compare);
    }

    /// ========================================================================
    /// Part 4: trimStartWith Methods ------------------------------------------

    /** Create a {@link SpanLeaf} with spaces and then {@code compare} text. */
    public boolean trimStartsWith(ArrayList<Span> children,
            SetupLeafStyle style, String compare){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(style, "Leaf style cannot be null.");
        checkNotNull(compare, "Compare string cannot be null.");
        return finishing(children, style, trimStartsWith(compare));
    }

    /**
     * Check if the pointer starts with space and then {@code compare} text. If
     * so move {@linkplain end} to the length of {@linkplain compare}. Helper
     * method of {@link #trimStartsWith(ArrayList, String)}, and
     * {@link #trimStartsWith(ArrayList, SetupLeafStyle, String)}.
     */
    private boolean trimStartsWith(String compare){
        assert compare != null: "Null compare";
        /// End of the document
        if (matchMarker >= rawText.length()){
            return false;
        }

        /// Ignore spaces
        int next = ignoreSpaces();
        if (next == -1){
            return false;
        }

        /// compare here
        if (rawText.startsWith(compare, next)){
            nextMarker = next + compare.length();
            return true;
        }
        return false;
    }

    /**
     * Find the of the next index of non-space character. Helper method of
     * {@link #trimStartsWith(String)}.
     */
    private int ignoreSpaces(){
        if (matchMarker >= rawText.length()){
            return -1;
        }
        int next = matchMarker;
        while(CharMatcher.whitespace().matches(rawText.charAt(next))){
            next++;
            if (next >= rawText.length()){
                return -1;
            }
        }
        return next;
    }

    /// ========================================================================
    /// Part 5: startWith Methods ----------------------------------------------

    /**
     * Create a {@link SpanLeaf} with {@code compare} text. The created
     * {@linkplain SpanLeaf} will have the {@link SetupLeafStyle#KEYWORD}.
     */
    public boolean startsWith(ArrayList<Span> children, String compare){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(compare, "Compare string cannot be null.");

        return startsWith(children, SetupLeafStyle.KEYWORD, compare);
    }

    /**  Create a {@link SpanLeaf} with {@code compare} text. */
    public boolean startsWith(ArrayList<Span> children, SetupLeafStyle style,
            String compare){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(style, "Leaf style cannot be null.");
        checkNotNull(compare, "Compare string cannot be null.");

        return finishing(children, style, startsWith(compare));
    }

    /**
     * Check if the pointer at the text location starts with
     * {@linkplain compare}. If so move {@linkplain end} to the length of
     * {@linkplain compare}. Helper method of
     * {@link #startsWith(ArrayList, String)}, and
     * {@link #startsWith(ArrayList, SetupLeafStyle, String)}.
     */
    private boolean startsWith(String compare){
        assert compare != null: "Null compare.";

        int next = matchMarker;
        if (rawText.startsWith(compare, next)){
            nextMarker = next + compare.length();

            return true;
        }
        return false;
    }

    /// ========================================================================
    /// Part 6: matches Methods ------------------------------------------------

    /**
     * Create a {@link SpanLeaf} that matches {@link CharMatcher}. The created
     * {@linkplain SpanLeaf} will have the {@link SetupLeafStyle#DATA}.
     */
    public boolean matches(ArrayList<Span> children, CharMatcher matcher){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(matcher, "CharMatcher cannot be null.");
        return matches(children, SetupLeafStyle.DATA, matcher);
    }

    /** Create a {@link SpanLeaf} that matches {@link CharMatcher}. */
    public boolean matches(ArrayList<Span> children, SetupLeafStyle style,
            CharMatcher matcher){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(style, "Leaf style cannot be null.");
        checkNotNull(matcher, "CharMatcher cannot be null.");

        return finishing(children, style, matches(matcher));
    }

    /**
     * Check if the pointer at the text location matches
     * {@linkplain CharMatcher}. If so move {@linkplain end} to the length of
     * {@linkplain compare}. Helper method of
     * {@link #matches(ArrayList, String)}, and
     * {@link #matches(ArrayList, SetupLeafStyle, String)}.
     */
    private boolean matches(CharMatcher matches){
        int next = matchMarker;
        for (; rawText.length() > next; next++){
            if (! matches.matches(rawText.charAt(next))){
                // get to the last char + 1 that matches the matcher
                if (next != matchMarker){
                    nextMarker = next;
                    return true;
                } else {
                    return false;
                }
            }
        }

        // End of document
        if (next != matchMarker){
            //matchMarker has moved
            nextMarker = next;
            return true;
        }
        return false;
    }

    /// ========================================================================
    /// Part 7: getTo Methods --------------------------------------------------

    /**
     * Create a {@link SpanLeaf} that stop before {@code compares}. The created
     * {@linkplain SpanLeaf} will have the {@link SetupLeafStyle#KEYWORD}.
     */
    public boolean getTo(ArrayList<Span> children, List<String> compares){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(compares, "Compare list cannot be null.");

        return getTo(children, SetupLeafStyle.KEYWORD, compares);
    }

    /** Create a {@link SpanLeaf} that stop before {@code compares}. */
    public boolean getTo(ArrayList<Span> children, SetupLeafStyle style,
            List<String> compares){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(style, "Leaf style cannot be null.");
        checkNotNull(compares, "Compare list cannot be null.");

        return finishing(children, style, getTo(compares));
    }

    /**
     * Create a {@link SpanLeaf} that stop before {@code compares}. The created
     * {@linkplain SpanLeaf} will have the {@link SetupLeafStyle#KEYWORD}.
     */
    public boolean getTo(ArrayList<Span> children, String ... compares){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(compares, "Compare list cannot be null.");

        return getTo(children, SetupLeafStyle.KEYWORD, compares);
    }

    /** Create a {@link SpanLeaf} that stop before {@code compares}. */
    public boolean getTo(ArrayList<Span> children, SetupLeafStyle style,
            String ... compares){
        checkNotNull(children, "Children list cannot be null.");
        checkNotNull(style, "Leaf style cannot be null.");
        checkNotNull(compares, "Compare list cannot be null.");

        return finishing(children, style, getTo(Arrays.asList(compares)));
    }

    /**
     * Moves {@linkplain end} pointer to before one of the {@linkplain enders}
     * or the end of the rawText. Helper method for
     * {@link getTo(ArrayList, List)},
     * {@link getTo(ArrayList, SetupLeafStyle, List)},
     * {@link getTo(ArrayList, String ...)}, and
     * {@link getTo(ArrayList, SetupLeafStyle, String ...)}
     */
    private boolean getTo(List<String> enders) {
        assert enders != null: "Null enders.";

        /// Set up next
        int next = matchMarker;
        /// going the the text from pointer, looking out for ender strings
        for(;next < rawText.length(); next++){
            for (String ender : enders){
                if (rawText.startsWith(ender, next)){
                    /// Match is found
                    nextMarker = next;
                    return nextMarker != matchMarker;
                }
            }
        }

        if (matchMarker != next) {
            //matchMarker has moved
            nextMarker = next;
            return true;
        }

        return false;
    }

    /// ========================================================================
    /// Part 8: nestChars Methods ----------------------------------------------

    /**
     * Create a {@link SpanLeaf} with a size. The created
     * {@linkplain SpanLeaf} will have the {@link SetupLeafStyle#KEYWORD}.
     */
    public boolean nextChars(ArrayList<Span> children, int size){
        return nextChars(children, SetupLeafStyle.KEYWORD, size);
    }

    /** Create a {@link SpanLeaf} with a size. */
    public boolean nextChars(ArrayList<Span> children, SetupLeafStyle style,
            int size){
        return finishing(children, style, nextChars(size));
    }

    /**
     * Moves {@linkpalin end} pointer to {@linkplain size}, if possible. If not
     * possible do not move {@linkplain end}. Helper method of
     * {@link #nextChars(ArrayList, int)}, and
     * {@link #nextChars(ArrayList, SetupLeafStyle, int)}.
     */
    private boolean nextChars(int size) {
        if (matchMarker + size <= rawText.length()){
            nextMarker = matchMarker + size;

            return true;
        }
        return false;
    }

    /// ========================================================================
    /// Part 9: Span Building --------------------------------------------------

    /** Setup for creating a {@link SpanLeaf}. Helper method for Part 4 - 8 */
    private boolean finishing(ArrayList<Span> children, SetupLeafStyle style,
            boolean matches){
        if(matches){
            children.add(new SpanLeaf(this, style));
        }
        return matches;
    }

    /** Checks if there is still text to be parsed. */
    public boolean hasNext(){
        return matchMarker < rawText.length();
    }

    /** Checks if there is still text to be parsed. */
    protected String getRaw(){
        return rawText.substring(matchMarker, nextMarker);
    }

    /** Checks if there is still text to be parsed. */
    protected Document getDocument(){
        return document;
    }

    /** Move the pointer forwards. */
    protected void roll(){
        matchMarker = nextMarker;
    }

    @Override
    public String toString(){
        return "(" + pointerHelper(lastMarker) + "-" + pointerHelper(matchMarker) + "-" +
            pointerHelper(nextMarker) + "): " + rawText;
    }

    /**
     * Print either the location or "end" with bracket. Helper method of
     * {@link #toString()}.
     */
    private String pointerHelper(int ptr){
        if (ptr < rawText.length()){
            return ptr + "(" + rawText.charAt(ptr) + ")";
        }
        return ptr + "(end)";
    }
}
