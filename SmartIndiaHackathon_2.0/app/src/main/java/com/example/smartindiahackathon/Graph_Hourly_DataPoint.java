package com.example.smartindiahackathon;

public class Graph_Hourly_DataPoint {
    String avg_dwell,hour,footfall;


    public Graph_Hourly_DataPoint(String avg_dp, String footfall_dp, String hour_dp) {
        avg_dwell = avg_dp;
        footfall=footfall_dp;
        hour = hour_dp;

    }

    public Graph_Hourly_DataPoint(){

    }

    public String getAvg_dwell() {
        return avg_dwell;
    }

    public String getFootfall() {
        return footfall;
    }

    public String getHour() { return hour; }
}
