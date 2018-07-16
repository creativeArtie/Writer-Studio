package com.creativeartie.writerstudio.lang.markup;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** {@link Type} for {@link TextSpanInfo}.*/
public enum TextTypeMatter implements TextType{
    // TODO render the following comment out Area, then uncomment out,
    /** Formatted text at the top of the front page.*/
    FRONT_TOP(TITLE_TOP),
    /** Formatted text at the middle of the front page.*/
    FRONT_CENTER(TITLE_CENTER),
    /** Formatted text at the bottom of the front page.*/
    FRONT_BOTTOM(TITLE_BOTTOM),

    // /** Text for start of the main content. */
    // MAIN_STARTER(MAIN_STARTER),
    /** Text for the top of each main content page. */
    TEXT_HEADER(MAIN_HEADER),
    /** Text for section break in the main content */
    TEXT_BREAK(MAIN_BREAK),
    // /** Text for the bottom of each main content page. */
    // MAIN_FOOTER(MAIN_FOOTER)
    /** Text for the end of the main content. */
    TEXT_ENDER(MAIN_ENDER),

    // /** Text for start of the endnote content. */
    // ENDNOTE_STARTER(END_STARTER),
    // /** Text for the top of each endnote content page. */
    // ENDNOTE_HEADER(END_HEADER),
    // /** Text for the bottom of each endnote content page. */
    // ENDNOTE_FOOTER(END_FOOTER)
    // /** Text for the end of the endnote content. */
    // ENDNOTE_ENDER(END_ENDER),

    /** Text for start of the work(s) cited content. */
    SOURCE_STARTER(CITE_STARTER);
    // /** Text for the top of each work(s) cited content page. */
    // SOURCE_HEADER(CITE_HEADER),
    // /** Text for the bottom of each work(s) cited content page. */
    // SOURCE_FOOTER(CITE_FOOTER)
    // /** Text for the end of the work(s) cited content. */
    // SOURCE_ENDER(CITE_ENDER);

    private String keyName;

    /** Creates a {@linkplain Meta}.
     *
     * @param key
     *      storage key.
     */
    private TextTypeMatter(String key){
        keyName = key;
    }

    @Override
    public String getKeyName(){
        return keyName;
    }
}
