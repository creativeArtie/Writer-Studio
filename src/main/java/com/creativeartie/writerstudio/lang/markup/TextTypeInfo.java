package com.creativeartie.writerstudio.lang.markup;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** {@link Type} for {@link TextSpanMatter}.*/
public enum TextTypeInfo implements TextType{
    /** Text for the pdf author field. */
    AUTHOR(META_AUTHOR),
    /** Text for the pdf keyword field. */
    KEYWORDS(META_KEYWORDS),
    /** Text for the pdf subject field. */
    SUBJECT(META_SUBJECT),
    /** Text for the pdf title field. */
    TITLE(META_TITLE);

    private String keyName;

    /** Creates a {@linkplain Area}.
     *
     * @param key
     *      storage key.
     */
    private TextTypeInfo(String key){
        keyName = key;
    }

    @Override
    public String getKeyName(){
        return keyName;
    }
}
