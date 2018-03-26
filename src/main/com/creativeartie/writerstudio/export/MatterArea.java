package com.creativeartie.writerstudio.export;

import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

public class MatterArea implements AutoCloseable{
    private PDPageContentStream contentStream;

    MatterArea(PDPageContentStream content){
        contentStream = content;
    }

    MatterArea start(float x, float y, PDFont font, float size){
        contentStream.beginText();
        moveText(renderMatter.getXLocation(), renderMatter.getYLocation());
        return this;
    }

    public MatterArea writeData(DivisionLine text){
        return this;
    }

    @Override
    public void close() throws IOException{
        contentStream.endText();
    }

    private void moveText(float x, float y) throws IOException{
        localX += x;
        localY += y;
        contentStream.newLineAtOffset(x, y);
    }
}