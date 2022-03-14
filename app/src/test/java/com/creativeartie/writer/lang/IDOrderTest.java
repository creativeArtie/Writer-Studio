package com.creativeartie.writer.lang;

import org.junit.jupiter.api.*;

import java.util.*;

import com.creativeartie.writer.lang.markup.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order of Catalogue Identities")
public class IDOrderTest {
    /// Creates a quick document
    private static class IDDocument{

        private StringBuilder rawText;

        IDDocument(){
            rawText = new StringBuilder();
        }

        IDDocument addFoot(String id, String ... categories){
            rawText.append("!^").append(addId(id, categories)).append("\n");
            return this;
        }

        IDDocument addEnd(String id, String ... categories){
            rawText.append("!*").append(addId(id, categories)).append("\n");
            return this;
        }

        private StringBuilder addId(String id, String ... categories){
            StringBuilder ans = new StringBuilder();
            boolean first = true;
            for (String category: categories){
                if (first){
                    first = false;
                } else {
                    ans.append("-");
                }
                ans.append(category);
            }
            return ans.append((first? "": "-")).append(id);
        }

        Document build(){
            return new WritingText(rawText.toString());
        }
    }

    private IDBuilder newFootId(){
        return new IDBuilder().addCategory("foot");
    }

    private IDBuilder newEndId(){
        return new IDBuilder().addCategory("end");
    }

    @Test
    @DisplayName("Same 2 Subcategory, Different Name")
    public void sameCateogry2(){
        IDDocument builder = new IDDocument();
        builder.addFoot("files", "abc", "dd");
        builder.addFoot("fly",   "abc", "dd");
        IDAssert doc = new IDAssert();
        doc.addId(newFootId().addCategory("abc", "dd").setId("files"), 0);
        doc.addId(newFootId().addCategory("abc", "dd").setId("fly"),  1);
        doc.assertIds(builder.build(), true);
    }

    @Test
    @DisplayName("Footnote with Different Subcategories")
    public void differentSubCategory(){
        IDDocument builder = new IDDocument();
        builder.addFoot("id",   "");
        builder.addFoot("abc");
        builder.addEnd( "kkk");
        builder.addFoot("ddd", "abc");
        builder.addFoot("",    "abc");
        IDAssert doc = new IDAssert();
        doc.addId(newFootId().addCategory("")   .setId("id"),  2);
        doc.addId(newFootId()                   .setId("abc"), 1);
        doc.addId(newEndId()                    .setId("kkk"), 0);
        doc.addId(newFootId().addCategory("abc").setId("ddd"), 4);
        doc.addId(newFootId().addCategory("abc").setId(""),    3);
        doc.assertIds(builder.build(), true);
    }

    @Test
    @DisplayName("Same 1 Subcategory, Different Name")
    public void sameCategory1(){
        IDDocument builder = new IDDocument();
        builder.addFoot("de", "abc");
        builder.addFoot("see", "abc");
        builder.addFoot("aaa", "abc");
        builder.addFoot("",    "abc");
        IDAssert doc = new IDAssert();
        doc.addId(newFootId().addCategory("abc").setId("de" ), 2);
        doc.addId(newFootId().addCategory("abc").setId("see"), 3);
        doc.addId(newFootId().addCategory("abc").setId("aaa"), 1);
        doc.addId(newFootId().addCategory("abc").setId(""),    0);
        doc.assertIds(builder.build(), true);
    }

    @Test
    @DisplayName("With empty subcategory")
    public void emptySubcategory(){
        IDDocument builder = new IDDocument();
        builder.addFoot("c", "a", "b");
        builder.addFoot("d", "a", "");
        IDAssert doc = new IDAssert();
        doc.addId(newFootId() .addCategory("a", "b").setId("c"),1);
        doc.addId(newFootId().addCategory("a", "" ).setId("d"),0);
        doc.assertIds(builder.build(), true);
    }

    @Test
    @DisplayName("With no subcategory")
    public void noSubcategory(){
        IDDocument builder = new IDDocument();
        builder.addFoot("aaa");
        builder.addFoot("ccc");
        IDAssert doc = new IDAssert();
        doc.addId(newFootId().addCategory().setId("aaa"),0);
        doc.addId(newFootId().addCategory().setId("ccc"),1);
        doc.assertIds(builder.build(), true);
    }

    @Test
    @DisplayName("CatalogueMap#getIds(String) Test")
    public void longList(){
        IDDocument builder = new IDDocument();
        builder.addEnd( "a");
        builder.addEnd( "b", "a");
        builder.addFoot("0");
        builder.addFoot("1", "b");
        builder.addFoot("2", "b");
        builder.addFoot("3", "b");
        builder.addFoot("4", "b");
        builder.addFoot("5", "b");
        builder.addFoot("6", "b");
        builder.addFoot("7", "b");
        builder.addFoot("8", "b", "a");
        IDAssert doc = new IDAssert();
        doc.addId(newEndId()                       .setId("a"), 0);
        doc.addId(newEndId() .addCategory("a")     .setId("b"), 1);
        doc.addId(newFootId()                      .setId("0"), 2);
        doc.addId(newFootId().addCategory("b")     .setId("1"), 3);
        doc.addId(newFootId().addCategory("b")     .setId("2"), 4);
        doc.addId(newFootId().addCategory("b")     .setId("3"), 5);
        doc.addId(newFootId().addCategory("b")     .setId("4"), 6);
        doc.addId(newFootId().addCategory("b")     .setId("5"), 7);
        doc.addId(newFootId().addCategory("b")     .setId("6"), 8);
        doc.addId(newFootId().addCategory("b")     .setId("7"), 9);
        doc.addId(newFootId().addCategory("b", "a").setId("8"), 10);

        Document out = builder.build();
        doc.assertIds(out, true);

        TreeSet<SpanBranch> list = out.getDocument().getCatalogue()
            .getIds("foot");
        assertEquals(9, list.size(), "size");

        int i = 0;
        for(SpanBranch span : list){
            CatalogueIdentity id = null;
            id = new CatalogueIdentity(i == 8?
                    Arrays.asList("foot", "b", "a"):
                    (i == 0? Arrays.asList("foot"): Arrays.asList("foot", "b")),
                String.valueOf(i));
            CatalogueIdentity test = ((Catalogued)span).getSpanIdentity().get();
            assertEquals(id, test, "Wrong id");
            i++;
        }
    }
}
