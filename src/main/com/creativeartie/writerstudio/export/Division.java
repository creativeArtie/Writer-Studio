package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import org.apache.pdfbox.pdmodel.common.*;

import com.creativeartie.writerstudio.export.value.*;

public interface Division{
    public float getHeight();

    public float getWidth();

    public float getStartY();

    public List<ContentPostEditor> getPostTextConsumers(PDRectangle rect);
}