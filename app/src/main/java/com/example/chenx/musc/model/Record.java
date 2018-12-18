package com.example.chenx.musc.model;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

public class Record extends LitePalSupport {

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Action action;

    @Column(nullable = false)
    private int times;

    @Column(nullable = false)
    private float woWeight;

    @Column(nullable = false)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public Action getAction() {
        //Action action=LitePal.findBySQL("select action.* from action,record where action.id=record.action_id and action.id>?","0");
//        return LitePal.where("action_id = ", String.valueOf(getBaseObjId())).find(Action.class).get(0);
        return action;
    }

    public int getTimes() {
        return times;
    }

    public float getWoWeight() {
        return woWeight;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setWoWeight(float woWeight) {
        this.woWeight = woWeight;
    }
}
