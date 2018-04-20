package com.creativeartie.writerstudio.file;

/** Statstics Fields */
public enum FieldType {
    PAGE_NUMBER("Stats.PageNumber"), WORD_COUNT("Stats.WordCountEst");

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