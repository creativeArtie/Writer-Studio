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

    public SpanBranch test(int size, String rawText, int index){
        setup();
        assertTrue(document.size() > index(), "index out of range");
        SpanBranch span = doc.assertChild(size, rawText, idx);
        targetSpan = span;

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

    protected <U extends SpanBranch> U assertClass(Class<U> clazz){
        assertEquals(clazz, spanTarget.getClass(), "span class");
        return clazz.cast(spanTarget);
    }

    protected <T extends SpanBranch> T assertChild(T expect,
            Supplier<Optional<T>> supplier, String message){
        return assertChild(Optional.ofNullable(expect), supplier, message);
    }

    protected <T extends SpanBranch> T assertChild(Optional<T> expect,
            Supplier<Optional<T>> supplier, String message){
        Optioanl<T> test = supplier.get();
        ArrayList<Executable> found = new ArrayList<>();
        if (expect.isPresent()){
            found.add(() -> assertTrue(test.isPresent()));
            found.add(() -> assertSame(expect.get(), test.get());
        } else {
            found.add(() -> assertFalse(test.isPresent());
        }
        assertAll(message, found);
        return test.orElse(null);
    }

    protected abstract void test(SpanBranch branch);
}
