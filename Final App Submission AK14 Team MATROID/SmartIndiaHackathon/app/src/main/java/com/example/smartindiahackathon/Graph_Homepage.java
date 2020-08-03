package com.example.smartindiahackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Graph_Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph__homepage);

        CardView daily,hourly,monthly,weekly;

        daily = findViewById(R.id.daily_btn);
        hourly = findViewById(R.id.hourly_btn);
        monthly = findViewById(R.id.monthly_btn);
        weekly = findViewById(R.id.weekly_btn);

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Graph_Homepage.this, Graph_Hourly.class);
                startActivity(intent);
            }
        });
        hourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Graph_Homepage.this, Graph_HourWise.class);
                startActivity(intent);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Graph_Homepage.this, Graph_Monthly.class);
                startActivity(intent);
            }
        });
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Graph_Homepage.this, Graph_Daily.class);
                startActivity(intent);
            }
        });
    }

}