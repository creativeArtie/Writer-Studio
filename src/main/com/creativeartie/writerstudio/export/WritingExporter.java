package com.creativeartie.writerstudio.export;

import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.export.value.*;

public class WritingExporter implements AutoCloseable{

    private static final String FONT_FOLDER = "/data/fonts/";
    private static final String[] FONT_FILES = {
        "FreeSerif.ttf", "FreeSerifBold.ttf", "FreeSerifItalic.ttf",
        "FreeSerifBoldItalic.ttf",
        "FreeMono.ttf", "FreeMonoBold.ttf", "FreeMonoOblique.ttf",
        "FreeMonoBoldOblique.ttf"
    };
    private static final int SUPERSCRIPT = 0;
    private static final int SERIF = 0;
    private static final int SERIF_BOLD = 1;
    private static final int SERIF_ITALICS = 2;
    private static final int SERIF_BOTH = 3;
    private static final int MONO = 4;
    private static final int MONO_BOLD = 5;
    private static final int MONO_ITALICS = 6;
    private static final int MONO_BOTH = 7;

    public class PdfFont extends ContentFont{

        public PdfFont(){
            super((mono, bold, italics, superscript) ->
                    embedFonts[superscript? SUPERSCRIPT:(mono?
                        (bold?
                            (italics? MONO_BOTH   : MONO_BOLD):
                            (italics? MONO_ITALICS: MONO)
                        ):(bold?
                            (italics? SERIF_BOTH   : SERIF_BOLD):
                            (italics? SERIF_ITALICS: SERIF)
                        )
                    )]
                );
        }

        private PdfFont(ContentFont res, Key edit, Object replace){
            super(res, edit, replace);
        }

        @Override
        public float getWidth(String text) throws IOException{
            /// From https://stackoverflow.com/questions/13701017/calculation-
            /// string-width-in-pdfbox-seems-only-to-count-characters
            return getFont().getStringWidth(text) / 1000 * getSize();
        }

        @Override
        public float getHeight(){
            PDFontDescriptor descipter = getFont().getFontDescriptor();
            return (descipter.getCapHeight() + descipter.getXHeight()) / 1000 *
                getSize();
        }

        @Override
        public PdfFont produce(ContentFont font, Key key, Object value){
            return new PdfFont(font, key, value);
        }
    }

    private final String savePath;
    private final PDDocument pdfDocument;
    private final SectionTitle frontMatter;
    private final PDFont[] embedFonts;

    public WritingExporter(String path) throws IOException{
        savePath = path;
        pdfDocument = new PDDocument();
        embedFonts = new PDFont[FONT_FILES.length];
        int i = 0;
        for (String font: FONT_FILES){
            embedFonts[i++] = PDType0Font.load(pdfDocument, getClass()
                .getResourceAsStream(FONT_FOLDER + font));
        }
        frontMatter = new SectionTitle(this);
    }

    public void export(ManuscriptFile data) throws IOException{
        frontMatter.export(data);

    }

    public PDDocument getPdfDocument(){
        return pdfDocument;
    }

    @Override
    public void close() throws IOException{
        frontMatter.close();
        pdfDocument.save(savePath);
        pdfDocument.close();
    }
}