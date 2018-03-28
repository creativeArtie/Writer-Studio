package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.export.value.*;

public class SectionTitle extends Section {

    private WritingExporter parentDoc;
    private ManuscriptFile exportData;
    private PageContent outputPage;
    private ContentFont contentFont;
    private float areaWidth;

    SectionTitle(WritingExporter parent) throws IOException{
        super(parent);
        parentDoc = parent;
        outputPage = new PageContent(this);
        contentFont = parent. new PdfFont();
        areaWidth = outputPage.getRenderWidth();
    }

    void export(ManuscriptFile data) throws IOException{
        exportData = data;
        writeMeta();
        writeTitlePage();
    }

    private void writeMeta(){
        PDDocumentInformation info = getPdfDocument().getDocumentInformation();
        info.setAuthor(exportData.getText(MetaData.AUTHOR));
        info.setCreationDate(new GregorianCalendar());
        info.setCreator(WindowText.PROGRAM_NAME.getText());
        /// info.setKeywords(keywords);
        info.setModificationDate(new GregorianCalendar());
        info.setProducer(WindowText.PROGRAM_NAME.getText());
        /// info.setSubject(subject)
        info.setTitle(exportData.getText(MetaData.TITLE));
    }

    private void writeTitlePage() throws IOException{
        writeTop();
        writeMiddle();
        writeBottom();
    }

    private void writeTop() throws IOException{
        MatterArea top = new MatterArea(outputPage, PageAlignment.TOP);
        top.add(newLine(MetaData.AGENT_NAME, LineAlignment.LEFT));
        top.add(newLine(MetaData.AGENT_ADDRESS, LineAlignment.LEFT));
        top.add(newLine(MetaData.AGENT_EMAIL, LineAlignment.LEFT));
        top.add(newLine(MetaData.AGENT_PHONE, LineAlignment.LEFT));
        top.render();
    }

    public void writeMiddle() throws IOException{
        MatterArea mid = new MatterArea(outputPage, PageAlignment.MIDDLE);
        mid.add(newLine(MetaData.TITLE, LineAlignment.CENTER, 2));
        mid.add(newLine("By", LineAlignment.CENTER, 2));
        mid.add(newLine(MetaData.AUTHOR, LineAlignment.CENTER, 2));
        mid.render();
    }

    public void writeBottom() throws IOException{
        MatterArea bot = new MatterArea(outputPage, PageAlignment.BOTTOM);
        bot.add(newLine(MetaData.AUTHOR, LineAlignment.RIGHT));
        bot.add(newLine(MetaData.ADDRESS, LineAlignment.RIGHT));
        bot.add(newLine(MetaData.PHONE, LineAlignment.RIGHT));
        bot.add(newLine(MetaData.EMAIL, LineAlignment.RIGHT));
        bot.add(newLine(MetaData.WEBSITE,LineAlignment.RIGHT));
        bot.add(newLine(
            getData(MetaData.AUTHOR) + " © " + getData(MetaData.COPYRIGHT),
            LineAlignment.CENTER, 3
        ));
        bot.render();
    }

    private DivisionLine newLine(MetaData data, LineAlignment alignment)
            throws IOException{
        return newLine(data, alignment, 1);
    }

    private DivisionLine newLine(MetaData data, LineAlignment alignment,
            float leading) throws IOException{
        return newLine(getData(data), alignment, leading);
    }

    private DivisionLine newLine(String text, LineAlignment alignment,
            float leading) throws IOException{
        return new DivisionLine(areaWidth, alignment).setLeading(leading)
            .appendSimpleText(text, contentFont);
    }

    private String getData(MetaData key){
        return exportData.getText(key);
    }

    @Override
    public void close() throws IOException{
        outputPage.close();
    }

}