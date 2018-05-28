
package com.creativeartie.writerstudio.lang;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.stream.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public abstract class SpanBranchAssert<T extends SpanBranchAssert>{
    private SpanBranch assertDoc;
    private Optional<CatalogueIdentity> expectId;
    private CatalogueStatus expectStatus;
    private ArrayList<StyleInfo> expectStyles;
    private boolean isCatalogued;
    private Class<T> returnCast;
    private SpanBranch spanTarget;

    public SpanBranchAssert(Class<T> self, Document doc){
        returnCast = self;
        expectStatus = CatalogueStatus.NO_ID;
        isCatalogued = false;
        expectId = Optional.empty();
        expectStyles = new ArrayList<>();
        assertDoc = doc;
    }

    public T returnCast(){
        return returnCast.cast(this);
    }

    public T setCatalogued(CatalogueStatus status){
        return setCatalogued(status, null);
    }

    public T setCatalogued(CatalogueStatus status, IDBuilder builder){
        expectId = Optional.ofNullable(builder).map(found -> found.build());
        expectStatus = status;
        isCatalogued = true;
        return returnCast();
    }

    public void setCatalogued(){
        isCatalogued = true;
    }

    public boolean isCatalogued(){
        return isCatalogued;
    }

    public CatalogueStatus getCatalogueStatus(){
        return expectStatus;
    }

    public void setStyles(StyleInfo ... styles){
        expectStyles.addAll(Arrays.asList(styles));
    }

    public void addStyles(StyleInfo ... styles){
        expectStyles.addAll(Arrays.asList(styles));
    }

    public void setup(){}

    public SpanBranch test(int size, String rawText, int ... idx){
        setup();
        SpanBranch span = assertDoc.assertChild(size, rawText, idx);

        assertArrayEquals(expectStyles.toArray(),
            span.getBranchStyles().toArray(), "styles");
    
        ArrayList<Executable> catalogue = new ArrayList<>();
        if (isCatalogued){
            catalogue.add(() -> assertTrue(span instanceof Catalogued));
            Optional<CatalogueIdentity> test = span instanceof Catalogued?
                ((Catalogued)span.getSpanIdentity(): Optional.empty();
            if (expectId.isPresent()){
                catalogue.add(() -> assertTrue(test.isPresent()));
                catalogue.add(() -> assertEquals(expectId.get(), test.get()));
            } else {
                catalogue.add(() -> assertFalse(test.isPresent()));
            }
        }
        catalogue.add(() -> assertEquals(expectStatus, span.getIdStatus()));
        assertAll("catalogue id", catalogue);
        
        test(span);
        return span;
    }

    protected static <U> T assertClass(SpanBranch span, Class<U> clazz){
        assertEquals(clazz, spanTarget.getClass(), "span class");
        return clazz.cast(spanTarget);
	}

    protected <U> void assertSpan(Optional<U> expect, 
			Supplier<SpanBranch> supplier, String message){
		Optional<SpanBranch> span = supplier.get();
		
        ArrayList<Executable> tests = new ArrayList<>();
        if (expect.isPresent()){
			tests.add(() -> assertTrue(span.isPresent());
			tests.add(() -> assertSame(expect.get(), test.orElse(null));
        } else {
			tests.add(() -> assertFalse(span.isPresent());
        }
        assertAll(message, tests);
    }

    protected abstract void test(SpanBranch branch);
}
