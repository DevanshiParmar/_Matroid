package com.example.smartindiahackathon;

public class Graph_Daily_DataPoint {
    String avg_dwell,date,footfall;


    public Graph_Daily_DataPoint(String avg_dp, String footfall_dp, String month_dp) {
        avg_dwell = avg_dp;
        date = month_dp;
        footfall=footfall_dp;
    }

    public Graph_Daily_DataPoint(){

    }

    public String getAvg_dwell() {
        return avg_dwell;
    }

    public String getFootfall() {
        return footfall;
    }

    public String getDate() { return date; }
}

