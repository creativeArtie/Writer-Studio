package com.creativeartie.jwriter.pdf;

import java.io.*;
import java.util.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.resource.*;
import com.creativeartie.jwriter.lang.markup.*;

import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.jwriter.pdf.value.*;

public final class InputTitle implements Input{
    private InputWriting baseData;
    private ManuscriptFile outputDoc;

    public InputTitle(InputWriting data){
        baseData = data;
        outputDoc = data.getOutputDoc();
    }

    public InputWriting getBaseData(){
        return baseData;
    }

    public ArrayList<PdfItem> getTitleTopText(StreamData data)
             throws IOException{
        ArrayList<PdfItem> ans = new ArrayList<>();
        float width = data.getWidth();
        ans.add(newBlock(MetaData.AGENT_NAME, TextAlignment.LEFT, width));
        ans.add(newBlock(MetaData.AGENT_ADDRESS, TextAlignment.LEFT, width));
        ans.add(newBlock(MetaData.AGENT_EMAIL, TextAlignment.LEFT, width));
        ans.add(newBlock(MetaData.AGENT_PHONE, TextAlignment.LEFT, width));
        return ans;
    }

    public ArrayList<PdfItem> getTitleCenterText(StreamData data)
             throws IOException{
        ArrayList<PdfItem> ans = new ArrayList<>();
        float width = data.getWidth();
        ans.add(newBlock(MetaData.TITLE, TextAlignment.CENTER, width, 2));
        ans.add(newBlock("By", TextAlignment.CENTER, width, 2));
        ans.add(newBlock(MetaData.AUTHOR, TextAlignment.CENTER, width, 2));
        return ans;
    }

    public ArrayList<PdfItem> getTitleBottomText(StreamData data)
             throws IOException{
        ArrayList<PdfItem> ans = new ArrayList<>();
        float width = data.getWidth();
        ans.add(newBlock(MetaData.AUTHOR, TextAlignment.RIGHT, width));
        ans.add(newBlock(MetaData.ADDRESS, TextAlignment.RIGHT, width));
        ans.add(newBlock(MetaData.PHONE, TextAlignment.RIGHT, width));
        ans.add(newBlock(MetaData.EMAIL, TextAlignment.RIGHT, width));
        ans.add(newBlock(MetaData.WEBSITE,TextAlignment.RIGHT, width));
        ans.add(newBlock(
            outputDoc.getText(MetaData.AUTHOR) + " © " + outputDoc.getText(
                MetaData.COPYRIGHT),
            TextAlignment.CENTER, width, 3
        ));
        return ans;
    }

    private PdfItem newBlock(MetaData data, TextAlignment alignment,
            float width) throws IOException{
        return newBlock(data, alignment, width, 1);
    }

    private PdfItem newBlock(MetaData data, TextAlignment alignment,
            float width, float leading) throws IOException{
        return newBlock(outputDoc.getText(data), alignment, width, leading);
    }

    private PdfItem newBlock(String text, TextAlignment alignment,
            float width, float leading) throws IOException{
        return new PdfItem(width, alignment).setLeading(leading)
            .appendText(text, getBaseFont());
    }
}