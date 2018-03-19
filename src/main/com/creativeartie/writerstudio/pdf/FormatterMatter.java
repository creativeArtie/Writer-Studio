package com.creativeartie.writerstudio.pdf;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.pdf.value.*;

/**
 * Defines the placement of the text on the page.
 */
abstract class FormatterMatter extends ForwardingList<FormatterItem>{

    abstract float getXLocation();
    abstract float getYLocation();
    abstract float getWidth();
    abstract float getHeight();
}