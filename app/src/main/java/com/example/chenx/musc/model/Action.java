package com.example.chenx.musc.model;


import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Action extends LitePalSupport {
   @Column(unique = true,defaultValue = "unknown workout")
    private String name;

    @Column(nullable = false)
    private String group;

    @Column(ignore = true)
    private String tinyUrl;

    @Column(ignore = true)
    private String url;

    private List<Record> records=new ArrayList<Record>();


    public Action(String name)
    {
        this.name=name;
    }

    public Action( )
    {

    }

    public String getGroup() {
        return group;
    }

    public String getName()
    {
        return name;
    }

    public String getTinyUrl()
    {
        return tinyUrl;
    }

    public String getUrl()
    {
        return url;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setTinyUrl(String tinyUrl)
    {
        this.tinyUrl=tinyUrl;
    }

    public void setUrl(String url)
    {
        this.url=url;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public List<Record> getRecords() {
        return LitePal.where("action_id = ?",String.valueOf(getBaseObjId())).find(Record.class);
    }


}
