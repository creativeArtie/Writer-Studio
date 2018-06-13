package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

/// Group of tests related to SpanBranch
public abstract class SpanBranchAssert<T extends SpanBranchAssert>{

    /// %Part 1: Fields and Constructor ########################################

    private DocumentAssert assertDoc;
    private Optional<CatalogueIdentity> expectId;
    private CatalogueStatus expectStatus;
    private boolean isCatalogued;
    private Class<T> returnCast;
    private SpanBranch spanTarget;
    private boolean isId;

    public SpanBranchAssert(Class<T> self, DocumentAssert doc){
        returnCast = self;
        expectStatus = CatalogueStatus.NO_ID;
        isCatalogued = false;
        expectId = Optional.empty();
        assertDoc = doc;
        isId = false;
    }


    /// %Part 2: Setups ########################################################
    /// %Part 2.1: Set Catalogued ==============================================

    /// span is a {@link Catalogued} but without id.
    public T setCatalogued(CatalogueStatus status){
        return setCatalogued(status, null);
    }

    /// span is a {@link Catalogued} with an id.
    public T setCatalogued(CatalogueStatus status, IDBuilder builder){
        expectId = Optional.ofNullable(builder).map(found -> found.build());
        expectStatus = status;
        isCatalogued = true;
        return cast();
    }

    public T setCatalogued(){
        isCatalogued = true;
        return cast();
    }

    public T setId(boolean id){
        isId = id;
        return cast();
    }

    public boolean isCatalogued(){
        return isCatalogued;
    }

    public CatalogueStatus getCatalogueStatus(){
        return expectStatus;
    }

    /// %Part 2.3: Other Setups ================================================

    /// Setup other things
    public abstract void setup();

    /// cast it self back for nice chain calling.
    protected T cast(){
        return returnCast.cast(this);
    }

    /// %Part 3: Assertion Runs ################################################

    /// %Part 3.1: Main Test ###################################################

    /// Main test function
    public SpanBranch test(int size, String rawText, int ... idx){
        setup();
        SpanBranch span = assertDoc.assertChild(size, rawText, idx);
        spanTarget = span;

        ArrayList<Executable> list = new ArrayList<>();

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
            catalogue.add(() -> assertEquals(isId, ((Catalogued) span).isId(),
                "isId()"));
        } else {
            catalogue.add(() -> assertFalse(span instanceof Catalogued));
        }
        catalogue.add(() -> assertEquals(expectStatus, span.getIdStatus()));
        list.add(() -> assertAll("catalogue id", catalogue));

        ArrayList<Executable> gets = new ArrayList<>();
        list.add(() -> test(span, gets));
        list.add(() -> assertAll("gets", gets));
        assertAll(span.toString(), list);
        return span;
    }

    /// Tests related to the class.
    protected abstract void test(SpanBranch branch, ArrayList<Executable> tests);

    /// covert the {@link SpanBranch} to the correct class.
    protected <U> U assertClass(Class<U> clazz){
        assertEquals(clazz, spanTarget.getClass(), "span class");
        return clazz.cast(spanTarget);
    }

    protected Executable assertSpan(Class<? extends SpanBranch> clazz,
            int[] location, Supplier<? extends SpanBranch> supplier,
            String message)
    {
        return () -> {
            SpanBranch expect = assertDoc.assertChild(clazz, location);
            SpanBranch test = supplier.get();
            assertSame(expect, test, message);
        };
    }

    /// Find a child span
    protected Executable assertChild(Class<? extends SpanBranch> clazz,
            List<int[]> locations, Supplier<List<? extends SpanBranch>> supplier,
            String message)
    {
        return () ->
        {
            ArrayList<? extends SpanBranch> expects = new ArrayList<>();
            ArrayList<Executable> list = new ArrayList<>();
            for (int[] location: locations)
            {
                list.add(() -> {assertDoc.assertChild(clazz, location);});
            }
            assertAll("exepcts", list);

            List<? extends SpanBranch> test = supplier.get();
            ArrayList<Executable> tests = new ArrayList<>();
            tests.add(() -> assertEquals(expects.size(), test.size(), "size"));
            if (expects.size() == test.size()){
                int i = 0;
                for(SpanBranch expect: expects){
                    SpanBranch found = test.get(i);
                    String pos = "position:" + i;
                    tests.add(() -> assertSame(expect, found, pos));
                    i++;
                }
            }
            assertAll(message, tests);

        };
    }

    protected Executable assertChild(Class<? extends SpanBranch> clazz,
            int[] location, Supplier<Optional<? extends SpanBranch>> supplier,
            String message)
    {
        return () -> {
            Optional<? extends SpanBranch> expect = Optional.ofNullable(
                location != null? null: assertDoc.assertChild(clazz, location)
            );
            Optional<? extends SpanBranch> test = supplier.get();

            ArrayList<Executable> tests = new ArrayList<>();
            if (expect.isPresent()){
                tests.add(() -> assertTrue(test.isPresent()));
                tests.add(() -> assertSame(expect.get(), test.orElse(null)));
            } else {
                tests.add(() -> assertFalse(test.isPresent()));
            }
            assertAll(message, tests);
        };
    }

}
