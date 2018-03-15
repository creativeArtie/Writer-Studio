package com.creativeartie.jwriter.project;

import java.util.*;

public class PropertyName extends Property{
    static final String[] STORED_PARTS = new String[]{"FirstName", "MiddleName",
        "LastName"}

    static final String FIRST_NAME = "FirstName";
    static final String LAST_NAME = "Surname";
    static final String MIDDLE_NAME = "MiddleName";
    static final String FULL_NAME = "FullName";

    private String firstName;
    private String middleName;
    private String lastName;

    public PropertyName(String key, Properties props){
        super(key, props);
    }

    String convertKey(String part){
        switch (part){
            case FIRST_NAME:
                return FIRST_NAME;
            case MIDDLE_NAME:
                return getProperty(MIDDLE_NAME);
            case LAST_NAME:
                return getProperty(LAST_NAME);
            case FULL_NAME:
                return getProperty(FIRST_NAME) + getProperty(MIDDLE_NAME) +
                    getProperty(LAST_NAME);
        }
        return "";
    }

    public void setValue(String first, String middle, String last){
        setProperty(FIRST_NAME, first);
        setProperty(MIDDLE_NAME, middle);
        setProperty(LAST_NAME, last);
    }
}