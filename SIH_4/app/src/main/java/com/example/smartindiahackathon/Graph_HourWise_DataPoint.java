package com.example.smartindiahackathon;

public class Graph_HourWise_DataPoint {
    String entry_time,exit_time;


    public Graph_HourWise_DataPoint(String Entry_time, String Exit_time) {
        entry_time = Entry_time;
        exit_time = Exit_time;
    }
    public Graph_HourWise_DataPoint(){
    }

    public String getEntry_time() {
        return entry_time;
    }

    public String getExit_time() {
        return exit_time;
    }
}
