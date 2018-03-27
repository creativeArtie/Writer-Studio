package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.export.value.*;

public class SectionTitle implements AutoCloseable{

    private PDDocument pdfDocument;
    private WritingExporter writingExporter;
    private ManuscriptFile exportData;
    private Optional<PageContent> outputPage;

    SectionTitle(){
        outputPage = Optional.empty();
    }

    void export(WritingExporter doc, ManuscriptFile data) throws IOException{
        pdfDocument = doc.getPdfDocument();
        writingExporter = doc;
        exportData = data;
        outputPage = Optional.of(new PageContent(pdfDocument));
        writeMeta();
        writeTitlePage();
    }

    private void writeMeta(){
        PDDocumentInformation info = pdfDocument.getDocumentInformation();
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
        outputPage = Optional.of(new PageContent(pdfDocument));
        writeTop();
        writeMiddle();
        writeBottom();
    }

    private void writeTop() throws IOException{
        PageContent page = outputPage.get();
        MatterArea top = new MatterArea(page.getHeight(), PageAlignment.TOP);
        ContentFont font = writingExporter. new PdfFont();
        ArrayList<DivisionLine> ans = new ArrayList<>();
        float width = page.getWidth();
        ans.add(newBlock(MetaData.AGENT_NAME, LineAlignment.LEFT, width, font));
        ans.add(newBlock(MetaData.AGENT_ADDRESS, LineAlignment.LEFT, width, font));
        ans.add(newBlock(MetaData.AGENT_EMAIL, LineAlignment.LEFT, width, font));
        ans.add(newBlock(MetaData.AGENT_PHONE, LineAlignment.LEFT, width, font));

    }

    public void writeMiddle() throws IOException{
        PageContent page = outputPage.get();
        ContentFont font = writingExporter.new PdfFont();
        ArrayList<DivisionLine> ans = new ArrayList<>();
        float width = page.getWidth();
        ans.add(newBlock(MetaData.TITLE, LineAlignment.CENTER, width, 2, font));
        ans.add(newBlock("By", LineAlignment.CENTER, width, 2, font));
        ans.add(newBlock(MetaData.AUTHOR, LineAlignment.CENTER, width, 2, font));
    }

    public void writeBottom() throws IOException{
        PageContent page = outputPage.get();
        ContentFont font = writingExporter.new PdfFont();
        ArrayList<DivisionLine> ans = new ArrayList<>();
        float width = page.getWidth();
        ans.add(newBlock(MetaData.AUTHOR, LineAlignment.RIGHT, width, font));
        ans.add(newBlock(MetaData.ADDRESS, LineAlignment.RIGHT, width, font));
        ans.add(newBlock(MetaData.PHONE, LineAlignment.RIGHT, width, font));
        ans.add(newBlock(MetaData.EMAIL, LineAlignment.RIGHT, width, font));
        ans.add(newBlock(MetaData.WEBSITE,LineAlignment.RIGHT, width, font));
        ans.add(newBlock(
            getData(MetaData.AUTHOR) + " © " + getData(MetaData.COPYRIGHT),
            LineAlignment.CENTER, width, 3, font
        ));
    }

    private DivisionLine newBlock(MetaData data, LineAlignment alignment,
            float width, ContentFont font) throws IOException{
        return newBlock(data, alignment, width, 1, font);
    }

    private DivisionLine newBlock(MetaData data, LineAlignment alignment,
            float width, float leading, ContentFont font) throws IOException{
        return newBlock(getData(data), alignment, width, leading, font);
    }

    private DivisionLine newBlock(String text, LineAlignment alignment,
            float width, float leading, ContentFont font) throws IOException{
        return new DivisionLine(width, alignment).setLeading(leading)
            .appendSimpleText(text, font);
    }

    private String getData(MetaData key){
        return exportData.getText(key);
    }

    @Override
    public void close() throws IOException{
        if (outputPage.isPresent()){
            outputPage.get().close();
        }
    }

}