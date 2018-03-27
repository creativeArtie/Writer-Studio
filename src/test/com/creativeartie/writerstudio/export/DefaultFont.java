package com.creativeartie.writerstudio.export;

import java.io.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.export.value.*;

public class DefaultFont extends ContentFont{

    public DefaultFont(){
        super((m, b, i, s) -> PDType1Font.TIMES_ROMAN);
    }

    private DefaultFont(ContentFont font, Key key, Object value){
        super(font, key, value);
    }

    @Override
    public float getWidth(String text) throws IOException{
        /// From https://stackoverflow.com/questions/13701017/calculation-
        /// string-width-in-pdfbox-seems-only-to-count-characters
        return getFont().getStringWidth(text) / 1000 * getSize();
    }

    @Override
    public float getHeight(){
        PDFontDescriptor descipter = getFont().getFontDescriptor();
        return (descipter.getCapHeight() + descipter.getXHeight()) / 1000 *
            getSize();
    }

    @Override
    public ContentFont produce(ContentFont font, Key key, Object value){
        return new DefaultFont(font, key, value);
    }
}