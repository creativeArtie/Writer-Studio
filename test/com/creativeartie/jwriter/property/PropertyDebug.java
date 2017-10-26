package com.creativeartie.jwriter.property;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.rules.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class PropertyDebug{
    private static final String defaults = "data/propDefaults";
    private static final String user1 = "data/propTest1";
    private static final String user2 = "data/propTest2";
    private static final String user3 = "data/propTest3";
    
    @Rule 
    public ExpectedException thrown = ExpectedException.none();
    
    private PropertyManager build() throws Exception{
        return build(user1);
    }
    
    private PropertyManager build(String user) throws Exception{
        return new PropertyManager(defaults, user);
    }
    
    @Test
    public void creation() throws Exception{
        build();
    }
    
    @Test
    public void noOptionSet() throws Exception{
        assertEquals(2, build().getIntProperty("notSet").get().intValue());
    }
    
    @Test
    public void hasOptionSet() throws Exception{
        assertEquals(987654, build().getIntProperty("hasSet").get().intValue());
    }
    
    @Test
    public void notOption() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Property is not found: unknown.");
        build().getIntProperty("unknown");
    }
    
    @Test
    public void basicSet() throws Exception{
        IntegerProperty property = build().getIntProperty("hasSet");
        property.set(new Integer(12));
        assertEquals(12, property.get().intValue());
    }
    
    @Test
    public void save() throws Exception{
        PropertyManager manager = build(user2);
        IntegerProperty property = manager.getIntProperty("hasSet");
        property.set(new Integer(12));
        manager.store();
        manager = build(user2);
        assertEquals(12, manager.getIntProperty("hasSet").get().intValue());
    }
    
    private boolean failListener;
    
    @Test
    public void testListener() throws Exception{
        PropertyManager manager = build(user3);
        failListener = true;
        IntegerProperty property = manager.getIntProperty("hasSet");
        property.addListener((listener, value) -> {
            assertEquals(344524524, value.intValue());
            failListener = false;
        });
        property.set(344524524);
        assertFalse(failListener);
    }
    
    private boolean failDoubleProperty;
    
    @Test
    public void doublePropertyGet() throws Exception{
        PropertyManager manager = build(user3);
        failDoubleProperty = true;
        IntegerProperty property = manager.getIntProperty("hasSet");
        property.addListener((listener, value) -> {
            assertEquals(35566, value.intValue());
            failDoubleProperty = false;
        });
        IntegerProperty property1 = manager.getIntProperty("hasSet");
        property1.set(35566);
        assertFalse(failDoubleProperty); 
    }
    
    @Test
    public void clearListeners() throws Exception{
        PropertyManager manager = build(user3);
        failDoubleProperty = true;
        IntegerProperty property = manager.getIntProperty("hasSet");
        property.addListener((listener, value) -> fail());
        IntegerProperty property1 = manager.getIntProperty("hasSet");
        property1.addListener((listener, value) -> fail());
        property1.clearListeners();
        property1.set(35566);
    }
    
    @Test
    public void removeListener() throws Exception{
        PropertyManager manager = build(user3);
        failDoubleProperty = true;
        IntegerProperty property = manager.getIntProperty("hasSet");
        PropertyListener<Integer> listen = (listener, value) -> fail();
        property.addListener(listen);
        property.removeListener(listen);
        property.set(999);
    }
}
