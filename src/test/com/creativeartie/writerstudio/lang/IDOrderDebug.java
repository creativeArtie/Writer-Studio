package com.creativeartie.writerstudio.lang;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.TreeSet;

@RunWith(JUnit4.class)
public class IDOrderDebug {
    @Test
    public void subCategoryDirectorys(){
        IDTestDocument doc = new IDTestDocument();
        IDBuilder builder = new IDBuilder();
        doc.addId(builder        .addCategory("abc", "dd").setId("files"), 0);
        doc.addId(builder.reset().addCategory("abc", "dd").setId("fly"),  1);
        doc.assertIds(true);
    }

    @Test
    public void mix(){
        IDTestDocument doc = new IDTestDocument();
        IDBuilder builder = new IDBuilder();
        doc.addId(builder        .addCategory("")   .setId("id"),  2);
        doc.addId(builder.reset()                   .setId("abc"), 0);
        doc.addId(builder.reset()                   .setId("kkk"), 1);
        doc.addId(builder.reset().addCategory("abc").setId("ddd"), 4);
        doc.addId(builder.reset().addCategory("abc").setId(""),    3);
        doc.assertIds(true);
    }

    @Test
    public void sameCategory(){
        IDTestDocument doc = new IDTestDocument();
        IDBuilder builder = new IDBuilder();
        doc.addId(builder        .addCategory("abc").setId("de" ), 3);
        doc.addId(builder.reset().addCategory()     .setId("abc"), 0);
        doc.addId(builder.reset().addCategory("abc").setId("aaa"), 2);
        doc.addId(builder.reset().addCategory("abc").setId(""),    1);
        doc.assertIds(true);
    }

    @Test
    public void subCategory(){
        IDTestDocument doc = new IDTestDocument();
        IDBuilder builder = new IDBuilder();
        doc.addId(builder         .addCategory("a", "b").setId("c"),1);
        doc.addId(builder.reset().addCategory("a", "" ).setId("d"),0);
        doc.assertIds(true);
    }

    @Test
    public void basicSimpleOrder(){
        IDTestDocument doc = new IDTestDocument();
        IDBuilder builder = new IDBuilder();
        doc.addId(builder        .addCategory().setId("aaa"),0);
        doc.addId(builder.reset().addCategory().setId("ccc"),1);
        doc.assertIds(true);
    }

    @Test
    public void longList(){
        IDTestDocument setup = new IDTestDocument();
        IDBuilder builder = new IDBuilder();
        setup.addId(builder                              .setId("a"), 0);
        setup.addId(builder.reset().addCategory("a")     .setId("b"), 1);
        setup.addId(builder.reset().addCategory("b")     .setId("1"), 2);
        setup.addId(builder.reset().addCategory("b")     .setId("2"), 3);
        setup.addId(builder.reset().addCategory("b")     .setId("3"), 4);
        setup.addId(builder.reset().addCategory("b")     .setId("4"), 5);
        setup.addId(builder.reset().addCategory("b")     .setId("5"), 6);
        setup.addId(builder.reset().addCategory("b")     .setId("6"), 7);
        setup.addId(builder.reset().addCategory("b")     .setId("7"), 8);
        setup.addId(builder.reset().addCategory("b", "a").setId("8"), 9);
        setup.addId(builder.reset().addCategory("ba")    .setId("c"), 10);
        Document doc = setup.build();
        setup.assertIds(doc, true);

        TreeSet<SpanBranch> list = doc.getCatalogue().getIds("b");
        assertEquals("Wrong size.", 8, list.size());

        int i = 1;
        for(SpanBranch span : list){
            CatalogueIdentity id = null;
            id = new CatalogueIdentity(i == 8? Arrays.asList("b", "a"):
                Arrays.asList("b"), String.valueOf(i));
            CatalogueIdentity test = ((Catalogued)span).getSpanIdentity().get();
            assertEquals("Wrong id.", id, test);
            i++;
        }
    }
}
