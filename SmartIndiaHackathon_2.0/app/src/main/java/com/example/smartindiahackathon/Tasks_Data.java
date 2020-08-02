package com.example.smartindiahackathon;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class Tasks_Data {
    public String publisher;
    public String title;
    public String desc;
    public List<String> membersId;

    public Tasks_Data() {
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getMembersId() {
        return membersId;
    }

    public void setMembersId(List<String> membersId) {
        this.membersId = membersId;
    }
}
