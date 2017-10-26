package com.creativeartie.jwriter.property;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;

public class StyleProperty extends MapProperty{
    private static final String FIELD = ":";
    private static final String LINE = ";";
    
    StyleProperty(String propertyKey, PropertyManager propertyManager){
        super(propertyKey, propertyManager, FIELD, LINE);
    }
    
    @Override
    protected Map<String, String> fromStorage(String value){
        return super.fromStorage(CharMatcher.whitespace().removeFrom(value));
    }
    
    public String toCss(){
        return toCss(Arrays.asList(new StyleProperty[]{this}));
    }
    
    public static String toCss(List<StyleProperty> properties){
        TreeMap<String, String> rawMap = new TreeMap<>();
        properties.forEach((property) -> {
            property.get().entrySet().forEach((entry) -> {
                rawMap.put(entry.getKey(), entry.getValue());
            });
        });
        return Joiner.on(LINE).withKeyValueSeparator(FIELD).join(rawMap) + LINE;
    }
}
