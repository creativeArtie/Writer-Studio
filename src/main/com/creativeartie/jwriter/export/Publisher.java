package com.creativeartie.jwriter.export;

import java.io.*;
import com.creativeartie.jwriter.lang.markup.*;

public interface Publisher extends Closeable{

    public void addParagraph(FormatSpanMain format);

    public void addHeading(FormatSpanMain format);

    public void addSecionBreak();
}