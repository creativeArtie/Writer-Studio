package com.creativeartie.jwriter.lang;

import java.util.*;
import com.google.common.base.*;

/**
 * SetupPointer for the rawText. Stores two points: {@linkplain start} for where the
 * pointer is currently checking from and {@linkplain end} for where is the
 * next matching data.
 */
public final class SetupPointer{

    private final String rawText;
    private final Document document;

    /// Points to where the pointer would roll back to
    private int lastMarker;
    /// Points to the matching start
    private int matchMarker;
    /// Points to the last success (but not commit) check
    private int nextMarker;

    static SetupPointer newPointer(String raw, Document doc){
        return new SetupPointer(raw, doc);
    }

    static SetupPointer updatePointer(String raw, Document doc){
        return new SetupPointer(raw, doc);
    }

    private SetupPointer(String raw, Document doc){
        rawText = raw;
        document = doc;
        matchMarker = 0;
        nextMarker = 0;
        lastMarker = 0;
    }

    public void mark(){
        lastMarker = nextMarker;
    }

    public void rollBack(SpanBranch remove){
        remove.setRemove();
        rollBack(remove.toArray(new Span[0]));
    }

    public void rollBack(List<Span> remove){
        rollBack(remove.toArray(new Span[0]));
    }

    public void rollBack(Span ... remove){
        matchMarker = lastMarker;
        nextMarker = lastMarker;
        for(Span span: remove){
            span.setRemove();
        }
    }

    public boolean trimStartsWith(ArrayList<Span> children, String compare){
        return trimStartsWith(children, SetupLeafStyle.KEYWORD, compare);
    }

    public boolean trimStartsWith(ArrayList<Span> children, SetupLeafStyle style,
        String compare
    ){
        return finishing(children, style, trimStartsWith(compare));
    }

    private boolean trimStartsWith(String ... compares){
        if (matchMarker >= rawText.length()){
            return false;
        }
        int next = ignoreSpaces();
        if (next == -1){
            return false;
        }
        for (String compare: compares){
            if (rawText.startsWith(compare, next)){
                nextMarker = next + compare.length();

                return true;
            }
        }
        return false;
    }

    public boolean startsWith(ArrayList<Span> children, String compare){
        return startsWith(children, SetupLeafStyle.KEYWORD, compare);
    }

    public boolean startsWith(ArrayList<Span> children, SetupLeafStyle style,
        String compare
    ){
        return finishing(children, style, startsWith(compare));
    }

    /**
     * Check if the pointer at the text location starts with
     * {@linkplain compare}. If so move {@linkplain end} to the length of
     * {@linkplain compare}.
     */
    private boolean startsWith(String ... compares){
        int next = matchMarker;
        for(String compare : compares){
            if (rawText.startsWith(compare, next)){
                nextMarker = next + compare.length();

                return true;
            }
        }
        return false;
    }

    public boolean matches(ArrayList<Span> children, CharMatcher matcher){
        return matches(children, SetupLeafStyle.DATA, matcher);
    }

    public boolean matches(ArrayList<Span> children, SetupLeafStyle style,
        CharMatcher matcher
    ){
        return finishing(children, style, matches(matcher));
    }

    private boolean matches(CharMatcher matches){
        int next = matchMarker;
        for (; rawText.length() > next; next++){
            if (! matches.matches(rawText.charAt(next))){
                if (next != matchMarker){
                    nextMarker = next;
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (next != matchMarker){
            nextMarker = next;
            return true;
        }
        return false;
    }

    public boolean getTo(ArrayList<Span> children, List<String> compares){
        return getTo(children, SetupLeafStyle.KEYWORD, compares);
    }

    public boolean getTo(ArrayList<Span> children, SetupLeafStyle style,
        List<String> compare
    ){
        return finishing(children, style, getTo(compare));
    }

    public boolean getTo(ArrayList<Span> children, String ... compare){
        return getTo(children, SetupLeafStyle.KEYWORD, compare);
    }

    public boolean getTo(ArrayList<Span> children, SetupLeafStyle style,
        String ... compare
    ){
        return finishing(children, style, getTo(Arrays.asList(compare)));
    }

    /**
     * Moves {@linkplain end} pointer to before one of the {@linkplain enders}
     * or the end of the rawText.
     */
    public boolean getTo(List<String> enders) {

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
            nextMarker = next;
            return true;
        }


        return false;
    }

    public boolean nextChars(ArrayList<Span> children, int size){
        return nextChars(children, SetupLeafStyle.KEYWORD, size);
    }

    public boolean nextChars(ArrayList<Span> children, SetupLeafStyle style,
        int size
    ){
        return finishing(children, style, nextChars(size));
    }

    /**
     * Moves {@linkpalin end} pointer to {@linkplain size}, if possible. If not
     * possible do not move {@linkplain end}
     */
    private boolean nextChars(int size) {
        if (matchMarker + size <= rawText.length()){
            nextMarker = matchMarker + size;

            return true;
        }
        return false;
    }

    private boolean finishing(ArrayList<Span> children, SetupLeafStyle style,
        boolean matches
    ){
        if(matches){
            children.add(new SpanLeaf(this, style));
        }
        return matches;
    }

    public boolean hasNext(){
        return matchMarker < rawText.length();
    }

    protected String getRaw(){
        return rawText.substring(matchMarker, nextMarker);
    }

    public Document getDocument(){
        return document;
    }

    protected void roll(){
        matchMarker = nextMarker;
    }


    @Override
    public String toString(){
        return "(" + pointerHelper(lastMarker) + "-" + pointerHelper(matchMarker) + "-" +
            pointerHelper(nextMarker) + "): " + rawText;
    }

    private String pointerHelper(int ptr){
        if (ptr < rawText.length()){
            return ptr + "(" + rawText.charAt(ptr) + ")";
        }
        return ptr + "(end)";
    }

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
}
