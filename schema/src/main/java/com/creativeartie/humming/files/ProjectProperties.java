package com.creativeartie.humming.files;

import java.io.*;
import java.util.*;

/**
 * List of project properties
 */
public class ProjectProperties implements Serializable {
    private static final long serialVersionUID = -6786244514727898783L;
    private Properties projectProp;

    ProjectProperties() {
        projectProp = new Properties();
    }

    void save(OutputStream output) throws IOException {
        projectProp.store(output, new String());
    }

    void load(InputStream input) throws IOException {
        projectProp.load(input);
    }
}
