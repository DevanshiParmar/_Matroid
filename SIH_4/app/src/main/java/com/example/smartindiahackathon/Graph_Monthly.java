package com.example.smartindiahackathon;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Graph_Monthly extends AppCompatActivity {

    Switch avg_switch, footfall_switch;
    BarChart chart1;
    Button pg22;
    ArrayList<Graph_MonthlyAllData> monthAllData = new ArrayList<>();
    FirebaseDatabase db;
    DatabaseReference ref_monthly, ref_gate, ref_touchpoint;

    Spinner gate_tv, touchpt_tv;
    List touchpoint_list = new ArrayList<>();
    List gate_list = new ArrayList<>();
    String touchpt_selected = "Check_in", gate_selected = "N1", city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph__monthly);

        avg_switch = findViewById(R.id.Average_button);
        footfall_switch = findViewById(R.id.Footfall_button);
        chart1 = findViewById(R.id.bgraph1);
        pg22 = findViewById(R.id.pg22);

        touchpt_tv = findViewById(R.id.touchpoint_spinner);
        gate_tv = findViewById(R.id.gate_no_spinner);


        //chart1.getAxisLeft().setDrawGridLines(false);
        chart1.getXAxis().setDrawGridLines(false);
        //chart1.getAxisRight().setDrawGridLines(false);//description
        chart1.setNoDataText("Loading");
        chart1.getDescription().setEnabled(false);
        //chart1.setScaleEnabled(false);
        //chart1.setBackgroundColor(R.color.Grey);

        db = FirebaseDatabase.getInstance();
        city = SaveSharedPreferences.getAirportName(this);
        ref_touchpoint = db.getReference(city);

        // ref_monthly = db.getReference("/A1/N1/Monthly");
        //ref_monthly.keepSynced(true);
        System.out.println("welcome_month");

        fetch_data();

        avg_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click");
                showChart_Monthly();
            }
        });

        footfall_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click");
                showChart_Monthly();
            }
        });

        pg22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click");
                showChart_Monthly();
            }
        });
        ref_touchpoint.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                touchpoint_list.add(dataSnapshot.getKey());
                ArrayAdapter<String> adapter_touchpoint = new ArrayAdapter<String>(Graph_Monthly.this, android.R.layout.simple_spinner_item, touchpoint_list);
                touchpt_tv.setAdapter(adapter_touchpoint);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        touchpt_tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                touchpt_selected = parent.getItemAtPosition((position)).toString();
                System.out.println("touchpt_selected:" + touchpt_selected);
                fetch_gate_no();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gate_tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gate_selected = parent.getItemAtPosition((position)).toString();
                System.out.println("gate_selected:" + gate_selected);
                fetch_data();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void fetch_data() {
        ref_monthly = db.getReference(city + "/" + touchpt_selected + "/" + gate_selected + "/Monthly");
        ref_monthly.keepSynced(true);
        ref_monthly.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("snap1");

                if (dataSnapshot.hasChildren()) {
                    System.out.println("has children");

                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Graph_Monthly_DataPoint p1 = snap.getValue(Graph_Monthly_DataPoint.class);
                        monthAllData.add(new Graph_MonthlyAllData(p1.getAvg_dwell(), p1.getFootfall(), p1.getMonth()));
                        System.out.println("Dataaa:" + p1.getMonth() + "  " + p1.getAvg_dwell() + "  " + p1.getFootfall());
                    }
                    showChart_Monthly();

                } else {
                    System.out.println("No data found");
                    chart1.clear();
                    chart1.invalidate();
                    Toast.makeText(Graph_Monthly.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
                Toast.makeText(Graph_Monthly.this, "Failed to read value.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void fetch_gate_no() {

        ref_gate = db.getReference(city + "/" + touchpt_selected);
        ref_gate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("snap gate no");

                if (dataSnapshot.hasChildren()) {
                    System.out.println("has children");
                    gate_list.clear();

                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        gate_list.add(snap.getKey());
                    }
                    ArrayAdapter<String> adapter_gate = new ArrayAdapter<String>(Graph_Monthly.this, android.R.layout.simple_list_item_1, gate_list);
                    gate_tv.setAdapter(adapter_gate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
                Toast.makeText(Graph_Monthly.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showChart_Monthly() {

        ArrayList<BarEntry> barEntryList_avg = new ArrayList<>();
        ArrayList<BarEntry> barEntryList_footfall = new ArrayList<>();
        ArrayList<BarEntry> barEntryList_footfall_formatted = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        System.out.println("show Chart");

        for (int i = 0; i < monthAllData.size(); i++) {
            int avg = Integer.parseInt(monthAllData.get(i).getAvgAll());
            int footfall = Integer.parseInt(monthAllData.get(i).getFootfallAll());
            footfall /= 1000;

            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.MONTH, Integer.parseInt(monthAllData.get(i).getMonthAll()) - 1);
            System.out.println(calendar.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
            simpleDateFormat.setCalendar(calendar);
            String monthName = simpleDateFormat.format(calendar.getTime());

            System.out.println("Month:" + monthName + "   Avg:" + avg + "   Footfall:" + footfall);
            barEntryList_avg.add(new BarEntry(i, avg));
            barEntryList_footfall.add(new BarEntry(i, footfall * 1000));
            barEntryList_footfall_formatted.add(new BarEntry(i, footfall));
            labels.add(monthName.substring(0, 3));
        }

        BarDataSet barDataSet_avg = new BarDataSet(barEntryList_avg, "Average dwell time");
        BarDataSet barDataSet_footfall = new BarDataSet(barEntryList_footfall, "Footfall");
        BarDataSet barDataSet_footfall_formatted = new BarDataSet(barEntryList_footfall_formatted, "Footfall in thousands");

        barDataSet_avg.setColors(getResources().getColor(R.color.graph_line_2));
        barDataSet_footfall.setColors((getResources().getColor(R.color.graph_line)));
        barDataSet_footfall_formatted.setColors((getResources().getColor(R.color.graph_line)));

        barDataSet_avg.setValueTextSize(10);
        barDataSet_footfall.setValueTextSize(10);
        barDataSet_footfall_formatted.setValueTextSize(10);

        barDataSet_footfall_formatted.setValueFormatter(new MyValueFormatter_k());
        barDataSet_footfall.setValueFormatter(new MyValueFormatter_1());

        // Description desc = new Description();
        //desc.setText("Monthss");
        //chart1.setDescription(desc);

        XAxis x_axis = chart1.getXAxis();
        YAxis ry_axis = chart1.getAxisRight();
        YAxis ly_axis = chart1.getAxisLeft();

        x_axis.setValueFormatter(new IndexAxisValueFormatter(labels));
        x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //x_axis.setCenterAxisLabels(true);
        x_axis.setGranularity(1f);
        x_axis.setGranularityEnabled(true);
        x_axis.setLabelCount(labels.size());
        x_axis.setTextColor((getResources().getColor(R.color.graph_text_axis)));

        ly_axis.setTextColor((getResources().getColor(R.color.graph_text_axis)));
        ly_axis.setGranularity(1f);
        ly_axis.setAxisMinimum(0);
        ly_axis.setDrawAxisLine(false);

        ry_axis.setEnabled(false);

        Legend legend;
        legend = chart1.getLegend();
        legend.setEnabled(true);
        legend.setTextColor((getResources().getColor(R.color.graph_text_dwell)));
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        //legend.setYEntrySpace(-5);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setFormSize(4f);
        legend.setTextSize(0.7f);
        //legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        BarData barData_avg = new BarData();
        BarData barData_footfall = new BarData();
        BarData barData_both = new BarData(barDataSet_avg, barDataSet_footfall_formatted);

        barData_avg.addDataSet(barDataSet_avg);
        barData_footfall.addDataSet(barDataSet_footfall);

        barData_avg.setBarWidth(0.5f);
        barData_footfall.setBarWidth(0.5f);
        barData_both.setBarWidth(0.5f);

        barData_avg.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
        barData_footfall.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
        barData_both.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
        //barDataSet_avg.setValueFormatter(new MyValueFormatter1());

        if (avg_switch.isChecked() && !footfall_switch.isChecked()) {

            chart1.clear();
            chart1.setData(barData_avg);
            chart1.invalidate();

        } else if (!avg_switch.isChecked() && footfall_switch.isChecked()) {

            chart1.clear();
            chart1.setData(barData_footfall);
            chart1.invalidate();

        } else if (avg_switch.isChecked() && footfall_switch.isChecked()) {

            chart1.clear();
            //chart1.setDragEnabled(true);
            //chart1.setVisibleXRangeMaximum(5);
            barData_both.setBarWidth(0.175f);//0.15
            chart1.setData(barData_both);
            chart1.invalidate();

        } else {
            chart1.clear();
            chart1.invalidate();
            Toast.makeText(Graph_Monthly.this, "Select atleast one of the two parameters", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyValueFormatter_k extends ValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return (int) value + "k";
        }
    }

    public class MyValueFormatter_1 extends ValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return (int) value + "";
        }
    }

}
