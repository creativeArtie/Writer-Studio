package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Formats for {@link TextDataSpan}.
 *
 * This usually mean alignment or data type. Formatting will be a different
 * thing altogether.
 *
 * The value order is set by:
 * <ul>
 * <li>{@link #listAligns()}</li>
 * <li>{@link #isAlignType()}</li>
 * </ul>
 */
public enum TextMatterAlign {
    /** Align text to the left. */
    LEFT(ALIGN_LEFT),
    /** Align text to the center. */
    CENTER(ALIGN_CENTER),
    /** Align text to the right. */
    RIGHT(ALIGN_RIGHT);

    private String keyName;

    /** Creates a {@linkplain Format}.
     *
     * @param key
     *      storage key.
     */
    private Format(String key){
        keyName = key;
    }

    /** Gets the key name in the file.
     *
     * @return answer
     */
    String getKeyName(){
        return keyName;
    }

}
