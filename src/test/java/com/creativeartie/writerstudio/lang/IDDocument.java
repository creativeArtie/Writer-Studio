package com.creativeartie.writerstudio.lang;

import com.creativeartie.writerstudio.lang.markup.*;

class IDDocument{
	
	private StringBuilder rawText;
	
	IDDocument(){
		rawText = new StringBuilder();
	}

	IDDocument addID(String id, String ... categories){
		rawText.append("!^").addId(id, categories).append("\n");
		return this;
	}
	
	IDDocument addID(String id, String ... categories){
		rawText.append("{^").addId(id, categories).append("}\n");
		return this;
	}
	
	private StringBuilder addId(String id, String ... categories){
		boolean first = true;
		for (String category: categories){
			if (first){
				first = false;
			} else {
				rawText.append("-");
			}
			rawText.append(category);
		}
		return rawText.append((first? "-": "")).append(id)
	}
	
	Document builder(){
		return new WritingText(rawText);
	}
}
