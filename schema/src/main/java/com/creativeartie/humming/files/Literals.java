package com.creativeartie.humming.files;

/**
 * List of literal text
 */
@SuppressWarnings("nls")
public enum Literals {
    /**
     * literal for {@link com.creativeartie.humming.document.IdentityTodo}
     */
    TODO_PHRASE_ID("phrase"),

    /**
     * literal for {@link com.creativeartie.humming.document.ParaAgenda}
     */
    TODO_LINE_ID("line"),

    /**
     * Log file name for {@link ProjectZip}
     */
    LOG_FILE("log.obj"),

    /**
     * Project properties file name for {@link ProjectZip}
     */
    PROP_FILE("info.properties");

    private final String literalText;

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
