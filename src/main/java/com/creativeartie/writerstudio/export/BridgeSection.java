package com.creativeartie.writerstudio.export;

import java.util.*;

public interface BridgeSection{

    public BridgeMatter getHeader();

    public BridgeMatter getFooter();

    public BridgeMatter getContent();
}
