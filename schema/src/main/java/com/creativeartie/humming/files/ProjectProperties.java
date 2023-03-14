package com.creativeartie.humming.files;

import java.io.*;
import java.util.*;

public class ProjectProperties implements Serializable {
    private static final long serialVersionUID = -6786244514727898783L;
    // private Properties projectProp;
    private ArrayList<String> savePaths;

    public ProjectProperties() {
        // projectProp = new Properties();
        savePaths = new ArrayList<>();
    }
}
