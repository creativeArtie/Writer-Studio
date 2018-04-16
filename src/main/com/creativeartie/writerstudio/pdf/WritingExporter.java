package com.creativeartie.writerstudio.pdf;

import java.io.*; // AutoCloseable, IOException
import java.util.*; // ArrayList, Iterator, Optional

import com.google.common.collect.*; // AbstractSequentialIterator

import org.apache.pdfbox.pdmodel.*; // PDDocument
import org.apache.pdfbox.pdmodel.font.*; // PDFont, PDFontDescriptor, PDType0Font

import com.creativeartie.writerstudio.pdf.value.*; // ContentFont, Utilities
import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.*; // Span, SpanNode
import com.creativeartie.writerstudio.lang.markup.*; // (many)

import static com.creativeartie.writerstudio.main.Checker.*;

/** Exports the pdf file and the main interaction of the package.
 *
 * Purpose:
 * <ul>
 * <li>Exports a PDF file</li>
 * <li>Manages objects of this package, esp. {@link Section}.</li>
 * </ul>
 */
public final class WritingExporter implements AutoCloseable{

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

    /** Font that can be use in the package
     */
    class PdfFont extends ContentFont{

        /** Only public constructor. */
        PdfFont(){
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

        /** Construtor for {#link proude(ContentFont, Key, Object)
         *
         * @param res
         *      copying {@link ContentFont}
         * @param edit
         *      editing field
         * @param replace
         *      replacing value
         * @see #produce(ContentFont, Key, Object)
         */
        private PdfFont(ContentFont res, Key edit, Object replace){
            super(res, edit, replace);
        }

        @Override
        public float getWidth(String text) throws IOException{
            checkNotNull(text, "text");

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
            checkNotNull(font, "font");
            checkNotNull(key, "key");
            checkNotNull(value, "value");

            return new PdfFont(font, key, value);
        }
    }

    private final String savePath;
    private final PDDocument pdfDocument;
    private final PDFont[] embedFonts;

    private ManuscriptFile exportingFile;
    private final ArrayList<LinedSpanPointNote> endnoteList;
    private final TreeSet<FormattedSpan> citationList;

    /** Only constructor.
     *
     * @param path
     *      file path
     * @throws IOException
     *      from rendering file
     */
    public WritingExporter(String path) throws IOException{
        savePath = checkNotNull(path, "path");
        pdfDocument = new PDDocument();

        /// setup fonts
        embedFonts = new PDFont[FONT_FILES.length];
        int i = 0;
        for (String font: FONT_FILES){
            embedFonts[i++] = PDType0Font.load(pdfDocument, getClass()
                .getResourceAsStream(FONT_FOLDER + font));
        }

        /// initalize list and sets
        endnoteList = new ArrayList<>();
        citationList = new TreeSet<>(Comparator.comparing(
            /// use parsed text to remove duplication + to sort by alphabetlly
            s -> s.getParsedText()
        ));
    }

    /** exports a {@link ManuscriptFile}.
     *
     * @param path
     *      file path
     * @throws IOException
     *      from rendering file
     */
    public void export(ManuscriptFile data) throws IOException{
        checkNotNull(data, "data");

        exportingFile = data;

        /// Prep the file
        endnoteList.clear();
        citationList.clear();

        /// Export title page
        try (SectionTitle front = new SectionTitle(this)){
            front.export(data);
        }

        /// Export main data
        try (SectionContentMain content = new SectionContentMain(this)){
            /// setup
            content.addHeader(data);

            /// find LinedSpans
            Optional<LinedSpan> first = data.getDocument().locateSpan(0,
                LinedSpan.class);
            Iterator<LinedSpan> spans = first.map(f -> listChildren(f))
                .orElse(new ArrayList<LinedSpan>().iterator());

            /// Add the lines
            while (spans.hasNext()){
                content.addLine(spans.next());
            }

            /// Add the document ending lines
            content.addLines(data.getMetaData().getPrint(TextDataType.Area.
                MAIN_ENDER));
        }

        /// Add endnotes pages
        if (! endnoteList.isEmpty()){
            try (SectionContentEndnote endnote = new SectionContentEndnote(
                    this)){
                for (LinedSpanPointNote note: endnoteList){
                    endnote.addLine(note);
                }
            }
        }

        /// Add citation pages
        if (! citationList.isEmpty()){
            try (SectionContentCite citation = new SectionContentCite(this)){
                citation.addTitle(data.getMetaData());
                for (FormattedSpan cite: citationList){
                    citation.addLine(cite);
                }
            }
        }
    }

    /** list the {@link LinedSpan}.
     *
     * @param first
     *      the first line span
     * @return answer
     * @see #export(ManuscriptFile)
     */
    private Iterator<LinedSpan> listChildren(LinedSpan first){
        assert first != null: "Null first";
        return new AbstractSequentialIterator<LinedSpan>(first){
              protected LinedSpan computeNext(LinedSpan previous) {
                  return computeChild(previous);
              }
        };
    }

    /** Gets the list LinedSpan.
     *
     * @param previous
     *      previous rendered {@link LinedSpan}.
     * @return answer
     * @see #listChildren(LinedSpan)
     */
    private LinedSpan computeChild(SpanNode<?> previous){
        assert previous != null: "Null first";

        /// Gets the parent of the span
        SpanNode<?> parent = previous.getParent();

        /// find the location of the span
        int location = previous.getPosition();
        while (location + 1 == parent.size()){
            if (parent instanceof Document){
                /// got to the last span of the document
                return null;
            }

            /// go to the next parent that has more children
            previous = parent;
            parent = parent.getParent();
            location = previous.getPosition();
        }

        /// next child
        location++;
        Span ptr = parent.get(location);

        /// search for the child
        while (! (ptr instanceof LinedSpan)){
            if (ptr instanceof SectionSpan){
                /// go to the first span of the section
                ptr = ((SectionSpan)ptr).get(0);
                location = 0;
            } else {
                /// skip the notes (for now)
                ptr = parent.get(location++);
            }
        }

        return (LinedSpan) ptr;
    }

    /** Insert and endnote and return a number.
     *
     * @param note
     *      note card
     * @return answer
     */
    String addEndnote(LinedSpanPointNote note){
        checkNotNull(note, "note");

        int index = endnoteList.indexOf(note);
        if (index == -1){
            index = endnoteList.size();
            endnoteList.add(note);
        }
        return Utilities.toRomanSuperscript(index + 1);
    }

    /** Insert and citiation and in-text line.
     *
     * @param note
     *      note card
     * @return answer
     */
    Optional<LinedSpanCite> addCitation(NoteCardSpan note){
        checkNotNull(note, "note");

        /// gets the source and in line
        Optional<FormattedSpan> cite = note.getSource();
        Optional<LinedSpanCite> inText = note.getInTextLine();

        /// Adds the citation if not found.
        if (cite.isPresent() && inText.isPresent()){
            citationList.add(cite.get());
        }
        return inText;
    }

    /** Gets the output document
     * @return answer
     */
    public PDDocument getPdfDocument(){
        return pdfDocument;
    }

    /** Gets the output data file
     * @return answer
     */
    ManuscriptFile getDataFile(){
        return exportingFile;
    }

    @Override
    public void close() throws IOException{
        pdfDocument.save(savePath);
        pdfDocument.close();
    }
}