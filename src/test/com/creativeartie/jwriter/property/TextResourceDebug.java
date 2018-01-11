package com.creativeartie.jwriter.property;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.rules.*;

import java.io.File;
import java.util.Optional;
import java.util.Locale;
import java.util.MissingResourceException;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class TextResourceDebug{
    private static final String file = "data/text";
    
    private TextResourceManager build() throws Exception{
        return TextResourceManager.getResouce(file);
    }
    
    @Rule 
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void testBasic() throws Exception{
        build();
    }
    
    @Test
    public void simpleGet() throws Exception{
        assertEquals("OK", build().getText("Ok").get());
    }
    
    @Test
    public void diffLocaleGet() throws Exception{
        TextResourceManager.updateLocale(new Locale("en", "CA"));
        TextResourceManager manager = build();
        assertEquals("Cancel", manager.getText("Cancel").get());
        TextResourceManager.updateLocale(null);
        assertEquals("Can.", manager.getText("Cancel").get());
    }
    
    @Test
    public void changeLocale() throws Exception{
        TextResourceManager.updateLocale(Locale.GERMAN);
        assertEquals("Abbrechen", build().getText("Cancel").get());
        TextResourceManager.updateLocale(null);
        assertEquals("Can.", build().getText("Cancel").get());
    }
    
    @Test
    public void changeUnknownLocale() throws Exception{
        TextResourceManager.updateLocale(Locale.CHINESE);
        assertEquals("OK", build().getText("Ok").get());
        TextResourceManager.updateLocale(null);
        assertEquals("OK", build().getText("Ok").get());
    }
    
    @Test
    public void keyNotFound() throws Exception{
        thrown.expect(MissingResourceException.class);
        thrown.expectMessage("Resource key not found in data/text: not-found");
        build().getText("not-found");
    }
    
    @Test
    public void sameManager() throws Exception{
        assertSame(build(), build());
    }
    
    @Test
    public void sameResource() throws Exception{
        assertSame(build().getText("Cancel"), build().getText("Cancel"));
    }
    
    private boolean failListener;
    @Test
    public void listener() throws Exception{
        failListener = true;
        TextResource test = build().getText("ListenTest");
        test.addListener((listener, value) -> {
            if (failListener) {
                assertEquals("horen", value);
                failListener = false;
            }
        });
        TextResourceManager.updateLocale(Locale.GERMAN);
        assertFalse(failListener);
        TextResourceManager.updateLocale(null);
    }
}
