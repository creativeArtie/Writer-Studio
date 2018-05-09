package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

public class TextDataType{

    public interface Type{
        String getKeyName();
    }

    public enum Area implements Type, StyleInfo{
        FRONT_TOP(TITLE_TOP), FRONT_CENTER(TITLE_CENTER),
        FRONT_BOTTOM(TITLE_BOTTOM),

        MAIN_HEADER(TEXT_HEADER), MAIN_BREAK(TEXT_BREAK),
        MAIN_ENDER(TEXT_AFTER),

        SOURCE_TITLE(CITE_TITLE);

        private String keyName;
        private Area(String key){
            keyName = key;
        }

        public String getKeyName(){
            return keyName;
        }
    }

    public enum Meta implements Type, StyleInfo{
        AUTHOR(META_AUTHOR), KEYWORDS(META_KEYWORDS), SUBJECT(META_SUBJECT),
        TITLE(META_TITLE);

        private String keyName;
        private Meta(String key){
            keyName = key;
        }

        public String getKeyName(){
            return keyName;
        }
    }

    public enum Format implements StyleInfo{
        LEFT(ALIGN_LEFT), CENTER(ALIGN_CENTER), RIGHT(ALIGN_RIGHT),
        TEXT(ALIGN_TEXT);

        public static Format[] listAligns(){
            return Arrays.copyOf(values(), values().length - 1);
        }

        private String keyName;
        private Format(String key){
            keyName = key;
        }

        String getKeyName(){
            return keyName;
        }

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