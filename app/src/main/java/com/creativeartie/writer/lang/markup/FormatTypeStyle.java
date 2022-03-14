package com.creativeartie.writer.lang.markup;

import com.creativeartie.writer.lang.*;

import static com.creativeartie.writer.lang.markup.AuxiliaryData.*;

/** {@link FormatSpan} formats.
 *
 * The value order is set by:
 * <ol>
 * <li>{@link FormattedParser#parse(SetupPointer)} through</li>
 * <li> {@link AuxiliaryData#FORMATTED_TEXT}</li>
 * </ol>
 */
public enum FormatTypeStyle{
    /** Bold text with the token: {@link AuxiliaryData#FORMAT_BOLD}.*/
    BOLD(FORMAT_BOLD),
    /** Italics text with the token: {@link AuxiliaryData#FORMAT_ITALICS}. */
    ITALICS(FORMAT_ITALICS),
    /** Underlined text with the token: {@link AuxiliaryData#FORMAT_UNDERLINE}. */
    UNDERLINE(FORMAT_UNDERLINE),
    /** Mono-text text with the token: {@link AuxiliaryData#FORMAT_CODED}.  */
    CODED(FORMAT_CODED);

    private String formatToken;

    /** Creates a {@linkplain FormattedParser}.
     * @param token
     *      formatter token
     */
    private FormatTypeStyle(String token){
        assert token != null && token.length() > 0: "Empty token";
        formatToken = token;
    }

    /** Gets format token.
     *
     * @return answer.
     * @see FormattedParser#parse(SetupPointer)
     */
    String getToken(){
        return formatToken;
    }
}
