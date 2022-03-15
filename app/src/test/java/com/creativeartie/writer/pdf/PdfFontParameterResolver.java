package com.creativeartie.writer.pdf;

import org.junit.jupiter.api.extension.*;

public class PdfFontParameterResolver implements ParameterResolver{

    @Override
    public Object resolveParameter(ParameterContext parameter,
            ExtensionContext extension) throws ParameterResolutionException {
        try (WritingExporter build = new WritingExporter("build/outputs/tmp.pdf")) {
				return build.new PdfFont();
        } catch (Exception ex){
            throw new ParameterResolutionException("WritingExporter failed.",
                ex);
        }
    }

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
			throws ParameterResolutionException {
		return parameterContext.getParameter().getType() == WritingExporter.PdfFont.class;
	}
}
