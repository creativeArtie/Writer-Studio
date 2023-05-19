package com.creativeartie.humming.main;

import java.util.*;

import com.creativeartie.humming.ui.*;
import com.google.common.base.*;

/**
 * Interface texts used in the code.
 *
 * @author wai
 */
@SuppressWarnings("nls")
public class UIText {
    private interface GetText {
        default String getText() {
            String namespace = getClass().getSimpleName();
            String name = name();
            namespace = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, namespace);
            name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
            return getBundle().getString(Joiner.on('.').join(namespace, name));
        }

        /**
         * The name
         *
         * @return the enum name
         */
        String name();
    }

    /**
     * General text
     *
     * @author wai
     */
    public enum General implements GetText {
        /** Window Title */
        TITLE,
        /** Writing tab. */
        WRITER_TAB;
    }

    /**
     * Default names
     *
     * @author wai
     */
    public enum DefaultNames implements GetText {
        /** Default note file */
        NOTE,
        /** Default draft file */
        DRAFT;
    }

    private static ResourceBundle textBundle;

    /**
     * Get resource bundle
     *
     * @return the resource bundle
     *
     * @see App#start(javafx.stage.Stage)
     */
    public static ResourceBundle getBundle() {
        if (textBundle == null) {
            textBundle = ResourceBundle.getBundle("data.uiText");
        }
        return textBundle;
    }

    private UIText() {}
}
