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
    float getYLocation(){
        float leading = 0;
        if (! isEmpty()){
            FormatterItem child = get(0);
            if (! child.isEmpty()){
                leading = child.get(0).getTextHeight() * (child.getLeading() - 1);
            }
        }
        return getStartY() - leading;
    }
    abstract float getStartY();
    abstract float getWidth();
    abstract float getHeight();
}