package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchBasicAsserts.*;
import static com.creativeartie.writer.lang.DocumentAssert.*;
public class FormatAgendaTest{

    public static IDBuilder buildId(String id){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_AGENDA).setId(id);
    }

    private static final SetupParser[] parsers = new SetupParser[]{
        FormatParseAgenda.PARSER
    };

    @Test
    public void missingNone(){
        ///           0123456789
        String raw = "{!Agenda}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("0"), 0);
        FormatAgendaAssert agenda = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("Agenda");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("Agenda")
            .setEnd(false)  .setCount(1);

        agenda.test(3,       raw,      0);
        doc.assertKey( 0, 2, "{!",     0, 0);
        content.test(1,      "Agenda", 0, 1);
        doc.assertText(2, 8, "Agenda", 0, 1, 0);
        doc.assertKey( 8, 9, "}",      0, 2);
        doc.assertRest();
    }

    @Test
    public void missingLastColon(){
        ///           012345
        String raw = "{!abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("0"), 0);
        FormatAgendaAssert agenda = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("abc");
        ContentAssert content = new ContentAssert(doc)
            .setBoth("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        agenda.test(2,       raw,   0);
        doc.assertKey(0, 2, "{!",  0, 0);
        content.test(1,      "abc", 0, 1);
        doc.assertText(2, 5, "abc", 0, 1, 0);
        doc.assertRest();
    }

    @Test
    public void missingLastTwo(){
        ///           012
        String raw = "{!";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("0"), 0);
        FormatAgendaAssert agenda = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("");

        agenda.test(1,       raw,  0);
        doc.assertKey(0, 2, "{!", 0, 0);
        doc.assertRest();
    }

    @Test
    public void missingMiddle(){
        ///           0123
        String raw = "{!}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder builder = doc.addId(buildId("0"), 0);
        FormatAgendaAssert agenda = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("");

        agenda.test(2,       raw,  0);
        doc.assertKey(0, 2, "{!", 0, 0);
        doc.assertKey(2, 3, "}",  0, 1);
        doc.assertRest();
    }

    @Test
    public void missingByNewLine(){
        ///           012345678
        String raw = "{!\nabc}";
        DocumentAssert doc = assertDoc(2, raw, parsers);

        IDBuilder builder = doc.addId(buildId("0"), 0);
        FormatAgendaAssert agenda = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("");

        agenda.test(1,       "{!", 0);
        doc.assertKey(0, 2,  "{!", 0, 0);
        doc.assertRest("\nabc}");
    }

    @Test
    public void editAgendaContent(){
        ///              012345
        String before = "{!ac}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "b", 0);
        ///             0123456
        String after = "{!abc}";
        doc.assertDoc(1, after);

        IDBuilder builder = doc.addId(buildId("0"), 0);
        FormatAgendaAssert agenda = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("abc");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        agenda.test(3,       after, 0);
        doc.assertKey(0, 2,  "{!",  0, 0);
        content.test(1,      "abc", 0, 1);
        doc.assertText(2, 5, "abc", 0, 1, 0);
        doc.assertKey(5, 6,  "}",   0, 2);
        doc.assertRest();
    }

    @Test
    public void editWithEscape(){
        ///              0123
        String before = "{!a}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(3, "\\}", 0);
        ///             012 3456
        String after = "{!a\\}}";
        doc.assertDoc(1, after);

        IDBuilder builder = doc.addId(buildId("0"), 0);
        FormatAgendaAssert agenda = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("a}");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("a}")
            .setEnd(false)  .setCount(1);
        EscapeAssert escape = new EscapeAssert(doc)
            .setEscape("}");

        agenda.test(3,       after,  0);
        doc.assertKey(0, 2, "{!",   0, 0);
        content.test(2,      "a\\}", 0, 1);
        doc.assertText(2, 3, "a",    0, 1, 0);
        escape.test(2,       "\\}",  0, 1, 1);
        doc.assertKey(3, 4,  "\\",   0, 1, 1, 0);
        doc.assertText(4, 5, "}",    0, 1, 1, 1);
        doc.assertKey(5, 6,  "}",    0, 2);
        doc.assertRest();
    }

    @Test
    public void editNewAgenda(){
        ///              012345678901
        String before = "{!abc{!ddd}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(5, "}");
        ///             0123456789012
        String after = "{!abc}{!ddd}";
        doc.assertDoc(2, after);

        IDBuilder builder1 = doc.addId(buildId("00"), 0);
        FormatAgendaAssert agenda1 = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder1)
            .setAgenda("abc");
        ContentAssert content1 = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);
        IDBuilder builder2 = doc.addId(buildId("06"), 1);
        FormatAgendaAssert agenda2 = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder2)
            .setAgenda("ddd");
        ContentAssert content2 = new ContentAssert(doc)
            .setBoth("ddd").setBegin(false)
            .setEnd(false) .setCount(1);

        agenda1.test(3,        "{!abc}", 0);
        doc.assertKey( 0,  2,  "{!",     0, 0);
        content1.test(1,       "abc",    0, 1);
        doc.assertText(2,  5,  "abc",    0, 1, 0);
        doc.assertKey( 5,  6,  "}",      0, 2);
        agenda2.test(3,        "{!ddd}", 1);
        doc.assertKey( 6,  8,  "{!",     1, 0);
        content2.test(1,       "ddd",    1, 1);
        doc.assertText( 8, 11, "ddd",    1, 1, 0);
        doc.assertKey(11, 12,  "}",      1, 2);
        doc.assertRest();
    }

    @Test
    public void editNewContent(){
        ///              0123
        String before = "{!}";
        DocumentAssert doc = assertDoc(1, before, parsers);
        doc.insert(2, "abc", 0);
        ///             01234
        String after = "{!abc}";
        doc.assertDoc(1, after);

        IDBuilder builder = doc.addId(buildId("0"), 0);
        FormatAgendaAssert agenda = new FormatAgendaAssert(doc)
            .setCatalogued(CatalogueStatus.UNUSED, builder)
            .setAgenda("abc");
        ContentAssert content = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        agenda.test(3,       after, 0);
        doc.assertKey(0, 2,  "{!",  0, 0);
        content.test(1,      "abc", 0, 1);
        doc.assertText(2, 5, "abc", 0, 1, 0);
        doc.assertKey(5, 6,  "}",   0, 2);
        doc.assertRest();
    }

}
