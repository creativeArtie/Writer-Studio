package com.creativeartie.writerstudio.resource;

public enum MetaData {
    AGENT_NAME("MetaData.AgentName"), AGENT_ADDRESS("MetaData.AgentAddress"),
    AGENT_EMAIL("MetaData.AgentEmail"), AGENT_PHONE("MetaData.AgentPhone"),
    TITLE("MetaData.ManuscriptTitle"), PEN_NAME("MetaData.PenName"),
    FIRST_NAME("MetaData.AuthorFirst"), LAST_NAME("MetaData.AuthorLast"),
    ADDRESS("MetaData.AuthorAddress"), EMAIL("MetaData.AuthorEmail"),
    PHONE("MetaData.AuthorPhone"), WEBSITE("MetaData.AuthorWebsite"),
    COPYRIGHT("MetaData.CopyrightYear"), KEY_WORD("MetaData.TitleKeyWord"),
    PAGE("MetaData.WordPage"), BY("MetaData.ByWord"),
    AUTHOR("MetaData.AuthorName");

    private final String dataKey;

    private MetaData(String key){
        dataKey = key;
    }

    public String getKey(){
        return dataKey;
    }
}
