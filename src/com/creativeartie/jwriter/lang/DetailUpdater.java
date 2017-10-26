package com.creativeartie.jwriter.lang;

import java.util.*;

// import static com.creativeartie.jwriter.lang.DetailUpdateType.*;
import com.creativeartie.jwriter.main.*;

/* // TODO Speed up preformance by edit only some of the text
public final class DetailUpdater{
    
    public static DetailUpdater replace(SetupParser ... parsers){
        return new DetailUpdater(REPLACE, parsers);
    }
    
    public static DetailUpdater mergeBoth(SetupParser ... parsers){
        return new DetailUpdater(MERGE_BOTH, parsers);
    }
    
    public static DetailUpdater mergeLast(SetupParser ... parsers){
        return new DetailUpdater(MERGE_LAST, parsers);
    }
    
    public static DetailUpdater mergeNext(SetupParser ... parsers){
        return new DetailUpdater(MERGE_NEXT, parsers);
    }
    
    public static DetailUpdater unable(){
        return new DetailUpdater(UNABLE, null);
    }
    
    private final Optional<SetupParser[]> nodePasers;
    private final DetailUpdateType updatePlan;
    
    private DetailUpdater(DetailUpdateType update, 
        SetupParser[] parsers
    ){
        nodePasers = Optional.ofNullable(parsers);
        updatePlan = Checker.checkNotNull(update, "update");
    }
    
    DetailUpdateType getPlan(){
        return updatePlan;
    }
    
    List<Span> parse(String raw, Document root){
        Checker.checkNotNull(raw, "raw");
        Checker.checkNotNull(root, "root");
        SetupPointer pointer = SetupPointer.updatePointer(raw, root);
        ArrayList<Span> ans = new ArrayList<>();
        while(pointer.hasNext()){
            for(SetupParser parser: nodePasers.get()){
                if(parser.parse(ans, pointer)){
                    break;
                }
            }
        }
        return ans;
    }
}
*/