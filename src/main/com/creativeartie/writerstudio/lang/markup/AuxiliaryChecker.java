package com.creativeartie.writerstudio.lang.markup;

import java.util.*; // Arrays, List

import com.creativeartie.writerstudio.lang.*; // SetupParser

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * All methods meant to check if a string can be reparsed locally in a single
 * {@link SpanBranch}.
 */
public final class AuxiliaryChecker{

    /** Check if a {@link SectionSpan} or {@link NoteCardSpan} can be contained.
     *
     * @param last
     *      last section?
     * @param text
     *      new text
     * @return answer
     */
    static boolean checkSectionEnd(String text, boolean isLast){
        checkNotNull(text, "text");
        if (text.endsWith(CHAR_ESCAPE)){
            /// if (text == "text\\")
            return isLast;
        }
        if (text.endsWith(CHAR_ESCAPE + LINED_END)){
            /// if (text == "text\\\n")
            return isLast;
        }
        if (! (isLast || text.endsWith(LINED_END))){
            /// if (! isLast && text != "text\n" )
            return false;
        }
        /// if ( text == "text\n")
        return true;
    }

    /** check if the text is contained in a line.
     *
     * @param text
     *      new text
     * @param last
     *      last line?
     */
    static boolean checkLineEnd(String text, boolean last){
        checkNotNull(text, "text");
        return last?
            notCutoff(text.substring(0, text.length() - LINED_END.length()),
                LINED_END):
            willEndWith(text, LINED_END);
    }

    /** Check if ender is at the end or never appeared.
     *
     * @param text
     *      new text
     * @param ender
     *      text ender
     * @return answer
     */
    static boolean willEndWith(String text, String ender){
        checkNotNull(text, "text");
        checkNotNull(ender, "ender");
        return text.endsWith(ender)?
            notCutoff(text.substring(0, text.length() - ender.length()),
                Arrays.asList(ender)
            ): false;
    }

    /** Check if text not cutoff by span enders and line end.
     *
     * @param text
     *     new text
     * @param ending
     *      ending tokens
     * @return answer
     * @see #notCutoff(text, List)
     */
    static boolean notCutoff(String text, String ... endings){
        return notCutoff(text, Arrays.asList(endings));
    }

    /** Check if text not cutoff by span enders and line end.
     *
     * @param text
     *     new text
     * @param ending
     *      ending tokens
     * @return answer
     * @see #notCutoff(text, String ...)
     */
    static boolean notCutoff(String text, List<String> endings){
        checkNotNull(text, "text");
        checkNotNull(endings, "endings");

        boolean escaped = false;
        for(int i = 0; i < text.length(); i++){
            if (! escaped){
                /// escape next character if escaped
                if (text.startsWith(CHAR_ESCAPE, i)){
                    escaped = true;
                } else {

                    /// check for span ending token
                    for (String ender: endings){
                        if (text.startsWith(ender, i)) return false;
                    }

                    /// check for line ending
                    if (text.startsWith(LINED_END, i)) return false;
                }
            } else {
                escaped = false;
            }
        }
        /// escape the next character (aka merge with next).
        return ! escaped;
    }

    /** Private construtor */
    private AuxiliaryChecker(){}
}