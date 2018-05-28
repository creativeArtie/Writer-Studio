package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

/// Group of tests related to SpanBranch
public abstract class SpanBranchAssert<T extends SpanBranchAssert>{
    private DocumentAssert assertDoc;
    private Optional<CatalogueIdentity> expectId;
    private CatalogueStatus expectStatus;
    private ArrayList<StyleInfo> expectStyles;
    private boolean isCatalogued;
    private Class<T> returnCast;
    private SpanBranch spanTarget;

    public SpanBranchAssert(Class<T> self, DocumentAssert doc){
        returnCast = self;
        expectStatus = CatalogueStatus.NO_ID;
        isCatalogued = false;
        expectId = Optional.empty();
        expectStyles = new ArrayList<>();
        assertDoc = doc;
    }

    /// cast it self back for nice chain calling.
    protected T returnCast(){
        return returnCast.cast(this);
    }

    /// span is a {@link Catalogued} but without id.
    public T setCatalogued(CatalogueStatus status){
        return setCatalogued(status, null);
    }

    /// span is a {@link Catalogued} with an id.
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

    /// Setup other things
    public void setup(){}

    /// Main test function
    public SpanBranch test(int size, String rawText, int ... idx){
        setup();
        SpanBranch span = assertDoc.assertChild(size, rawText, idx);
        ArrayList<Executable> list = new ArrayList<>();

        list.add(() -> assertArrayEquals(expectStyles.toArray(),
            span.getBranchStyles().toArray(), "styles"));

        /// catalogue testing
        ArrayList<Executable> catalogue = new ArrayList<>();
        if (isCatalogued){
            catalogue.add(() -> assertTrue(span instanceof Catalogued));
            /// Set empty if fail before
            Optional<CatalogueIdentity> test = span instanceof Catalogued?
                ((Catalogued)span).getSpanIdentity(): Optional.empty();
            if (expectId.isPresent()){
                catalogue.add(() -> assertTrue(test.isPresent()));
                catalogue.add(() -> assertEquals(expectId.get(), test.get()));
            } else {
                catalogue.add(() -> assertFalse(test.isPresent()));
            }
        }
        catalogue.add(() -> assertEquals(expectStatus, span.getIdStatus()));
        list.add(() -> assertAll("catalogue id", catalogue));

        test(span, list);
        assertAll(span.toString(), list);
        return span;
    }

    /// covert the {@link SpanBranch} to the correct class.
    protected <U> U assertClass(SpanBranch span, Class<U> clazz){
        assertEquals(clazz, spanTarget.getClass(), "span class");
        return clazz.cast(spanTarget);
    }

    /// Find a child span
    protected <U> void assertSpan(Optional<U> expect,
            Supplier<Optional<SpanBranch>> supplier, String message){
        Optional<SpanBranch> test = supplier.get();

        ArrayList<Executable> tests = new ArrayList<>();
        if (expect.isPresent()){
            tests.add(() -> assertTrue(test.isPresent()));
            tests.add(() -> assertSame(expect.get(), test.orElse(null)));
        } else {
            tests.add(() -> assertFalse(test.isPresent()));
        }
        assertAll(message, tests);
    }

    /// Tests related to the class.
    protected abstract void test(SpanBranch branch, ArrayList<Executable> tests);
}