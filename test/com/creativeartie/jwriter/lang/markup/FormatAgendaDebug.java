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

        agenda.test(     doc, 3, after,  0);
        doc.assertKeyLeaf( 0, 2, "{!",   0, 0);
        content.test(    doc, 1, "abc",  0, 1);
        doc.assertTextLeaf(2, 5, "abc",  0, 1, 0);
        doc.assertKeyLeaf( 5, 6, "}",    0, 2);

        doc.assertIds();
    }

}
