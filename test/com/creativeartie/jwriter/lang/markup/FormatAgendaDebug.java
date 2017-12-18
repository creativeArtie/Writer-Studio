package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.rules.*;

import java.io.File;
import java.util.Optional;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class FormatAgendaDebug{

    static IDBuilder buildId(String id){
        return new IDBuilder().addCategory("agenda").setId(id);
    }

    private static final SetupParser[] parsers = new SetupParser[]{
        FormatParseAgenda.PARSER};

    @Test
    public void complete(){
        ///           0123456789
        String raw = "{!Agenda}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildId("0");
        doc.addId(id,  0);

        FormatAgendaTest agenda = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, id).setText("Agenda");
        ContentTest content = new ContentTest()
            .setText("Agenda").setBegin(false)
            .setEnd(false)    .setCount(1);

        agenda.test(     doc, 3, raw,      0);
        doc.assertKeyLeaf( 0, 2, "{!",     0, 0);
        content.test(    doc, 1, "Agenda", 0, 1);
        doc.assertTextLeaf(2, 8, "Agenda", 0, 1, 0);
        doc.assertKeyLeaf( 8, 9, "}",      0, 2);

        doc.assertIds();
    }

    @Test
    public void noEnd(){
        ///           012345
        String raw = "{!abc";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatAgendaTest agenda = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, doc.addId(buildId("0"), 0))
            .setText("abc");
        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        agenda.test(     doc, 2, raw,   0);
        doc.assertKeyLeaf( 0, 2, "{!",  0, 0);
        content.test(    doc, 1, "abc", 0, 1);
        doc.assertTextLeaf(2, 5, "abc", 0, 1, 0);

        doc.assertIds();
    }

    @Test
    public void onlyStart(){
        ///           012
        String raw = "{!";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        IDBuilder id = buildId("0");

        FormatAgendaTest agenda = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, doc.addId(buildId("0"), 0))
            .setText("");

        agenda.test(     doc, 1, raw,  0);
        doc.assertKeyLeaf( 0, 2, "{!", 0, 0);

        doc.assertIds();
    }

    @Test
    public void noText(){
        ///           0123
        String raw = "{!}";
        DocumentAssert doc = assertDoc(1, raw, parsers);

        FormatAgendaTest agenda = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, doc.addId(buildId("0"), 0))
            .setText("");

        agenda.test(    doc, 2, raw,  0);
        doc.assertKeyLeaf(0, 2, "{!", 0, 0);
        doc.assertKeyLeaf(2, 3, "}",  0, 1);

        doc.assertIds();

    }

    @Test
    public void editAgendaContent(){
        ///              012345
        String before = "{!ac}";

        DocumentAssert doc = assertDoc(1, before, parsers);

        doc.insert(3, "b", 0, 1);
        ///             0123456
        String after = "{!abc}";
        doc.assertDoc(1, after);

        IDBuilder id = buildId("0");
        doc.addId(id,  0);


        FormatAgendaTest agenda = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, id).setText("abc");
        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        agenda.test(     doc, 3, after,  0);
        doc.assertKeyLeaf( 0, 2, "{!",   0, 0);
        content.test(    doc, 1, "abc",  0, 1);
        doc.assertTextLeaf(2, 5, "abc",  0, 1, 0);
        doc.assertKeyLeaf( 5, 6, "}",    0, 2);

        doc.assertIds();
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

        IDBuilder id = buildId("0");
        doc.addId(id,  0);


        FormatAgendaTest agenda = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, id).setText("a}");
        ContentTest content = new ContentTest()
            .setText("a}").setBegin(false)
            .setEnd(false).setCount(1);
        EscapeTest escape = new EscapeTest()
            .setEscape("}");

        agenda.test(     doc, 3, after,  0);
        doc.assertKeyLeaf( 0, 2, "{!",   0, 0);
        content.test(    doc, 2, "a\\}", 0, 1);
        doc.assertTextLeaf(2, 3, "a",    0, 1, 0);
        escape.test(     doc, 2, "\\}",  0, 1, 1);
        doc.assertKeyLeaf( 3, 4, "\\",   0, 1, 1, 0);
        doc.assertTextLeaf(4, 5, "}",    0, 1, 1, 1);
        doc.assertKeyLeaf( 5, 6, "}",    0, 2);

        doc.assertIds();
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

        IDBuilder id1 = buildId("00");
        doc.addId(id1,  0);
        FormatAgendaTest agenda1 = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, id1).setText("abc");
        ContentTest content1 = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        IDBuilder id2 = buildId("06");
        doc.addId(id2,  1);
        FormatAgendaTest agenda2 = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, id2).setText("ddd");
        ContentTest content2 = new ContentTest()
            .setText("ddd").setBegin(false)
            .setEnd(false) .setCount(1);

        agenda1.test(    doc,  3, "{!abc}", 0);
        doc.assertKeyLeaf( 0,  2, "{!",     0, 0);
        content1.test(   doc,  1, "abc",    0, 1);
        doc.assertTextLeaf(2,  5, "abc",    0, 1, 0);
        doc.assertKeyLeaf( 5,  6, "}",      0, 2);
        agenda2.test(    doc,  3, "{!ddd}", 1);
        doc.assertKeyLeaf( 6,  8, "{!",     1, 0);
        content2.test(   doc,  1, "ddd",    1, 1);
        doc.assertTextLeaf(8, 11, "ddd",    1, 1, 0);
        doc.assertKeyLeaf(11, 12, "}",      1, 2);

        doc.assertIds();
    }

    @Test
    public void editNewAContent(){
        ///              0123
        String before = "{!}";

        DocumentAssert doc = assertDoc(1, before, parsers);

        doc.insert(2, "abc", 0);

        ///             01234
        String after = "{!abc}";
        doc.assertDoc(1, after);

        IDBuilder id = buildId("0");
        doc.addId(id,  0);

        FormatAgendaTest agenda = new FormatAgendaTest()
            .setCatalogued(CatalogueStatus.UNUSED, id).setText("abc");
        ContentTest content = new ContentTest()
            .setText("abc").setBegin(false)
            .setEnd(false) .setCount(1);

        agenda.test(     doc, 3, after, 0);
        doc.assertKeyLeaf( 0, 2, "{!",  0, 0);
        content.test(    doc, 1, "abc", 0, 1);
        doc.assertTextLeaf(2, 5, "abc", 0, 1, 0);
        doc.assertKeyLeaf( 5, 6, "}",   0, 2);

        doc.assertIds();
    }

}
