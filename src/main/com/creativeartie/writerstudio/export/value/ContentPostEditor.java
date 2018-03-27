package com.creativeartie.writerstudio.export.value;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;

public interface ContentPostEditor{
    public void edit(PDPage page, PDPageContentStream stream) throws IOException;
}