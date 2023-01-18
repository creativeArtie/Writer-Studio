package com.creativeartie.writer.export;

public interface SetupDataSpace<T extends Number> extends RenderData<T>{

    public default OutputContentInfo<T> split(OutputContentInfo<T> info){
        String line = info.getFullText();
        int split = info.getLineSplit();
        String[] last = splitLine(line, split);
        String[] created = splitLine(last[1], split);
        split++;
        info.setStartText(created[0]);
        info.setEndText(created[1]);
        info.setLineSplit(split);
        return info;
    }

    public default OutputContentInfo<T> split(OutputContentInfo<T> info, T extra){
        String line = info.getFullText();
        int split = info.getLineSplit();
        String[] current = splitLine(line, split);

        line = current[1];
        if(line.length() != 0){;
            if (isFitWidth(line, info.getWidthSpace(), extra)){
                /// Fits all
                info.setStartText(line);
                info.setEndText("");
                return info;
            }
        } else {
            /// no text
            info.setStartText("");
            info.setEndText("");
            return info;
        }

        int where = 1;
        String[] last = current;
        current = splitLine(line, where);
        if (! isFitWidth(current[0], info.getWidthSpace(), extra)){
            /// None fit
            info.setStartText("");
            info.setEndText(last[1]);
            info.setLineSplit(split);
            return info;
        }
        int i = 0;
        while(current[0].length() > 0){
            if (! isFitWidth(current[0], info.getWidthSpace(), null)){
                /// Not fit middle
                info.setStartText(last[0]);
                info.setEndText(last[1]);
                info.setLineSplit(split - 1);
                return info;
            }
            last = current;
            current = splitLine(line, where);
            split++;
            where++;
            if (current[1].length() == 0){
                /// Not fit last
                info.setStartText(last[0]);
                info.setEndText(last[1]);
                info.setLineSplit(split - 1);
                return info;

            }
            assert i < info.getFullText().length();
            i++;
        }
        assert false: "Unreachable code.";

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

    public boolean isFitWidth(String text, T spaces, T extra);

}
