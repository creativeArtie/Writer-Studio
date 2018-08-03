package com.creativeartie.writerstudio.export;

public interface SetupDataSpace<T extends Number> extends RenderData<T>{

    public default OutputContentInfo<T> split(OutputContentInfo<T> info){
        String line = info.getFullText();
        int split = info.getLineSplit();
        String[] current = splitLine(line, split);

        line = current[1];
        if(line.length() != 0){
            if (isFitWidth(line, info.getWidthSpace())){
                info.setStartText(line);
                info.setEndText("");
                return info;
            }
        }
        return info;
    }

    public default String[] splitLine(String text, int where){
        int cur = 0;
        String start = "";
        String end = text;

        for(int i = 0; i < where; i++){
            cur = text.indexOf(' ', cur);
            if (cur == -1) {
                return new String[]{text, ""};
            }
            cur++;
            start = text.substring(0, cur);
            end = text.substring(cur);
        }

        return new String[]{start, end};
    }

    public boolean isFitWidth(String text, T spaces);
}
