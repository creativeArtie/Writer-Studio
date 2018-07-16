package com.creativeartie.writerstudio.pdf;

import org.junit.jupiter.api.extension.*;

import com.creativeartie.writerstudio.pdf.value.*;

public class PdfFontParameterResolver implements ParameterResolver{

    @Override
    public boolean supportsParameter​(ParameterContext parameter,
            ExtensionContext extension) throws ParameterResolutionException{
        return parameter.getParameter().getType() == WritingExporter.PdfFont.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameter,
            ExtensionContext extension) throws ParameterResolutionException {
        try {
            WritingExporter build = new WritingExporter("build/outputs/tmp.pdf");
            return build.new PdfFont();
        } catch (Exception ex){
            throw new ParameterResolutionException("WritingExporter failed.",
                ex);
        }
    }
}
