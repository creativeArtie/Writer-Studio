package com.creativeartie.jwriter.export;

import java.io.*;

import com.creativeartie.jwriter.file.*;

interface ExportLambda{
    public void exportFile(ManuscriptFile input, File output) throws
        FileNotFoundException;
}