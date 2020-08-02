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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
import java.util.List;
import java.util.Locale;

public class Graph_Daily extends AppCompatActivity {

    ImageButton next_button, previous_button;
    Button pg;

    EditText selector;
    ArrayList<BarEntry> dataVals_avg = new ArrayList<>();
    ArrayList<BarEntry> dataVals_ff = new ArrayList<>();
    ArrayList<BarEntry> dataVals_ff_formatted = new ArrayList<>();
    ArrayList<Integer> sundaysIndex = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();

    Spinner gate_tv, touchpt_tv;
    List touchpoint_list = new ArrayList<>();
    List gate_list = new ArrayList<>();
    String touchpt_selected = "Check_in", gate_selected = "N1", city;

    int week;

    Switch avg_switch, footfall_switch;
    BarChart chart1;
    ArrayList<Graph_MonthlyAllData> monthAllData = new ArrayList<>();
    FirebaseDatabase db;
    DatabaseReference ref_daily, ref_gate, ref_touchpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph__daily);

        pg = (Button) findViewById(R.id.pg22);
        next_button = (ImageButton) findViewById(R.id.next_button);
        previous_button = (ImageButton) findViewById(R.id.previous_button);
        selector = (EditText) findViewById(R.id.selector);

        chart1 = findViewById(R.id.bgraph1);
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
        //ref_daily = db.getReference("Ahmedabad/A1/N1/Daily");
        //ref_daily.keepSynced(true);
        city = SaveSharedPreferences.getAirportName(this);
        ref_touchpoint = db.getReference(city);

        int i = 0;
        String[] weekdays = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        for (; i < 7; i++)
            labels.add(weekdays[i]);

        fetch_data();

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (week < (sundaysIndex.size() - 2)) {
                    week++;
                    formDataVals(sundaysIndex.get(week), sundaysIndex.get(week + 1));
                } else
                    Toast.makeText(Graph_Daily.this, "Further data not available", Toast.LENGTH_SHORT).show();
            }
        });

        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (week > 0) {
                    week--;
                    formDataVals(sundaysIndex.get(week), sundaysIndex.get(week + 1));
                } else
                    Toast.makeText(Graph_Daily.this, "Further data not available", Toast.LENGTH_SHORT).show();
            }
        });
/*
        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("button click");
                if (!selector.getText().equals("")) {
                    if (0 <= Integer.parseInt(String.valueOf(selector.getText())) && Integer.parseInt(String.valueOf(selector.getText())) < sundaysIndex.size()) {
                        week = Integer.parseInt(String.valueOf(selector.getText()));
                        formDataVals(sundaysIndex.get(week));
                    } else
                        Toast.makeText(Graph_Daily.this, "Data not available for this week", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(Graph_Daily.this, "Enter a valid week", Toast.LENGTH_SHORT).show();

            }
        });
*/
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
                ArrayAdapter<String> adapter_touchpoint = new ArrayAdapter<String>(Graph_Daily.this, android.R.layout.simple_spinner_item, touchpoint_list);
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
        ref_daily = db.getReference("Ahmedabad/" + touchpt_selected + "/" + gate_selected + "/Daily");
        ref_daily.keepSynced(true);
        ref_daily.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("snap1");

                if (dataSnapshot.hasChildren()) {
                    System.out.println("has children");
                    Calendar calendar_1 = Calendar.getInstance();
                    SimpleDateFormat ref_df = new SimpleDateFormat("MM-dd", Locale.getDefault());
                    int index = 0;
                    sundaysIndex.clear();

                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Graph_Daily_DataPoint p1 = snap.getValue(Graph_Daily_DataPoint.class);
                        monthAllData.add(new Graph_MonthlyAllData(p1.getAvg_dwell(), p1.getFootfall(), p1.getDate()));

                        // System.out.println("p1:" + p1.getAvg_dwell() + "  " + p1.getDate() + "  " + p1.getFootfall());
                        try {
                            calendar_1.setTime(ref_df.parse(p1.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (index == 0 && !((calendar_1.get(Calendar.DAY_OF_WEEK) - 1) == 0)) {
                            sundaysIndex.add(0);
                        }

                        //System.out.println("day:" + (calendar_1.get(Calendar.DAY_OF_WEEK) - 1));
                        if ((calendar_1.get(Calendar.DAY_OF_WEEK) - 1) == 0) {
                            sundaysIndex.add(index);
                            //   System.out.println("Sunday on:" + (calendar_1.get(Calendar.DAY_OF_WEEK) - 1) + ", index:" +index+", sundayIndexSize:"+ sundaysIndex.size());
                        }
                        index++;
                    }
                    sundaysIndex.add(index - 1);
                    week = sundaysIndex.size() - 2;
                    System.out.println("WEEK no:" + week);
                    formDataVals(sundaysIndex.get(sundaysIndex.size() - 2), sundaysIndex.get(sundaysIndex.size() - 1));
                } else {
                    System.out.println("else- no chidren");
                    chart1.clear();
                    chart1.invalidate();
                    Toast.makeText(Graph_Daily.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
                Toast.makeText(Graph_Daily.this, "Failed to read value.", Toast.LENGTH_SHORT).show();

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
                    ArrayAdapter<String> adapter_gate = new ArrayAdapter<String>(Graph_Daily.this, android.R.layout.simple_list_item_1, gate_list);
                    gate_tv.setAdapter(adapter_gate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Failed to read value." + databaseError.toException());
                Toast.makeText(Graph_Daily.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showChart() {

        System.out.println("show Chart");

        BarDataSet barDataSet_avg = new BarDataSet(dataVals_avg, "Average Dwell Time");
        BarDataSet barDataSet_footfall = new BarDataSet(dataVals_ff, "Footfall");
        BarDataSet barDataSet_footfall_formatted = new BarDataSet(dataVals_ff_formatted, "Footfall");

        barDataSet_avg.setColors(getResources().getColor(R.color.graph_line_2));
        barDataSet_footfall.setColors((getResources().getColor(R.color.graph_line)));
        barDataSet_footfall_formatted.setColors((getResources().getColor(R.color.graph_line)));

        barDataSet_avg.setValueTextSize(12);
        barDataSet_footfall.setValueTextSize(12);
        barDataSet_footfall_formatted.setValueTextSize(12);

        barDataSet_avg.setValueFormatter(new Graph_Daily.MyValueFormatter_1());
        barDataSet_footfall.setValueFormatter(new Graph_Daily.MyValueFormatter_1());
        barDataSet_footfall_formatted.setValueFormatter(new Graph_Daily.MyValueFormatter_k());

        // Description desc = new Description();
        //desc.setText("Monthss");
        //chart1.setDescription(desc);

        XAxis x_axis = chart1.getXAxis();
        YAxis ry_axis = chart1.getAxisRight();
        YAxis ly_axis = chart1.getAxisLeft();

        x_axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        x_axis.setGranularity(1f);
        x_axis.setGranularityEnabled(true);
        x_axis.setLabelCount(7, false);
        x_axis.setValueFormatter(new IndexAxisValueFormatter(labels));
        x_axis.setTextColor((getResources().getColor(R.color.graph_text_axis)));

        //ly_axis.setDrawZeroLine(true);
        ly_axis.setTextColor((getResources().getColor(R.color.graph_text_axis)));
        ly_axis.setGranularity(1f);
        ly_axis.setAxisMinimum(0);
        ly_axis.setDrawAxisLine(false);

        ry_axis.setEnabled(false);
        //ly_axis.setLabelCount(10, true);

        Legend legend;
        legend = chart1.getLegend();
        legend.setEnabled(true);
        legend.setTextColor((getResources().getColor(R.color.graph_text_dwell)));

        BarData barData_avg = new BarData();
        BarData barData_footfall = new BarData();
        BarData barData_both = new BarData(barDataSet_avg, barDataSet_footfall_formatted);

        barData_avg.addDataSet(barDataSet_avg);
        barData_footfall.addDataSet(barDataSet_footfall);

        barData_avg.setBarWidth(0.5f);
        barData_footfall.setBarWidth(0.5f);

        barData_avg.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
        barData_footfall.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));
        barData_both.setValueTextColor((getResources().getColor(R.color.graph_text_dwell)));

        //barDataSet_avg.setValueFormatter(new MyValueFormatter1());

        if (avg_switch.isChecked() && !footfall_switch.isChecked()) {
            // x_axis.setValueFormatter(new Graph_Daily.MyAxisFormatter1());

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
            Toast.makeText(Graph_Daily.this, "Select atleast one of the two parameters", Toast.LENGTH_SHORT).show();
        }

    }

    public void formDataVals(int j, int k) {
        dataVals_avg.clear();
        dataVals_ff.clear();
        System.out.println("For week:" + k);
        int i;

        Calendar calendar_1 = Calendar.getInstance();
        SimpleDateFormat ref_df = new SimpleDateFormat("MM-dd", Locale.getDefault());

        try {
            calendar_1.setTime(ref_df.parse(monthAllData.get(j).getMonthAll()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        i = 0;

        for (; i < (calendar_1.get(Calendar.DAY_OF_WEEK) - 1); i++) {
            dataVals_avg.add(new BarEntry(i, 0));
            dataVals_ff.add(new BarEntry(i, 0));
            dataVals_ff_formatted.add(new BarEntry(i, 0));
        }

        int date_selector = Integer.parseInt(monthAllData.get(j).getMonthAll().substring(3, 5));
        calendar_1.add(calendar_1.DATE, 6 - calendar_1.get(Calendar.DAY_OF_WEEK) + 1);
        int mnth_selector;
        int date_selector_added = calendar_1.getTime().getDate();

        if (date_selector > date_selector_added)
            mnth_selector = Integer.parseInt(monthAllData.get(j).getMonthAll().substring(0, 2)) + 1;
        else
            mnth_selector = Integer.parseInt(monthAllData.get(j).getMonthAll().substring(0, 2));

        selector.setText(date_selector + " to " + date_selector_added + "/" + mnth_selector);

        for (; j < k; i++, j++) {
            dataVals_avg.add(new BarEntry(i, Float.parseFloat(monthAllData.get(j).getAvgAll())));
            dataVals_ff.add(new BarEntry(i, Float.parseFloat(monthAllData.get(j).getFootfallAll())));
            dataVals_ff_formatted.add(new BarEntry(i, Float.parseFloat(monthAllData.get(j).getFootfallAll()) / 10));
        }
        if (k == sundaysIndex.get(sundaysIndex.size() - 1)) {
            dataVals_avg.add(new BarEntry(i, Float.parseFloat(monthAllData.get(j).getAvgAll())));
            dataVals_ff.add(new BarEntry(i, Float.parseFloat(monthAllData.get(j).getFootfallAll())));
            dataVals_ff_formatted.add(new BarEntry(i, Float.parseFloat(monthAllData.get(j).getFootfallAll()) / 10));
            i++;
        }
        for (; i < 7; i++) {
            dataVals_avg.add(new BarEntry(i, 0));
            dataVals_ff.add(new BarEntry(i, 0));
            dataVals_ff_formatted.add(new BarEntry(i, 0));
        }

        showChart();
    }


    private class MyValueFormatter extends ValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            String Formatted_min, Formatted_sec;
            Formatted_min = "" + ((int) value / 60);
            Formatted_sec = "" + ((int) value % 60);

            System.out.println("formDataValues");

            if ((((int) value / 60) < 10)) {
                Formatted_min = "0" + Formatted_min;
            }
            if (((int) value % 60) < 10) {
                Formatted_sec = "" + Formatted_sec;
            }
            return Formatted_min + ":" + Formatted_sec;
        }
    }

    public class MyValueFormatter_k extends ValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return ((int) value * 10) + "";
        }
    }

    public class MyValueFormatter_1 extends ValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            if (value == 0)
                return "";
            else
                return ((int) value) + "";
        }
    }

    private class MyAxisFormatter1 extends ValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String[] weekdays = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
            String day = weekdays[(int) value + 1];
            System.out.println("day:" + day);
            return day.concat("-" + value);
        }
    }
}
