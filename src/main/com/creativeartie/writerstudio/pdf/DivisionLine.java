package com.creativeartie.writerstudio.pdf;

import java.util.*; // List, Arrays

import org.apache.pdfbox.pdmodel.common.*; // PDRectangle

import com.creativeartie.writerstudio.pdf.value.*; // ContentPostEditor

/** A {@link Division} for line drawing.
 */
final class DivisionLine implements Division{

    private final float divWidth;

    /** Only constructor
     * @param width
     *      the width of the line
     */
    DivisionLine(float width){
        divWidth = width;
    }

    @Override
    public float getStartY(){
        return 0;
    }

    @Override
    public float getHeight(){
        return 20f;
    }


    @Override
    public float getWidth(){
        return divWidth;
    }

    @Override
    public List<ContentPostEditor> getPostTextConsumers(PDRectangle rect){
        /// Draws a line
        return Arrays.asList(new ContentPostEditor[]{ (page, stream) -> {
            float x = rect.getLowerLeftX();
            float y = rect.getUpperRightY() - (rect.getHeight() / 2);
            float x1 = rect.getUpperRightX() / 2;
            stream.moveTo(x, y);
            stream.lineTo(x1, y);
            stream.stroke();
        }});
    }
}