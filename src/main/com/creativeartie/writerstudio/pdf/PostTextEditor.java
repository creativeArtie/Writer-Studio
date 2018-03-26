package com.creativeartie.writerstudio.pdf;

import java.io.*;
import org.apache.pdfbox.pdmodel.*;

public interface PostTextEditor{
    public void edit(PDPage page, PDPageContentStream stream) throws IOException;
}