package com.creativeartie.jwriter.lang.markup;

import java.util.*;
import java.util.Optional;

import com.google.common.base.*;

import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import com.creativeartie.jwriter.lang.*;

/**
 * Parser for {@link InfoFieldSpan}.
 */
final class InfoFieldParser implements SetupParser{

    private final InfoFieldType fieldType;

    private static Optional<InfoFieldParser[]> baseParsers = Optional.empty();

    public static InfoFieldParser[] getParsers(){
        if (! baseParsers.isPresent()){
            InfoFieldType[] types = InfoFieldType.values();
            InfoFieldParser[] create = new InfoFieldParser[types.length];
            for(int i = 0; i < create.length; i++){
                create[i] = new InfoFieldParser(types[i]);
            }
            baseParsers = Optional.of(create);
        }
        return baseParsers.get();
    }

    private InfoFieldParser(InfoFieldType type){
        fieldType = type;
    }

    Optional<SetupParser> getDataParser(){
        return fieldType.getDataParser();
    }

    @Override
    public Optional<SpanBranch> parse(SetupPointer childPointer){
        ArrayList<Span> children = new ArrayList<>();
        String field = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN,
            fieldType.name());

        if(childPointer.trimStartsWith(children, StyleInfoLeaf.FIELD, field)){
            return Optional.of(new InfoFieldSpan(children));
        }

        if (fieldType == InfoFieldType.ERROR){
            if (childPointer.getTo(children, StyleInfoLeaf.FIELD, LINED_DATA,
                LINED_END))
            {
                return Optional.of(new InfoFieldSpan(children));
            }
        }

        return Optional.empty();
    }
}
