package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.*;

import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;

public class SectionTitle {

    private PDDocument pdfDocument;
    private ManuscriptFile exportData;

    SectionTitle(){}

    public void export(PDDocument doc, ManuscriptFile data){
        pdfDocument = doc;
        exportData = data;
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

    private void writeTitlePage(){
    }

}