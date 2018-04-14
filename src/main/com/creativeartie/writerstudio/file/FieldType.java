package com.creativeartie.writerstudio.file;

/**
 * List of field types
 */
public enum FieldType {
    PAGE_NUMBER("Stats.PageNumber"), WORD_COUNT("Stats.WordCountEst");

    private String fieldKey;

    private FieldType(String key){
        fieldKey = key;
    }

    public String getFieldKey(){
        return fieldKey;
    }

    public static FieldType findField(String key){
        for (FieldType type: values()){
            if (type.fieldKey.equals(key)){
                return type;
            }
        }
        return null;
    }
}