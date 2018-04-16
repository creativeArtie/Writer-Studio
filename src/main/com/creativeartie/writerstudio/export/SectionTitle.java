package com.creativeartie.writerstudio.export;

import java.io.*; // IOException
import java.util.*; // ArrayList, GregorianCalendar, List

import org.apache.pdfbox.pdmodel.*; // PDDocumentInformation

import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.markup.*; // WritingData
import com.creativeartie.writerstudio.export.value.*; // LineAlignment, PageAlignment
import com.creativeartie.writerstudio.resource.*; // WindowText

import static com.creativeartie.writerstudio.main.Checker.*;

/** A {@link Section} for the title page
 */
final class SectionTitle extends Section {

    private WritingExporter parentDoc;
    private WritingData exportData;

    private PageContent outputPage;
    private float areaWidth;

    /** Only construcutor.
     *
     * @param parent
     *      input parent data
     * @throws IOException
     *      exceptions thrown from uses of other classes
     */
    SectionTitle(WritingExporter parent) throws IOException{
        super(parent);
        parentDoc = parent;
        outputPage = new PageContent(this);

        areaWidth = outputPage.getRenderWidth();
    }

    /** Render the title page.
     *
     * @param data
     *      rendering data
     * @throws IOException
     *      exceptions thrown from uses of other classes
     */
    void export(ManuscriptFile data) throws IOException{
        exportData = data.getMetaData();
        writeMeta();
        writeTitlePage();
    }

    /** Set the PDF file properties
     *
     * @see #export(ManuscriptFile)
     */
    private void writeMeta(){
        PDDocumentInformation info = getPdfDocument().getDocumentInformation();
        info.setAuthor(exportData.getMetaText(TextDataType.Meta.AUTHOR));
        info.setCreationDate(new GregorianCalendar());
        info.setCreator(WindowText.PROGRAM_NAME.getText());
        info.setKeywords(exportData.getMetaText(TextDataType.Meta.KEYWORDS));
        info.setModificationDate(new GregorianCalendar());
        info.setProducer(WindowText.PROGRAM_NAME.getText());
        info.setSubject(exportData.getMetaText(TextDataType.Meta.SUBJECT));
        info.setTitle(exportData.getMetaText(TextDataType.Meta.TITLE));
    }

    /** Set the PDF file properties
     *
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #export(ManuscriptFile)
     */
    private void writeTitlePage() throws IOException{
        writeTop();
        writeMiddle();
        writeBottom();
    }

    /** Set title page top section.
     *
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #writeTitlePage()
     */
    private void writeTop() throws IOException{
        MatterArea top = new MatterArea(outputPage, PageAlignment.TOP);
        top.addAll(newLines(exportData.getPrint(TextDataType.Area.FRONT_TOP)));
        top.render();
    }

    /** Set title page center section.
     *
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #writeTitlePage()
     */
    private void writeMiddle() throws IOException{
        MatterArea mid = new MatterArea(outputPage, PageAlignment.MIDDLE);
        mid.addAll(newLines(exportData.getPrint(TextDataType.Area.FRONT_CENTER)));
        mid.render();
    }

    /** Set title page bottom section.
     *
     * @throws IOException
     *      exceptions thrown from uses of other classes
     * @see #writeTitlePage()
     */
    private void writeBottom() throws IOException{
        MatterArea bot = new MatterArea(outputPage, PageAlignment.BOTTOM);
        bot.addAll(newLines(exportData.getPrint(TextDataType.Area.FRONT_BOTTOM)));
        bot.render();
    }

    /** Create the lines of the {@link TextDataSpanPrint} that was found.
     *
     * @param spans
     *      rending spans
     * @return answer
     * @see writeTop()
     * @see writeCenter()
     * @see writeBottom()
     */
    private List<DivisionTextFormatted> newLines(List<TextDataSpanPrint> spans)
            throws IOException{
        return DivisionTextFormatted.newPrintLines(outputPage.getRenderWidth(),
            parentDoc, spans);
    }

    @Override
    public void close() throws IOException{
        outputPage.close();
    }

}