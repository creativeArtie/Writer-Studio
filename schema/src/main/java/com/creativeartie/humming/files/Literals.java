package com.creativeartie.humming.files;

/**
 * List of literal text
 */
@SuppressWarnings("nls")
public enum Literals {
    /**
     * Id name for agenda span
     *
     * @see com.creativeartie.humming.document.IdentityTodo#getCategories()
     */
    TODO_PHRASE_ID("phrase"),

    /**
     * Id name for agenda line
     *
     * @see com.creativeartie.humming.document.ParaAgenda#getCategories()
     */
    TODO_LINE_ID("line"),

    /**
     * Log file name
     *
     * @see ProjectZip#save()
     */
    LOG_FILE("log.obj"),

    /**
     * Project properties file name
     *
     * @see ProjectZip#save()
     */
    PROP_FILE("info.properties"),

    /**
     * Project properties file name
     *
     * @see ProjectZip#save()
     */
    IMAGES_FILE("images.properties"),
    /**
     * Folder for documents.
     *
     * @see ProjectZip#save()
     */
    DOC_FOLDER("doc"),
    /**
     * Document extension for documents.
     *
     * @see ProjectZip#save()
     */
    DOC_EXT(".txt"),

    /**
     * Folder for documents.
     *
     * @see ProjectZip#save()
     */
    IMAGE_FOLDER("images"),
    /**
     * Text escape pattern.
     *
     * @see com.creativeartie.humming.schema.TextSpanPatterns
     */
    TEXTPAT_ESCAPE("\\\\"),
    /**
     * Text format pattern.
     *
     * @see com.creativeartie.humming.schema.TextSpanPatterns
     */
    TEXTPAT_FORMATS("\\*`\\_") {
        @Override
        public String getText() {
            return TEXTPAT_ESCAPE.getText() + this.literalText;
        }
    },
    /**
     * Text reference start pattern.
     *
     * @see com.creativeartie.humming.schema.TextSpanPatterns
     */
    TEXTPAT_REF("\\{") {
        @Override
        public String getText() {
            return TEXTPAT_FORMATS.getText() + this.literalText;
        }
    };

    final String literalText;

    private Literals(String text) {
        literalText = text;
    }

    /**
     * Get the literal text
     *
     * @return literal text
     */
    public String getText() {
        return literalText;
    }
}
