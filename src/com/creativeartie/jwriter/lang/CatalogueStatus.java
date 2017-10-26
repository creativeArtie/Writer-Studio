package com.creativeartie.jwriter.lang;

import static com.creativeartie.jwriter.lang.SetupStrings.*;
    
/**
 * Types of error with the CatalogueIdentity.
 */
public enum CatalogueStatus implements DetailStyle {
    /// This is no id assoicate in a CatalogueHolder
    NO_ID(STYLE_ID_NONE),
    /// There is an id but nothing is refer to it
    UNUSED(STYLE_ID_WARNING),
    /// A reference that pointing to no known CatalogueIdentity. 
    NOT_FOUND(STYLE_ID_ERROR), 
    /// More than one CatalogueIdentity has the same name
    MULTIPLE(STYLE_ID_WARNING),
    /// No error is found.
    READY(STYLE_ID_READY);
    
    private final String styleType;
    
    private CatalogueStatus(String readiness){
        styleType = readiness;
    }
    
    @Override
    public String getStyleClass(){
        return DetailStyle.styleFromEnum(STYLE_ID, styleType);
    }
}
