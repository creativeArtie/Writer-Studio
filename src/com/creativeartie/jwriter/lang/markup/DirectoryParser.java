package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.main.*;

/**
 * Create a Span for {@link CatalogueIdentity}.
 */
final class DirectoryParser implements SetupParser{
    /// Shows how to end a text
    private final ContentParser idContent;
    
    /// Adds a root category to differentiate footnote, links, etc
    private final Optional<DirectoryType> idType;
        
    DirectoryParser(DirectoryType type, String ... enders){
        idType = Optional.ofNullable(type);
        Checker.checkNotNull(enders, "spanEnders");
        
        /// adding DIRECTORY_CATEGORY into spanEnders by copying into new array
        String[] init = new String[enders.length + 1];
        System.arraycopy(enders, 0, init, 0, enders.length);
        init[enders.length] = DIRECTORY_CATEGORY;
        idContent = new ContentParser(SetupLeafStyle.ID, init);
    }
    
    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
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
            DirectorySpan ans = new DirectorySpan(children, idType);
            return Optional.of(ans);
        }
        return Optional.empty();
    }
}
