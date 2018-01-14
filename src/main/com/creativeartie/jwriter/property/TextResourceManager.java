package com.creativeartie.jwriter.property;

import java.util.*;
import java.io.*;

public class TextResourceManager {
    private static final HashMap<String, TextResourceManager> RESOURCES = 
        new HashMap<>();
        
    private static final String EXTENSION = ".properties";
    
    private static Optional<Locale> textLocale = Optional.empty();
    
    public static TextResourceManager getResouce(String file){
        if (RESOURCES.containsKey(file)){
            return RESOURCES.get(file);
        }
        TextResourceManager manager = new TextResourceManager(file);
        RESOURCES.put(file, manager);
        return manager;
    }
    
    public static void updateLocale(Locale locale) {
        textLocale = Optional.ofNullable(locale);
        RESOURCES.values().stream().map((manager) -> {
            manager.setBundle();
            return manager;
        }).forEachOrdered((manager) -> {
            manager.textFound.values().forEach((text) -> {
                text.update();
            });
        });
    }
    
    private final HashMap<String, TextResource> textFound;
    
    private Properties defaultTexts;
    
    private Properties localeTexts;
    
    private String baseName;
    
    private void setBundle() {
        StringBuilder builder = new StringBuilder(baseName);
        if(textLocale.isPresent()){
            Locale l = textLocale.get();
            String lang = l.getLanguage();
            if (! lang.isEmpty()){
                builder.append("_").append(lang);
            }
            String country = l.getCountry();
            if (! country.isEmpty()){
                builder.append("_").append(country);
            }
        }
        builder.append(EXTENSION);
        localeTexts = new Properties(defaultTexts);
        File file = new File(builder.toString());
        if (file.isFile()){            
            try (FileInputStream in = new FileInputStream(file)){
                localeTexts.load(in);
            } catch (IOException ex){
                throw new RuntimeException("This should not be thrown.", ex);
            }
        }
    }
    
    private TextResourceManager(String text){
        baseName = text;
        defaultTexts = new Properties();
        try (FileInputStream in = new FileInputStream(baseName + EXTENSION)){
            defaultTexts.load(in);
        } catch (IOException ex){
            throw new MissingResourceException("Resource file fail to load: " +
                baseName + EXTENSION, text, "");
        }
        setBundle();
        textFound = new HashMap<>();
    }
    
    public TextResource getText(String key){
        if (localeTexts.getProperty(key) == null){
            throw new MissingResourceException(
                "Resource key not found in " + baseName + ": " + key, 
                baseName, key);
        }
        if (textFound.containsKey(key)){
            return textFound.get(key);
        }
        TextResource resource = new TextResource(key, this);
        textFound.put(key, resource);
        return resource;
    }
    
    String get(String key){
        return localeTexts.getProperty(key);
    }
}
