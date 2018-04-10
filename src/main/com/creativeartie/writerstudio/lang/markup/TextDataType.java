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

    private TextDataType(){}
}