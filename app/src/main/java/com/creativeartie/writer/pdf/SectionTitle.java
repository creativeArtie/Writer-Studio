package com.creativeartie.writer.pdf;

import java.io.*; // IOException
import java.util.*; // ArrayList, GregorianCalendar, List

import org.apache.pdfbox.pdmodel.*; // PDDocumentInformation

import com.creativeartie.writer.lang.markup.*; // WritingData
import com.creativeartie.writer.pdf.value.*; // LineAlignment, PageAlignment
import com.creativeartie.writer.resource.*; // WindowText

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
     */
    void export(WritingFile data) throws IOException{
        exportData = data.getMetaData();
        writeMeta();
        writeTitlePage();
    }

    /** Set the PDF file properties
     *
     * @see #export(WritingFile)
     */
    private void writeMeta(){
        PDDocumentInformation info = getPdfDocument().getDocumentInformation();
        info.setAuthor(exportData.getInfo(TextTypeInfo.AUTHOR));
        info.setCreationDate(new GregorianCalendar());
        info.setCreator(WindowText.PROGRAM_NAME.getText());
        info.setKeywords(exportData.getInfo(TextTypeInfo.KEYWORDS));
        info.setModificationDate(new GregorianCalendar());
        info.setProducer(WindowText.PROGRAM_NAME.getText());
        info.setSubject(exportData.getInfo(TextTypeInfo.SUBJECT));
        info.setTitle(exportData.getInfo(TextTypeInfo.TITLE));
    }

    /** Set the PDF file properties
     *
     * @see #export(WritingFile)
     */
    private void writeTitlePage() throws IOException{
        writeTop();
        writeMiddle();
        writeBottom();
    }

    /** Set title page top section.
     *
     * @see #writeTitlePage()
     */
    private void writeTop() throws IOException{
        MatterArea top = new MatterArea(outputPage, PageAlignment.TOP);
        top.addAll(newLines(exportData.getMatter(TextTypeMatter.FRONT_TOP)));
        top.render();
    }

    /** Set title page center section.
     *
     * @see #writeTitlePage()
     */
    private void writeMiddle() throws IOException{
        MatterArea mid = new MatterArea(outputPage, PageAlignment.MIDDLE);
        mid.addAll(newLines(exportData.getMatter(TextTypeMatter.FRONT_CENTER)));
        mid.render();
    }

    /** Set title page bottom section.
     *
     * @see #writeTitlePage()
     */
    private void writeBottom() throws IOException{
        MatterArea bot = new MatterArea(outputPage, PageAlignment.BOTTOM);
        bot.addAll(newLines(exportData.getMatter(TextTypeMatter.FRONT_BOTTOM)));
        bot.render();
    }

    /** Create the lines of the {@link TextSpanMatter} that was found.
     *
     * @param spans
     *      rending spans
     * @return answer
     * @see writeTop()
     * @see writeCenter()
     * @see writeBottom()
     */
    private List<DivisionTextFormatted> newLines(List<TextSpanMatter> spans)
            throws IOException{
        return DivisionTextFormatted.newPrintLines(outputPage.getRenderWidth(),
            parentDoc, spans);
    }

    @Override
    public void close() throws IOException{
        outputPage.close();
    }

}
