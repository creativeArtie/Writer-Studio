package com.creativeartie.writerstudio.export;

import java.util.*;
import java.util.function.*;

public interface BridgeContent{

    public boolean isBold();

    public boolean isItalics();

    public boolean isUnderline();

    public boolean isCoded();

    public boolean isSuperscript();

    public String getText();

    public Optional<BridgeDivision> getNote();

    public Optional<String> getLink();

    public boolean isEquals(BridgeContent content);
}
