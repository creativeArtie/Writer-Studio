package com.creativeartie.writerstudio.lang;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.*;

import java.util.*;

public class IDOrderDebug {

    private IDBuilder newId(){
        return new IDBuilder().addCategory("foot");
    }

    @Test
    public void subCategoryDirectorys(){
        IDDocument builder = new IDDocument();
        builder.addId("files", "abc", "dd");
        builder.addId("fly",   "abc", "dd");
        IDAssertions doc = builder.build();
        doc.addId(newId.addCategory("abc", "dd").setId("files"), 0);
        doc.addId(newId().addCategory("abc", "dd").setId("fly"),  1);
        doc.assertIds(builder.build(), true);
    }

    @Test
    public void mix(){
        IDDocument builder = new IDDocument();
        builder.addId("id",   "");
        builder.addId("abc", "abc");
        builder.addId("kkk");
        builder.addId("ddd");
        builder.addId("",    "abc");
        DocumentAssert doc = builder.build();
        doc.addId(newId().addCategory("")   .setId("id"),  2);
        doc.addId(newId()                   .setId("abc"), 0);
        doc.addId(newId()                   .setId("kkk"), 1);
        doc.addId(newId().addCategory("abc").setId("ddd"), 4);
        doc.addId(newId().addCategory("abc").setId(""),    3);
        doc.assertIds(builder.build(), true);
    }

    @Test
    public void sameCategory(){
        IDDocument builder = new IDDocument();
        builder.addId("de", "abc");
        builder.addId("abc");
        builder.addId("aaa", "abc");
        builder.addId("",    "abc");
        DocumentAssert doc = builder.build();
        doc.addId(newId().addCategory("abc").setId("de" ), 3);
        doc.addId(newId().addCategory()     .setId("abc"), 0);
        doc.addId(newId().addCategory("abc").setId("aaa"), 2);
        doc.addId(newId().addCategory("abc").setId(""),    1);
        doc.assertIds(builder.build(), true);
    }

    @Test
    public void subCategory(){
        IDDocument builder = new IDDocument();
        builder.addId("c", "a", "b");
        builder.addId("d", "a", "");
        DocumentAssert doc = builder.build();
        doc.addId(newId() .addCategory("a", "b").setId("c"),1);
        doc.addId(newId().addCategory("a", "" ).setId("d"),0);
        doc.assertIds(builder.build(), true);
    }

    @Test
    public void basicSimpleOrder(){
        IDDocument builder = new IDDocument();
        builder.addId("aaa");
        builder.addId("ccc");
        DocumentAssert doc = builder.build();
        doc.addId(newId().addCategory().setId("aaa"),0);
        doc.addId(newId().addCategory().setId("ccc"),1);
        doc.assertIds(builder.build(), true);
    }

    @Test
    public void longList(){
        IDDocument builder = new IDDocument();
        builder.addId("a");
        builder.addId("b", "a");
        builder.addId("1", "b");
        builder.addId("2", "b");
        builder.addId("3", "b");
        builder.addId("4", "b");
        builder.addId("5", "b");
        builder.addId("6", "b");
        builder.addId("7", "b");
        builder.addId("8", "b", "a");
        builder.addId("c", "ba");
        DocumentAssert doc = builder.build();
        doc.addId(newId()                      .setId("a"), 0);
        doc.addId(newId().addCategory("a")     .setId("b"), 1);
        doc.addId(newId().addCategory("b")     .setId("1"), 2);
        doc.addId(newId().addCategory("b")     .setId("2"), 3);
        doc.addId(newId().addCategory("b")     .setId("3"), 4);
        doc.addId(newId().addCategory("b")     .setId("4"), 5);
        doc.addId(newId().addCategory("b")     .setId("5"), 6);
        doc.addId(newId().addCategory("b")     .setId("6"), 7);
        doc.addId(newId().addCategory("b")     .setId("7"), 8);
        doc.addId(newId().addCategory("b", "a").setId("8"), 9);
        doc.addId(newId().addCategory("ba")    .setId("c"), 10);

        TreeSet<SpanBranch> list = doc.getCatalogue().getIds("b");
        assertEquals("Wrong size.", 8, list.size());

        int i = 1;
        for(SpanBranch span : list){
            CatalogueIdentity id = null;
            id = new CatalogueIdentity(i == 8?
                    Arrays.asList("foot", "b", "a"): Arrays.asList("b"),
                String.valueOf(i));
            CatalogueIdentity test = ((Catalogued)span).getSpanIdentity().get();
            assertEquals(id, test, "Wrong id");
            i++;
        }
    }
}
