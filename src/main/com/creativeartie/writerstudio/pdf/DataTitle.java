package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;
import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.lang.markup.*;

import com.creativeartie.writerstudio.pdf.value.*;

public final class DataTitle extends Data{

    public DataTitle(Data data){
        super(data);
    }


    public ArrayList<FormatterItem> getTitleTopText(StreamData data)
             throws IOException{
        SizedFont font = data.getBaseFont();
        ArrayList<FormatterItem> ans = new ArrayList<>();
        float width = data.getWidth();
        ans.add(newBlock(MetaData.AGENT_NAME, TextAlignment.LEFT, width, font));
        ans.add(newBlock(MetaData.AGENT_ADDRESS, TextAlignment.LEFT, width, font));
        ans.add(newBlock(MetaData.AGENT_EMAIL, TextAlignment.LEFT, width, font));
        ans.add(newBlock(MetaData.AGENT_PHONE, TextAlignment.LEFT, width, font));
        return ans;
    }

    public ArrayList<FormatterItem> getTitleCenterText(StreamData data)
             throws IOException{
        SizedFont font = data.getBaseFont();
        ArrayList<FormatterItem> ans = new ArrayList<>();
        float width = data.getWidth();
        ans.add(newBlock(MetaData.TITLE, TextAlignment.CENTER, width, 2, font));
        ans.add(newBlock("By", TextAlignment.CENTER, width, 2, font));
        ans.add(newBlock(MetaData.AUTHOR, TextAlignment.CENTER, width, 2, font));
        return ans;
    }

    public ArrayList<FormatterItem> getTitleBottomText(StreamData data)
             throws IOException{
        SizedFont font = data.getBaseFont();
        ArrayList<FormatterItem> ans = new ArrayList<>();
        float width = data.getWidth();
        ans.add(newBlock(MetaData.AUTHOR, TextAlignment.RIGHT, width, font));
        ans.add(newBlock(MetaData.ADDRESS, TextAlignment.RIGHT, width, font));
        ans.add(newBlock(MetaData.PHONE, TextAlignment.RIGHT, width, font));
        ans.add(newBlock(MetaData.EMAIL, TextAlignment.RIGHT, width, font));
        ans.add(newBlock(MetaData.WEBSITE,TextAlignment.RIGHT, width, font));
        ans.add(newBlock(
            getData(MetaData.AUTHOR) + " © " + getData(MetaData.COPYRIGHT),
            TextAlignment.CENTER, width, 3, font
        ));
        return ans;
    }

    private FormatterItem newBlock(MetaData data, TextAlignment alignment,
            float width, SizedFont font) throws IOException{
        return newBlock(data, alignment, width, 1, font);
    }

    private FormatterItem newBlock(MetaData data, TextAlignment alignment,
            float width, float leading, SizedFont font) throws IOException{
        return newBlock(getData(data), alignment, width, leading, font);
    }

    private FormatterItem newBlock(String text, TextAlignment alignment,
            float width, float leading, SizedFont font) throws IOException{
        return new FormatterItem(width, alignment).setLeading(leading)
            .appendSimpleText(text, font);
    }
}