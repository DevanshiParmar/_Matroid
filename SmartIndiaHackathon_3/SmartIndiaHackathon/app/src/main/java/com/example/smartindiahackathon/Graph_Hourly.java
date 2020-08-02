package com.example.smartindiahackathon;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Graph_Hourly extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageButton next_button, previous_button;
    Button pg;

    Spinner gate_tv, touchpt_tv;
    List touchpoint_list = new ArrayList<>();
    List gate_list = new ArrayList<>();
    String touchpt_selected = "Check_in", gate_selected = "N1", city;

    EditText selector;
    ArrayList<Entry> dataVals_avg = new ArrayList<>();
    ArrayList<Entry> dataVals_ff = new ArrayList<>();
    int day;
    Date time_set;

    Switch avg_switch, footfall_switch;
    LineChart chart1;
    ArrayList<Graph_MonthlyAllData> monthAllData = new ArrayList<>();

    FirebaseDatabase db;
    DatabaseReference ref_hourly, ref_gate, ref_touchpoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph__hourly);

        pg = (Button) findViewById(R.id.pg22);
        next_button = (ImageButton) findViewById(R.id.next_button);
        previous_button = (ImageButton) findViewById(R.id.previous_button);
        selector = (EditText) findViewById(R.id.selector);

        chart1 = findViewById(R.id.linegraph1);
        avg_switch = findViewById(R.id.Average_button);
        footfall_switch = findViewById(R.id.Footfall_button);

        touchpt_tv = findViewById(R.id.touchpoint_spinner);
        gate_tv = findViewById(R.id.gate_no_spinner);

        //chart1.getAxisLeft().setDrawGridLines(false);
        chart1.getXAxis().setDrawGridLines(false);
        //chart1.getAxisRight().setDrawGridLines(false);//description
        chart1.setNoDataText("Loading");
        chart1.getDescription().setEnabled(false);
        //chart1.setScaleEnabled(false);
        //chart1.setDrawBorders(true);

        db = FirebaseDatabase.getInstance();
        city = SaveSharedPreferences.getAirportName(this);
        ref_touchpoint = db.getReference(city);

        Calendar calendar_1 = Calendar.getInstance();
        time_set = calendar_1.getTime();

        fetch_data();

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_set = new Date(time_set.getTime() + 3600 * 1000 * 24);

                SimpleDateFormat path_df = new SimpleDateFormat("MM-dd", Locale.getDefault());
                String formattedDate = path_df.format(time_set);
                System.out.println("formattedDate: " + formattedDate);

                int j;
                for (j = 0; j < monthAllData.size(); j++) {

                    if (monthAllData.get(j).getMonthAll().substring(0, 5).equals(path_df.format(time_set))) {
                        dataVals_avg.clear();
                        dataVals_ff.clear();

                        for (; j < monthAllData.size() && monthAllData.get(j).getMonthAll().substring(0, 5).equals(path_df.format(time_set)); j++) {
                            dataVals_avg.add(new Entry(Float.parseFloat(monthAllData.get(j).getMonthAll().substring(6)), Float.parseFloat(monthAllData.get(j).getAvgAll())));
                            dataVals_ff.add(new Entry(Float.parseFloat(monthAllData.get(j).getMonthAll().substring(6)), Float.parseFloat(monthAllData.get(j).getFootfallAll())));
                        }
                        selector.setText("" + time_set.getDate());
                        showChart();
                        break;
                    }
                    if ((monthAllData.size() - 1) == j) {
                        time_set = new Date(time_set.getTime() - 3600 * 1000 * 24);
                        Toast.makeText(Graph_Hourly.this, "No data available", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                time_set = new Date(time_set.getTime() - 3600 * 1000 * 24);

                SimpleDateFormat path_df = new SimpleDateFormat("MM-dd", Locale.getDefault());
                String formattedDate = path_df.format(time_set);
                System.out.println("formattedDate: " + formattedDate);

                int j;
                for (j = 0; j < monthAllData.size(); j++) {

                    if (monthAllData.get(j).getMonthAll().substring(0, 5).equals(path_df.format(time_set))) {
                        dataVals_avg.clear();
                        dataVals_ff.clear();

                        for (; j < monthAllData.size() && monthAllData.get(j).getMonthAll().substring(0, 5).equals(path_df.format(time_set)); j++) {
                            dataVals_avg.add(new Entry(Float.parseFloat(monthAllData.get(j).getMonthAll().substring(6)), Float.parseFloat(monthAllData.get(j).getAvgAll())));
                            dataVals_ff.add(new Entry(Float.parseFloat(monthAllData.get(j).getMonthAll().substring(6)), Float.parseFloat(monthAllData.get(j).getFootfallAll())));

                            System.out.println("dataVals:" + dataVals_avg.get(dataVals_avg.size() - 1));
                        }
                        selector.setText("" + time_set.getDate());
                        showChart();
                        break;
                    }
                    if ((monthAllData.size() - 1) == j) {
                        time_set = new Date(time_set.getTime() + 3600 * 1000 * 24);
                        Toast.makeText(Graph_Hourly.this, "No data available", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click");
                if (!selector.getText().equals("")) {
                    if (0 <= Integer.parseInt(String.valueOf(selector.getText())) && Integer.parseInt(String.valueOf(selector.getText())) < 32) {

                        day = Integer.parseInt(String.valueOf(selector.getText()));
                        System.out.println("day:" + day);

                        int j;
                        for (j = 0; j < monthAllData.size(); j++) {

                            if (Integer.parseInt(monthAllData.get(j).getMonthAll().substring(3, 5)) == day) {
                                dataVals_avg.clear();
                                dataVals_ff.clear();

                                for (; j < monthAllData.size() && Integer.parseInt(monthAllData.get(j).getMonthAll().substring(3, 5)) == day; j++) {
                                    dataVals_avg.add(new Entry(Float.parseFloat(monthAllData.get(j).getMonthAll().substring(6)), Float.parseFloat(monthAllData.get(j).getAvgAll())));
                                    dataVals_ff.add(new Entry(Float.parseFloat(monthAllData.get(j).getMonthAll().substring(6)), Float.parseFloat(monthAllData.get(j).getFootfallAll())));
                                    System.out.println("dataVals:" + dataVals_avg.get(dataVals_avg.size() - 1));
                                }
                                showChart();
                                break;
                            }
                            if ((monthAllData.size() - 1) == j) {
                                Toast.makeText(Graph_Hourly.this, "No data available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(Graph_Hourly.this, "Enter a valid date", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        avg_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click");
                showChart();
            }
        });

        footfall_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click");
                showChart();
            }
        });

        ref_touchpoint.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                touchpoint_list.add(dataSnapshot.getKey());
                ArrayAdapter<String> adapter_touchpoint = new ArrayAdapter<String>(Graph_Hourly.this, android.R.layout.simple_spinner_item, touchpoint_list);
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
        ref_hourly = db.getReference("Ahmedabad/" + touchpt_selected + "/" + gate_selected + "/Hourly");
        ref_hourly.keepSynced(true);

        ref_hourly.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("snap Hourly");
                SimpleDateFormat ref_df = new SimpleDateFormat("MM-dd", Locale.getDefault());

                if (dataSnapshot.hasChildren()) {
                    System.out.println("has children");
                    dataVals_avg.clear();
                    dataVals_ff.clear();

                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Graph_Hourly_DataPoint p1 = snap.getValue(Graph_Hourly_DataPoint.class);
                        monthAllData.add(new Graph_MonthlyAllData(p1.getAvg_dwell(), p1.getFootfall(), p1.getHour()));
                        System.out.println("p1:" + p1.getAvg_dwell() + "  " + p1.getHour().substring(0, 5) + "  " + p1.getFootfall());
                        System.out.println("calendar:" + ref_df.format(Calendar.getInstance().getTime()));
                        if (p1.getHour().substring(0, 5).equals(ref_df.format(Calendar.getInstance().getTime()))) {
                            dataVals_avg.add(new Entry(Float.parseFloat(p1.getHour().substring(6)), Float.parseFloat(p1.getAvg_dwell())));
                            dataVals_ff.add(new Entry(Float.parseFloat(p1.getHour().substring(6)), Float.parseFloat(p1.getFootfall())));
                            System.out.println("dataVals:" + dataVals_avg.size());
                        }
                    }
                    selector.setText("" + time_set.getDate());
                    showChart();

                } else {
                    System.out.println("No data found");
                    chart1.clear();
                    chart1.invalidate();
                    Toast.makeText(Graph_Hourly.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
                Toast.makeText(Graph_Hourly.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetch_gate_no() {

        ref_gate = db.getReference("Ahmedabad/" + touchpt_selected);
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
                    ArrayAdapter<String> adapter_gate = new ArrayAdapter<String>(Graph_Hourly.this, android.R.layout.simple_list_item_1, gate_list);
                    gate_tv.setAdapter(adapter_gate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
                Toast.makeText(Graph_Hourly.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showChart() {

        System.out.println("show Chart");

        LineDataSet lineDataSet_avg = new LineDataSet(dataVals_avg, "Average Dwell Time");
        LineDataSet lineDataSet_ff = new LineDataSet(dataVals_ff, "Footfall");

        lineDataSet_avg.setColors((getResources().getColor(R.color.graph_line_2)));
        lineDataSet_ff.setColors((getResources().getColor(R.color.graph_line)));

        lineDataSet_avg.setValueTextSize(12);
        lineDataSet_ff.setValueTextSize(12);

        lineDataSet_avg.setDrawHighlightIndicators(false);
        lineDataSet_ff.setDrawHighlightIndicators(false);

        lineDataSet_avg.setCircleHoleColor((getResources().getColor(R.color.graph_circle_hole)));
        lineDataSet_ff.setCircleHoleColor((getResources().getColor(R.color.graph_circle_hole)));

        lineDataSet_avg.setCircleColor((getResources().getColor(R.color.graph_circle)));
        lineDataSet_ff.setCircleColor((getResources().getColor(R.color.graph_line)));

        lineDataSet_avg.setLineWidth(2);
        //lineDataSet_avg.setValues();
        lineDataSet_ff.setLineWidth(2);

        XAxis x_axis = chart1.getXAxis();
        YAxis ry_axis = chart1.getAxisRight();
        YAxis ly_axis = chart1.getAxisLeft();

        x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        x_axis.setAxisMaximum(23);
        x_axis.setLabelCount(6);
        x_axis.setAxisMinimum(0);
        x_axis.setTextColor((getResources().getColor(R.color.graph_text_axis)));
        x_axis.setAxisLineColor((getResources().getColor(R.color.graph_line_axis)));

        //ly_axis.setDrawZeroLine(true);
        ly_axis.setGranularity(1f);
        ly_axis.setAxisMinimum(0);
        ly_axis.setDrawAxisLine(false);
        ly_axis.setTextColor((getResources().getColor(R.color.graph_text_axis)));
        ly_axis.setAxisLineColor((getResources().getColor(R.color.graph_line_axis)));
        //ly_axis.setAxisLineColor((getResources().getColor(R.color.graph_line)));

        ry_axis.setEnabled(false);

        Legend legend;
        legend = chart1.getLegend();
        legend.setEnabled(true);
        legend.setTextColor((getResources().getColor(R.color.graph_text_dwell)));


        ArrayList<ILineDataSet> iLineEntryList = new ArrayList<>();
        LineData lineData;

        //barlineDataSet_avg.setValueFormatter(new MyValueFormatter1());

        if (avg_switch.isChecked() && !footfall_switch.isChecked()) {
            iLineEntryList.clear();
            iLineEntryList.add(lineDataSet_avg);
            lineData = new LineData(iLineEntryList);
            lineData.setValueFormatter(new Graph_Hourly.MyValueFormatter());
            lineData.setValueTextColor((getResources().getColor(R.color.transparent)));

            //lineData.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
            chart1.clear();
            chart1.setData(lineData);
            chart1.invalidate();
        } else if (!avg_switch.isChecked() && footfall_switch.isChecked()) {
            iLineEntryList.clear();
            iLineEntryList.add(lineDataSet_ff);
            lineData = new LineData(iLineEntryList);
            lineData.setValueFormatter(new Graph_Hourly.MyValueFormatter());
            lineData.setValueTextColor((getResources().getColor(R.color.transparent)));

            //lineData.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
            chart1.clear();
            chart1.setData(lineData);
            chart1.invalidate();
        } else if (avg_switch.isChecked() && footfall_switch.isChecked()) {
            iLineEntryList.clear();
            iLineEntryList.add(lineDataSet_avg);
            iLineEntryList.add(lineDataSet_ff);
            lineData = new LineData(iLineEntryList);
            lineData.setValueTextColor((getResources().getColor(R.color.transparent)));

            //lineData.setValueFormatter(new Graph_Hourly_Bar.MyValueFormatter());
            //lineData.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
            chart1.clear();
            chart1.setData(lineData);
            chart1.invalidate();
        } else {
            Toast.makeText(Graph_Hourly.this, "Select either dwell time or footfall or both", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        touchpt_selected = "Check_in";
        System.out.println("touchpt_selected onNothingSelected:" + touchpt_selected);
    }

    private class MyValueFormatter extends ValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            String Formatted_min, Formatted_sec;
            Formatted_min = "" + ((int) value / 60);
            Formatted_sec = "" + ((int) value % 60);

            if ((((int) value / 60) < 10)) {
                Formatted_min = "0" + Formatted_min;
            }
            if (((int) value % 60) < 10) {
                Formatted_sec = "" + Formatted_sec;
            }
            return Formatted_min + ":" + Formatted_sec;
        }
    }

}
