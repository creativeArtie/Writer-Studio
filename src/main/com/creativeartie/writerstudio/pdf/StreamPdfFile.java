package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.value.*;
import com.creativeartie.writerstudio.resource.*;

public class StreamPdfFile implements AutoCloseable{
    private final String saveFile;
    private final PDDocument pdfDocument;
    private PDPageContentStream contentStream;
    private FormatterSectionTitle titlePage;
    private FormatterSectionContent writtenContent;
    private final PDFont[] embedFonts;
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

    private PDPage currentPage;

    public StreamPdfFile(String file) throws IOException{
        pdfDocument = new PDDocument();
        embedFonts = new PDFont[FONT_FILES.length];
        int i = 0;
        for (String font: FONT_FILES){
            embedFonts[i++] = PDType0Font.load(pdfDocument, getClass()
                .getResourceAsStream(FONT_FOLDER + font));
        }
        saveFile = file;
        newPage();
    }

    public StreamData newStreamData(){
        return new StreamData(){
            private int pageNumber = 1;
            @Override
            public int getPageNumber(){
                return pageNumber;
            }

            @Override
            public SizedFont getBaseFont(){
                return new SizedFont((mono, bold, italics, superscript) ->
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

            @Override
            public float getWidth(){
                return currentPage.getMediaBox().getWidth();
            }

            @Override
            public float getHeight(){
                return currentPage.getMediaBox().getHeight();
            }

            @Override
            public StreamData resetPageNumber(){
                pageNumber = 1;
                return this;
            }

            @Override
            public StreamData toNextPage(){
                pageNumber++;

                return this;
            }

            @Override
            public StreamData setMetaData(Data data){
                PDDocumentInformation info = pdfDocument.getDocumentInformation();
                info.setAuthor(data.getData(MetaData.AUTHOR));
                info.setCreationDate(new GregorianCalendar());
                info.setCreator(WindowText.PROGRAM_NAME.getText());
                /// info.setKeywords(keywords);
                info.setModificationDate(new GregorianCalendar());
                info.setProducer(WindowText.PROGRAM_NAME.getText());
                /// info.setSubject(subject)
                info.setTitle(data.getData(MetaData.TITLE));
                return this;
            }
        };
    }

    private StreamPdfFile newPage() throws IOException{
        currentPage = new PDPage();
        pdfDocument.addPage(currentPage);
        contentStream = new PDPageContentStream(pdfDocument, currentPage);
        return this;
    }

    StreamPdfFile addPage() throws IOException{
        contentStream.close();
        newPage();
        return this;
    }

    PDPageContentStream getContentStream(){
        return contentStream;
    }

    StreamPdfFile renderText(FormatterMatter matter) throws IOException{
        if (! matter.isEmpty()){
            new StreamTextRender(this, matter).render();
        }
        return this;
    }

    PDPage getPage(){
        return currentPage;
    }

    @Override
    public void close() throws IOException{
        contentStream.close();
        pdfDocument.save(saveFile);
        pdfDocument.close();
    }
}