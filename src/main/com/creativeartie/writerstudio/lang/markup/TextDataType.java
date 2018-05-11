package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Data type for querying meta information.*/
public class TextDataType{

    /** Basic {@link TextDataSpan} type.*/
    public interface Type{
        /** Gets the key name in the file.
         *
         * @return answer
         */
        public String getKeyName();
    }

    /** {@link Type} for {@link TextDataSpanPrint}.*/
    public enum Area implements Type, StyleInfo{
        // TODO render the following comment out Area, then uncomment out,
        /** Formatted text at the top of the front page.*/
        FRONT_TOP(TITLE_TOP),
        /** Formatted text at the middle of the front page.*/
        FRONT_CENTER(TITLE_CENTER),
        /** Formatted text at the bottom of the front page.*/
        FRONT_BOTTOM(TITLE_BOTTOM),

        // /** Text for start of the main content. */
        // MAIN_STARTER(TEXT_STARTER),
        /** Text for the top of each main content page. */
        MAIN_HEADER(TEXT_HEADER),
        /** Text for section break in the main content */
        MAIN_BREAK(TEXT_BREAK),
        // /** Text for the bottom of each main content page. */
        // MAIN_FOOTER(TEXT_FOOTER)
        /** Text for the end of the main content. */
        MAIN_ENDER(TEXT_ENDER),

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
        // SOURCE_ENDER(CITE_ENDER),

        private String keyName;

        /** Creates a {@linkplain Area}.
         *
         * @param key
         *      storage key.
         */
        private Area(String key){
            keyName = key;
        }

        @Override
        public String getKeyName(){
            return keyName;
        }
    }

    /** {@link Type} for {@link TextDataSpanMeta}.*/
    public enum Meta implements Type, StyleInfo{
        /** Text for the pdf author field. */
        AUTHOR(META_AUTHOR),
        /** Text for the pdf keyword field. */
        KEYWORDS(META_KEYWORDS),
        /** Text for the pdf subject field. */
        SUBJECT(META_SUBJECT),
        /** Text for the pdf title field. */
        TITLE(META_TITLE);

        private String keyName;

        /** Creates a {@linkplain Meta}.
         *
         * @param key
         *      storage key.
         */
        private Meta(String key){
            keyName = key;
        }

        @Override
        public String getKeyName(){
            return keyName;
        }
    }

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
    public enum Format implements StyleInfo{
        /** Align text to the left. */
        LEFT(ALIGN_LEFT),
        /** Align text to the center. */
        CENTER(ALIGN_CENTER),
        /** Align text to the right. */
        RIGHT(ALIGN_RIGHT),
        /** Data is a text type. */
        TEXT(ALIGN_TEXT);

        /** List the text alignment. */
        public static Format[] listAligns(){
            return Arrays.copyOf(values(), values().length - 1);
        }

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

        /** Check if type is alignment type.
         *
         * @return answer
         */
        public boolean isAlignType(){
            return ordinal() <= RIGHT.ordinal();
        }

    }

    /** Statstics Fields */
    public enum FieldType {
        /** To show the current page number */
        PAGE_NUMBER("Stats.PageNumber"),
        /** To show the word count round to the significate digit*/
        WORD_COUNT("Stats.WordCountEst");

        private String fieldKey;

        /** Constructor with a key
         * @param key
         *      reference key name
         */
        private FieldType(String key){
            fieldKey = key;
        }

        /** Gets reference key name
         *
         * @return answer
         */
        public String getFieldKey(){
            return fieldKey;
        }

        /** Finds the field for a key
         *
         * @param key
         *      reference key name
         * @return answer or null
         */
        public static FieldType findField(String key){
            if (key == null || key.isEmpty()) return null;

            for (FieldType type: values()){
                if (type.fieldKey.equals(key)){
                    return type;
                }
            }
            return null;
        }
    }

    private TextDataType(){}
}