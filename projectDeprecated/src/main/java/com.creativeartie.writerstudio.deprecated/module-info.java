module com.creativeartie.writerstudio.deprecated {
    exports com.creativeartie.writerstudio.pdf;
    exports com.creativeartie.writerstudio.resource;

    requires com.creativeartie.writerstudio.lang;
    requires com.google.common;
    requires pdfbox;
    requires java.desktop;
    requires com.creativeartie.writerstudio.util;
}
