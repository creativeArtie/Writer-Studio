package com.creativeartie.writer.writing;

import java.util.regex.*;

public class ParseDocument {
	private static String id_text = 
			"[\\p{IsIdeographic}\\p{IsAlphabetic}\\p{IsDigit}]";
	private static Pattern SYNTAX = Pattern.compile("");
	
	protected static enum Phrases{
		TOKEN(id_text + "(( |_)*" + id_text + ")*"),
        ID(TOKEN.format_syntax+"(-"+TOKEN.format_syntax+")*");
		
		protected final String format_syntax;
		
		Phrases(String format) {
			format_syntax = "(?<" + name() + ">" + format + ")";
		}
	}
	private ParseDocument() {}

}
