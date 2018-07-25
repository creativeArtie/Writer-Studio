package com.creativeartie.writerstudio.export.pdf;

import java.util.function.*;

import org.apache.pdfbox.pdmodel.*;

import com.creativeartie.writerstudio.export.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
public class RenderPdf implements FactoryRender<Float>{

    private String outputFile;
    private PDDocument pdfDocument;

    public RenderPdf(String file){
        pdfDocument = new PDDocument();
    }

    @Override
    public Float getZero(){
        return 0f;
    }

    @Override
    public RenderContent<Float> getRenderContent(){
        return null;
    }

    @Override
    public RenderDivision<Float> getRenderDivision(){
        return null;
    }
}
