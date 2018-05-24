package com.creativeartie.writerstudio.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.value.*;

public class DefaultFont extends ContentFont<PDFont>{

    public DefaultFont(){}

    private DefaultFont(ContentFont<PDFont> font, Key key, Object value){
        super(font, key, value);
    }

    @Override
    protected PDFont buildFont(boolean mono, boolean bold,
            boolean italics, boolean superscript) {
        return PDType1Font.TIMES_ROMAN;
    }

    @Override
    public float getWidth(String text){
        /// From https://stackoverflow.com/questions/13701017/calculation-
        /// string-width-in-pdfbox-seems-only-to-count-characters
        try {
            return getFont().getStringWidth(text) / 1000 * getSize();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public float getHeight(){
        PDFontDescriptor descipter = getFont().getFontDescriptor();
        return (descipter.getCapHeight() + descipter.getXHeight()) / 1000 *
            getSize();
    }

    @Override
    public DefaultFont produce(ContentFont<PDFont> font, Key key, Object value){
        return new DefaultFont(font, key, value);
    }
}
