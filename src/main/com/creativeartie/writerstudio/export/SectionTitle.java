package com.creativeartie.writerstudio.export;

import java.io.*; // IOException
import java.util.*; // ArrayList, GregorianCalendar, List

import org.apache.pdfbox.pdmodel.*; // PDDocumentInformation

import com.creativeartie.writerstudio.file.*; // ManuscriptFile
import com.creativeartie.writerstudio.lang.markup.*; // WritingData
import com.creativeartie.writerstudio.export.value.*; // LineAlignment, PageAlignment
import com.creativeartie.writerstudio.resource.*; // WindowText

public class SectionTitle extends Section {

    private WritingExporter parentDoc;
    private WritingData exportData;
    private PageContent outputPage;
    private float areaWidth;

    SectionTitle(WritingExporter parent) throws IOException{
        super(parent);
        parentDoc = parent;
        outputPage = new PageContent(this);
        areaWidth = outputPage.getRenderWidth();
    }

    void export(ManuscriptFile data) throws IOException{
        exportData = data.getMetaData();
        writeMeta();
        writeTitlePage();
    }

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

    private void writeTitlePage() throws IOException{
        writeTop();
        writeMiddle();
        writeBottom();
    }

    private void writeTop() throws IOException{
        MatterArea top = new MatterArea(outputPage, PageAlignment.TOP);
        top.addAll(newLines(exportData.getPrint(TextDataType.Area.FRONT_TOP)));
        top.render();
    }

    public void writeMiddle() throws IOException{
        MatterArea mid = new MatterArea(outputPage, PageAlignment.MIDDLE);
        mid.addAll(newLines(exportData.getPrint(TextDataType.Area.FRONT_CENTER)));
        mid.render();
    }

    public void writeBottom() throws IOException{
        MatterArea bot = new MatterArea(outputPage, PageAlignment.BOTTOM);
        bot.addAll(newLines(exportData.getPrint(TextDataType.Area.FRONT_BOTTOM)));
        bot.render();
    }

    private List<DivisionTextFormatted> newLines(List<TextDataSpanPrint> spans)
            throws IOException{
        ArrayList<DivisionTextFormatted> ans = new ArrayList<>();
        float width = outputPage.getRenderWidth();
        for(TextDataSpanPrint span: spans){
            ans.add(new DivisionTextFormatted(width, parentDoc)
                .addContent(span));
        }
        return ans;
    }

    @Override
    public void close() throws IOException{
        outputPage.close();
    }

}