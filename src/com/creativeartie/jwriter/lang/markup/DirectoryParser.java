package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Create a Span for {@link CatalogueIdentity}.
 */
final class DirectoryParser implements SetupParser{
    /// Shows how to end a text
    private final ContentParser idContent;
    private final String[] reparseEnders;

    /// Adds a root category to differentiate footnote, links, etc
    private final DirectoryType idType;

    DirectoryParser(DirectoryType type, String ... enders){
        checkNotNull(enders, "enders");
        checkNotNull(type, "type");

        idType = type;

        /// adding DIRECTORY_CATEGORY into spanEnders by copying into new array
        String[] init = new String[enders.length + 1];
        System.arraycopy(enders, 0, init, 0, enders.length);
        init[enders.length] = DIRECTORY_CATEGORY;
        reparseEnders = enders;

        idContent = new ContentParser(StyleInfoLeaf.ID, init);
    }

    /** Check if the text can be parse at Directory level. */
    boolean canParse(String text){
        return BasicParseText.canParse(text, Arrays.asList(reparseEnders));
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        checkNotNull(pointer, "pointer");

        /// Setup for DirectorySpan
        ArrayList<Span> children = new ArrayList<>();

        /// Extract category & id
        boolean more;
        do {
            idContent.parse(children, pointer);

            /// last current is not an id but a category
            more = pointer.startsWith(children, DIRECTORY_CATEGORY);
        } while (more);

        /// Create span if there are Span extracted
        if (children.size() > 0) {
            DirectorySpan ans = new DirectorySpan(children, idType, this);
            return Optional.of(ans);
        }
        return Optional.empty();
    }
}
