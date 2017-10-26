package com.creativeartie.jwriter.lang;

import com.google.common.base.*;

import com.creativeartie.jwriter.main.*;

public interface DetailStyle{
    public String getStyleClass();
    
    public static final String SEPARATOR = ".";
    
    public static String styleFromEnum(String prefix, String postfix){
        Checker.checkNotNull(prefix, "prefix");
        Checker.checkNotNull(postfix, "postfix");
        
        CaseFormat from = CaseFormat.UPPER_UNDERSCORE;
        CaseFormat to = CaseFormat.UPPER_CAMEL; 
        return from.to(to, prefix) + SEPARATOR + from.to(to, postfix);
    }
    
}
