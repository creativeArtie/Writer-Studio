package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Utility methods to check if a text can be parse locally. */
public final class AuxiliaryChecker{

    /** Check if a {@link SpanBranch} ends with a new line or at file end
     *
     * @param last
     *      in last section
     * @param text
     *      new text
     * @return answer
     * @see NoteCardSpan#getParser(String)
     * @see SectionSpan#getParser(String)
     */
    static boolean checkSectionEnd(String text, boolean isLast){
        argumentNotNull(text, "text");

        if (text.endsWith(TOKEN_ESCAPE)){
            /// if (text == "text\\")
            return isLast;
        }
        if (text.endsWith(TOKEN_ESCAPE + LINED_END)){
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
     * @see LinedSpan
     * @see TextSpan#getParser(String)
     */
    static boolean checkLineEnd(String text, boolean last){
        argumentNotNull(text, "text");

        return last?
            /// "\n" at end is optional
            notCutoff(text.substring(0, text.length() - LINED_END.length()),
                LINED_END):
            /// "\n" at end is required
            willEndWith(text, LINED_END);
    }

    /** Check if ender is at the end or never appeared.
     *
     * @param text
     *      new text
     * @param ender
     *      text ender
     * @return answer
     * @see #checkLineEnd(String, boolean)
     * @see FormatParsePoint#canParse(String)
     * @see FormatSpanAgenda#getParse(String)
     * @see FormatSpanLinkDirect#getParse(String)
     * @see FormatSpanLinkRef#getParse(String)
     */
    static boolean willEndWith(String text, String ender){
        argumentNotNull(text, "text");
        argumentNotNull(ender, "ender");

        return text.endsWith(ender)?
            /// text ends with ender, check for middle
            notCutoff(text.substring(0, text.length() - ender.length()),
                Arrays.asList(ender)
            ):
            /// text does not end with ender
            false;
    }

    /** Check if text not cutoff by span enders and line end.
     *
     * Same as {@code AuxiliaryChecker#notCutoff(text, Arrays.asList(endings))}.
     *
     * @param text
     *     new text
     * @param ending
     *      ending tokens
     * @return answer
     * @see #checkLineEnd(String, boolean)
     * @see EditionSpan#canParse(String)
     */
    static boolean notCutoff(String text, String ... endings){
        argumentNotNull(text, "text");
        argumentNotEmpty(endings, "endings");

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
     * @see #willEndWith(String, String)
     * @see DirectoryParser#canParse(String)
     */
    static boolean notCutoff(String text, List<String> endings){
        argumentNotNull(text, "text");
        argumentNotEmpty(endings, "endings");

        boolean escaped = false;
        for(int i = 0; i < text.length(); i++){
            if (! escaped){
                /// escape next character if escaped
                if (text.startsWith(TOKEN_ESCAPE, i)){
                    escaped = true;
                } else {

                    /// check for span ending token
                    for (String ender: endings){
                        if (text.startsWith(ender, i)) return false;
                    }

                    /// check for line ending
                    if (text.startsWith(LINED_END, i)) return false;
                }
            } else /* if (escape) */{
                escaped = false;
            }
        }
        /// escape the next character (aka merge with next).
        return ! escaped;
    }

    /** Private construtor */
    private AuxiliaryChecker(){}
}
