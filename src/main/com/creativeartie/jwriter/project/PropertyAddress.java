package com.creativeartie.jwriter.project;

import java.util.*;

public class PropertyAddress extends Property{
    static final String STREET = "Street";
    static final String CITY = "City";
    static final String PROVINCE = "Province";
    static final String POSTAL_CODE = "PostalCode";
    static final String STATE = "State";
    static final String ZIP_CODE = "ZipCode";
    static final String COUNTRY = "Country";

    private String firstName;
    private String middleName;
    private String lastName;

    PropertyAddress(String key, Properties props){
        super(key, props);
    }

    @Override
    String convertKey(String part){
        switch (part){
            case STREET:
                return getProperty(STREET);
            case CITY:
                return getProperty(CITY);
            case PROVINCE:
            case STATE:
                return getProperty(PROVINCE);
            case POSTAL_CODE:
            case ZIP_CODE:
                return getProperty(POSTAL_CODE);
            case COUNTRY:
                return getProperty(COUNTRY);
        }
        return "";
    }

    public void setValue(String street, String city, String province,
            String postal, String country){
        setProperty(STREET, street);
        setProperty(CITY, city);
        setProperty(PROVINCE, province);
        setProperty(POSTAL_CODE, postal);
        setProperty(COUNTRY, country);
    }
}