package com.example.chenx.musc.model;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class User extends LitePalSupport {
    private int id;

    @Column(unique = true,nullable = false)
    private  String Email;

    @Column(nullable = false)
    private  String Name;

    @Column(nullable = false)
    private  String password;

    private List<Record> records;

    public List<Record> getRecords() {
        return LitePal.where("user_id = ?",String.valueOf(getBaseObjId())).find(Record.class);
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
