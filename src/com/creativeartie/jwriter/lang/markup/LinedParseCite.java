package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;

enum LinedParseCite implements SetupParser {
    INSTANCE;

    @Override
    public Optional<SpanBranch> parse(SetupPointer pointer){
        ArrayList<Span> children = new ArrayList<>();
        if (pointer.startsWith(children, LINED_CITE)){
            Optional<SetupParser> used = Optional.empty();
            for (InfoFieldParser parser: InfoFieldParser.getParsers()){
                if (parser.parse(children, pointer)){
                    used = parser.getDataParser();
                    break;
                }
            }
            pointer.trimStartsWith(children, LINED_DATA);

            if (! (used.isPresent() && used.get().parse(children,
                pointer)))
            {
                pointer.getTo(children, StyleInfoLeaf.TEXT, LINED_END);
            }

            pointer.startsWith(children, LINED_END);
            return Optional.of(new LinedSpanCite(children));
        }
        return Optional.empty();
    }
}
