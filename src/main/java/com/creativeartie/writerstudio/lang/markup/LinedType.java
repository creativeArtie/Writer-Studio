package com.creativeartie.writerstudio.lang.markup;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Type of lines.
 *
 * The value order is set by:
 * <ul>
 * <li>{@link #findType(String)}</li>
 * </ul>
 */
public enum LinedType implements StyleInfo{

    /** a heading line. */
    HEADING(null),
    /** a non-publishing outline line. */
    OUTLINE(null),
    /** a numbered list item. */
    NUMBERED(null),
    /** a bullet list item. */
    BULLET(null),

    /** Footnote that starts with {@link AuxiliaryData#LINED_FOOTNOTE}. */
    FOOTNOTE(LINED_FOOTNOTE),
    /** Endnote that starts with {@link AuxiliaryData#LINED_ENDNOTE}. */
    ENDNOTE(LINED_ENDNOTE),
    /** A resuable link that starts with {@link AuxiliaryData#LINED_LINK}. */
    LINK(LINED_LINK),
    /** Non-pulishing note that starts with {@link AuxiliaryData#LINED_NOTE}.  */
    NOTE(LINED_NOTE),

    /** Non-pulishing to do item that starts with {@link AuxiliaryData#LINED_AGENDA}. */
    AGENDA(LINED_AGENDA),
    /** A block quote that starts with {@link AuxiliaryData#LINED_QUOTE}. */
    QUOTE(LINED_QUOTE),
    /** A section break the line is {@link AuxiliaryData#LINED_BREAK}. */
    BREAK(LINED_BREAK),
    /** A citation source that starts with {@link AuxiliaryData#LINED_CITE}. */
    SOURCE(LINED_CITE),
    /** A paragraph with no starting token.
     *
     * The should be kept last because it is the "default case" for
     * {@link #findType(String)}.
     */
    PARAGRAPH(null);

    private final Optional<String> spanStarter;

    /** Creates a {@linkplain LineType}.
     *
     * @param parser
     *      start token
     */
    private LinedType(String starter){
        spanStarter = Optional.ofNullable(starter);
    }


    static LinedType findType(String raw){
        argumentNotNull(raw, "raw");
        for (LinedType type: values()){
            if (type == PARAGRAPH){
                return type;
            }
            int ordinal = type.ordinal();
            if (ordinal < FOOTNOTE.ordinal()){
                LinedParseLevel level = LinedParseLevel.values()[ordinal];
                List<String> starters = LEVEL_STARTERS.get(level);
                for (int i = LEVEL_MAX - 1; i >= 0; i--){
                    if(raw.startsWith(starters.get(i))){
                        if (i == LEVEL_MAX + 1){
                            return LinedType.PARAGRAPH;
                        } else {
                            return type;
                        }
                    }
                }
            }
            if (type.spanStarter.isPresent() &&
                    raw.startsWith(type.spanStarter.get())){
                return type;
            }
        }
        assert false;
        return null;
    }
}
