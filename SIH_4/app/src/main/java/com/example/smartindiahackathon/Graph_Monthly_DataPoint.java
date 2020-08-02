package com.example.smartindiahackathon;

public class Graph_Monthly_DataPoint {
    String avg_dwell,month,footfall;


    public Graph_Monthly_DataPoint(String avg_dp, String footfall_dp, String month_dp) {
        avg_dwell = avg_dp;
        footfall=footfall_dp;
        month = month_dp;

    }

    public Graph_Monthly_DataPoint(){

    }

    public String getAvg_dwell() {
        return avg_dwell;
    }

    public String getFootfall() {
        return footfall;
    }

    public String getMonth() { return month; }
}
