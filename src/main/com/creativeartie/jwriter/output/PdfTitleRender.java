package com.creativeartie.jwriter.output;

import java.util.*;

import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;

class PdfTitleRender extends PdfPageRender{

    PdfTitleRender(OutputInfo info, PdfFileOutput file){
        super(info, file);
        addNewPage();
    }

    void render(){
        Div div = new Div();
        for (String line: getInfo().getTitleTopText()){
            div.add(new Paragraph(line)
                .setMargin(0f).setPadding(0f)
            );
        }
        addTop(div);

        div = new Div();
        for (String line: getInfo().getTitleCenterText()){
            div.add(new Paragraph(line)
                .setMargin(0f).setPadding(0f)
                .setMultipliedLeading(2.0f)
                .setTextAlignment(TextAlignment.CENTER)
            );
        }
        addCentre(div);


        div = new Div();
        ArrayList<String> lines = getInfo().getTitleBottomText();
        for (int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            if (i < lines.size() - 1){
                div.add(new Paragraph(line)
                    .setMargin(0f).setPadding(0f)
                    .setTextAlignment(TextAlignment.RIGHT)
                );
            } else {
                /// Copy right text
                div.add(new Paragraph(line)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(40f)
                );
            }
        }
        addBottom(div);
    }
}