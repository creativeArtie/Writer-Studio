package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.params.provider.*;
import static com.creativeartie.writerstudio.lang.CatalogueStatus.*;

import java.util.stream.*;

public abstract class IDParameterMethodSource {
	
	private Arguments buildArgument(CatalogueStatus status, 
			boolean ... ids){
		StringBuilder builder = new StringBuilder();
		for (boolean id: ids){
			builder.append(id? getIdText(), getRefText());
		}
		return Arguments.of(UNUSED, builder.build, ids);
	}

    public static Stream<Arguments> provideText(IDParameterMethodSource source){
        String id = source.getIdText();
        String ref = source.getRefText();
        return Stream.of(
            buildArgument(UNUSED,    id),
            buildArgument(MULTIPLE,  id  + id),
            buildArgument(MULTIPLE,  id  + id  + id),
            buildArgument(MULTIPLE,  id  + id  + id + id),
            buildArgument(MULTIPLE,  id  + id  + id + ref),
            buildArgument(MULTIPLE,  id  + id  + ref),
            buildArgument(MULTIPLE,  id  + id  + ref + id),
            buildArgument(MULTIPLE,  id  + id  + ref + ref),
            buildArgument(READY,     id  + ref),
            buildArgument(MULTIPLE,  id  + ref + id),
            buildArgument(MULTIPLE,  id  + ref + id + id),
            buildArgument(MULTIPLE,  id  + ref + id + ref),
            buildArgument(READY,     id  + ref + ref),
            buildArgument(MULTIPLE,  id  + ref + ref + id),
            buildArgument(READY,     id  + ref + ref + ref),
            buildArgument(NOT_FOUND, ref),
            buildArgument(READY,     ref + id),
            buildArgument(MULTIPLE,  ref + id  + id),
            buildArgument(MULTIPLE,  ref + id  + id + id),
            buildArgument(MULTIPLE,  ref + id  + id + ref),
            buildArgument(READY,     ref + id  + ref),
            buildArgument(MULTIPLE,  ref + id  + ref + id),
            buildArgument(READY,     ref + id  + ref + ref),
            buildArgument(NOT_FOUND, ref + ref),
            buildArgument(READY,     ref + ref + id),
            buildArgument(MULTIPLE,  ref + ref + id + id),
            buildArgument(READY,     ref + ref + id + ref),
            buildArgument(NOT_FOUND, ref + ref + ref),
            buildArgument(READY,     ref + ref + ref + id),
            buildArgument(NOT_FOUND, ref + ref + ref + ref)
        );
    }

    protected IDParameterMethodSource(){}

    protected abstract String getIdText();
    protected abstract String getRefText();
}
