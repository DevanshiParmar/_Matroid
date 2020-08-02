package com.example.smartindiahackathon;

public class Graph_MonthlyAllData {
    String Month1,Avg1,Footfall1;


    public Graph_MonthlyAllData( String avg1, String footfall1,String month1) {
        Month1 = month1;
        Avg1 = avg1;
        Footfall1=footfall1;
    }

    public void setMonthAll(String month1) {
        Month1 = month1;
    }

    public void setAvgAll(String avg1) {
        Avg1 = avg1;
    }

    public void setFootfallAll(String footfall1) {
        Footfall1 = footfall1;
    }

    public String getMonthAll() {
        return Month1;
    }

    public String getAvgAll() {
        return Avg1;
    }

    public String getFootfallAll() {
        return Footfall1;
    }

}

