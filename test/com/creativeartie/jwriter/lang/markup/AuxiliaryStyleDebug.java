package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableListMultimap;

import com.creativeartie.jwriter.lang.*;

@RunWith(Parameterized.class)
public class AuxiliaryStyleDebug{

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {SetupLeafStyle.KEYWORD, "Basic.Keyword"},
            {SetupLeafStyle.ID,      "Basic.Id"     },
            {SetupLeafStyle.FIELD,   "Basic.Field"  },
            {SetupLeafStyle.DATA,    "Basic.Data"   },
            {SetupLeafStyle.PATH,    "Basic.Path"   },
            {SetupLeafStyle.TEXT,    "Basic.Text"   },
            {CatalogueStatus.NO_ID,     "Id.None"  },
            {CatalogueStatus.UNUSED,    "Id.Warning"},
            {CatalogueStatus.NOT_FOUND, "Id.Error"  },
            {CatalogueStatus.MULTIPLE,  "Id.Warning"},
            {CatalogueStatus.READY,     "Id.Ready"  },
            {AuxiliaryStyle.ESCAPE,       "Other.Escape"},
            {AuxiliaryStyle.NO_ID,        "Other.NoId"},
            {AuxiliaryStyle.DATA_ERROR,   "Other.DataError"},
            {AuxiliaryStyle.MAIN_SECTION, "Main.MainSection"},
            {AuxiliaryStyle.MAIN_NOTE,    "Main.MainNote"},
            {AuxiliaryStyle.AGENDA,       "Inline.Agenda"},
            {AuxiliaryStyle.DIRECT_LINK,  "Inline.DirectLink"},
            {AuxiliaryStyle.REF_LINK,     "Inline.RefLink"},
            {DirectoryType.FOOTNOTE, "Inline.Footnote"},
            {DirectoryType.ENDNOTE,  "Inline.Endnote"},
            {DirectoryType.LINK,     "Inline.Link"},
            {DirectoryType.NOTE,     "Inline.Note"},
            {EditionType.STUB,  "Edition.Stub"},
            {EditionType.DRAFT, "Edition.Draft"},
            {EditionType.FINAL, "Edition.Final"},
            {EditionType.OTHER, "Edition.Other"},
            {EditionType.NONE,  "Edition.None"},
            {FormatType.BOLD,      "Format.Bold"},
            {FormatType.ITALICS,   "Format.Italics"},
            {FormatType.UNDERLINE, "Format.Underline"},
            {FormatType.CODED,     "Format.Coded"},
            // {InfoFieldType.PAGES,    "Field.Pages"},
            {InfoFieldType.SOURCE,   "Field.Source"},
            {InfoFieldType.IN_TEXT,  "Field.InText"},
            {InfoFieldType.FOOTNOTE, "Field.Footnote"},
            {InfoFieldType.ERROR,    "Field.Error"},
            {InfoDataType.FORMATTED, "Data.Formatted"},
            {InfoDataType.NUMBER,    "Data.Number"},
            {InfoDataType.TEXT,      "Data.Text"},
            {LinedType.HEADING,   "Lined.Heading"},
            {LinedType.OUTLINE,   "Lined.Outline"},
            {LinedType.QUOTE,     "Lined.Quote"},
            {LinedType.NUMBERED,  "Lined.Numbered"},
            {LinedType.BULLET,    "Lined.Bullet"},
            {LinedType.FOOTNOTE,  "Lined.Footnote"},
            {LinedType.ENDNOTE,   "Lined.Endnote"},
            {LinedType.HYPERLINK, "Lined.Hyperlink"},
            {LinedType.NOTE,      "Lined.Note"},
            {LinedType.AGENDA,    "Lined.Agenda"},
            {LinedType.BREAK,     "Lined.Break"},
            {LinedType.SOURCE,    "Lined.Source"},
            {LinedType.PARAGRAPH, "Lined.Paragraph"}
        });
    }

    @Parameter
    public DetailStyle info;

    @Parameter(1)
    public String styleClass;

    @Test
    public void test(){
        assertEquals("Eror For type: " + info.getClass().getSimpleName() + "." +
            info, styleClass, info.getStyleClass());
    }
}
