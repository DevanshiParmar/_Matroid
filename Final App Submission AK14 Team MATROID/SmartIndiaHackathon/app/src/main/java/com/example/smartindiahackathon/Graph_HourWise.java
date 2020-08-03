package com.example.smartindiahackathon;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Graph_HourWise extends AppCompatActivity {
    ImageButton next_button, previous_button;
    Button pg;
    EditText selector;
    LineChart chart1;
    java.util.Date entry, exit;
    ArrayList<Entry> dataVals = new ArrayList<>();
    String path, city;
    int hr;
    Date time_set;

    Spinner gate_tv, touchpt_tv;
    List touchpoint_list = new ArrayList<>();
    List gate_list = new ArrayList<>();
    String touchpt_selected = "Check_in", gate_selected = "N1";
    String formattedDate;
    FirebaseDatabase db;
    DatabaseReference ref, ref_gate, ref_touchpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph__hour_wise);

        pg = (Button) findViewById(R.id.pg22);
        chart1 = (LineChart) findViewById(R.id.linegraph1);
        next_button = (ImageButton) findViewById(R.id.next_button);
        previous_button = (ImageButton) findViewById(R.id.previous_button);
        selector = (EditText) findViewById(R.id.selector);

        touchpt_tv = findViewById(R.id.touchpoint_spinner);
        gate_tv = findViewById(R.id.gate_no_spinner);


        //chart1.getAxisLeft().setDrawGridLines(false);
        chart1.getXAxis().setDrawGridLines(false);
        //chart1.getAxisRight().setDrawGridLines(false);//description
        chart1.setNoDataText("Loading");
        chart1.getDescription().setEnabled(false);
        //chart1.setScaleEnabled(false);
        //chart1.setDrawBorders(true);

        Calendar calendar_1 = Calendar.getInstance();
        time_set = calendar_1.getTime();
        SimpleDateFormat path_df = new SimpleDateFormat("MM-dd_HH", Locale.getDefault());
        formattedDate = path_df.format(time_set);

        db = FirebaseDatabase.getInstance();
        city = SaveSharedPreferences.getAirportName(this);
        ref_touchpoint = db.getReference(city);

        path = city + "/" + touchpt_selected + "/" + gate_selected + "/Entries/" + formattedDate;
        hr = time_set.getHours();
        System.out.println("path: " + path);
        fetchData(path, 0);

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat path_df = new SimpleDateFormat("MM-dd_HH", Locale.getDefault());

                calendar.setTime(time_set);
                calendar.add(Calendar.HOUR, 1);
                time_set = calendar.getTime();
                String formattedDate = path_df.format(time_set);
                System.out.println("path: " + path);
                path = city + "/" + touchpt_selected + "/" + gate_selected + "/Entries/" + formattedDate;
                hr = time_set.getHours();
                fetchData(path, 1);
            }
        });

        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat path_df = new SimpleDateFormat("MM-dd_HH", Locale.getDefault());
                time_set = new Date(time_set.getTime() - 3600 * 1000);
                String formattedDate = path_df.format(time_set);
                System.out.println("path: " + path);
                path = city + "/" + touchpt_selected + "/" + gate_selected + "/Entries/" + formattedDate;
                hr = time_set.getHours();
                fetchData(path, 2);
            }
        });


        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click");
                if (!selector.getText().equals("")) {
                    if (0 <= Integer.parseInt(String.valueOf(selector.getText())) && Integer.parseInt(String.valueOf(selector.getText())) < 24) {

                        hr = Integer.parseInt(String.valueOf(selector.getText()));
                        System.out.println("hr:" + hr);
                        SimpleDateFormat path_df = new SimpleDateFormat("MM-dd_HH", Locale.getDefault());
                        String formattedDate = path_df.format(time_set);
                        System.out.println("path: " + path);

                        if (hr < 10)
                            path = city + "/" + touchpt_selected + "/" + gate_selected + "/Entries/" + formattedDate.substring(0, 6) + "0" + hr;
                        else
                            path = city + "/" + touchpt_selected + "/" + gate_selected + "/Entries/" + formattedDate.substring(0, 6) + hr;

                        fetchData(path, 3);
                    } else {
                        Toast.makeText(Graph_HourWise.this, "Enter a valid hour from 24 hour format", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        ref_touchpoint.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                touchpoint_list.add(dataSnapshot.getKey());
                ArrayAdapter<String> adapter_touchpoint = new ArrayAdapter<String>(Graph_HourWise.this, android.R.layout.simple_spinner_item, touchpoint_list);
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
                fetchData(city + "/" + touchpt_selected + "/" + gate_selected + "/Entries/" + formattedDate, 0);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fetchData(String path, final int flag) {

        ref = db.getReference(path);
        ref.keepSynced(true);
        final SimpleDateFormat df = new SimpleDateFormat("mm:ss");
        final SimpleDateFormat tm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        selector = (EditText) findViewById(R.id.selector);
        System.out.println("path given:" + path);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataVals.clear();
                System.out.println("datachange");

                if (dataSnapshot.hasChildren()) {
                    System.out.println("has children");
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Graph_HourWise_DataPoint p1 = snap.getValue(Graph_HourWise_DataPoint.class);
                        try {
                            entry = tm.parse(p1.getEntry_time());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            exit = tm.parse(p1.getExit_time());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        System.out.println("p1 :" + p1.getExit_time() + "," + p1.getEntry_time());

                        long diff_min = (exit.getTime() - entry.getTime()) / (60 * 1000);
                        long diff_sec = (exit.getTime() - entry.getTime()) / (1000);
                        //System.out.println("Exit :" + exit + ", " + "Entry :" + entry );//+ ",Dwell :" + diff_min + " " + diff_sec + ",exit minutes :" + exit.getMinutes());
                        //exit_list.add(diff_sec);
                        //entry_list.add(entry);
                        dataVals.add(new Entry(exit.getMinutes(), diff_sec));
                    }


                    if (flag == 3) {
                        time_set.setHours(hr);
                    }
                    selector.setText(String.valueOf(hr));
                    showChart();
                } else {
                    System.out.println("else- no chidren");
                    Toast.makeText(Graph_HourWise.this, "No data available", Toast.LENGTH_SHORT).show();
                    if (flag == 1) {
                        time_set = new Date(time_set.getTime() - 3600 * 1000);
                    } else if (flag == 2) {
                        time_set = new Date(time_set.getTime() + 3600 * 1000);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
                Toast.makeText(Graph_HourWise.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
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
                    ArrayAdapter<String> adapter_gate = new ArrayAdapter<String>(Graph_HourWise.this, android.R.layout.simple_list_item_1, gate_list);
                    gate_tv.setAdapter(adapter_gate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
                Toast.makeText(Graph_HourWise.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showChart() {
        LineDataSet lineDataSet = new LineDataSet(dataVals, "Dwell Time");
        ArrayList<ILineDataSet> iLineEntryList = new ArrayList<>();
        LineData lineData;
        System.out.println("show Chart");

        lineDataSet.setLineWidth(2);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setColor((getResources().getColor(R.color.graph_line_2)));
        lineDataSet.setValueTextSize(12);
        lineDataSet.setCircleHoleColor((getResources().getColor(R.color.graph_circle_hole)));
        lineDataSet.setCircleColor((getResources().getColor(R.color.graph_circle)));


        //lineDataSet.setColors(ColorTemplate.LIBERTY_COLORS);

        XAxis x_axis = chart1.getXAxis();
        YAxis ry_axis = chart1.getAxisRight();
        YAxis ly_axis = chart1.getAxisLeft();

        x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        x_axis.setAxisMaximum(60);
        x_axis.setLabelCount(6);
        x_axis.setAxisMinimum(0);
        x_axis.setValueFormatter(new MyAxisFormatter1());
        x_axis.setTextColor((getResources().getColor(R.color.graph_text_axis)));
        x_axis.setAxisLineColor((getResources().getColor(R.color.graph_line)));

        //ly_axis.setDrawZeroLine(true);
        ly_axis.setGranularity(1f);
        ly_axis.setAxisMinimum(0);
        ly_axis.setDrawAxisLine(false);
        ly_axis.setTextColor((getResources().getColor(R.color.graph_text_axis)));
        //ly_axis.setAxisLineColor((getResources().getColor(R.color.graph_line)));

        ry_axis.setEnabled(false);

        Legend legend;
        legend = chart1.getLegend();
        legend.setEnabled(true);
        legend.setTextColor((getResources().getColor(R.color.graph_text_dwell)));

        //barlineDataSet.setValueFormatter(new MyValueFormatter1());

        iLineEntryList.clear();
        iLineEntryList.add(lineDataSet);
        lineData = new LineData(iLineEntryList);
        lineData.setValueFormatter(new MyValueFormatter());

        lineData.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
        lineData.setValueTextColor((getResources().getColor(R.color.transparent)));

        chart1.clear();
        chart1.setData(lineData);
        chart1.invalidate();
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

    private class MyAxisFormatter1 extends ValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            if (hr < 10) {
                if (value == 0)
                    return "0" + hr + ":00";
                else
                    return "0" + hr + ":" + ((int) value);
            } else {
                if (value == 0)
                    return hr + ":00";
                else
                    return hr + ":" + ((int) value);
            }
        }
    }
}