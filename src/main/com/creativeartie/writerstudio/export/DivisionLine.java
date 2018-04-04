package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.apache.pdfbox.pdmodel.common.*;

import com.creativeartie.writerstudio.export.value.*;

public class DivisionLine implements Division{

    private float divWidth;

    public DivisionLine(float width){
        divWidth = width;
    }

    public float getHeight(){
        return 10f;
    }

    public float getStartY(){
        return 0;
    }

    public float getWidth(){
        return divWidth;
    }

    public List<ContentPostEditor> getPostTextConsumers(PDRectangle rect){

        return Arrays.asList(new ContentPostEditor[]{
            (page, stream) -> {
                float x = rect.getLowerLeftX();
                float y = rect.getUpperRightY() + (rect.getHeight() / 2);
                float x1 = rect.getUpperRightX() / 2;
                stream.moveTo(x, y);
                stream.lineTo(x1, y);
                stream.stroke();
                System.out.println(x + " " + y);
            }
        });
    }
}