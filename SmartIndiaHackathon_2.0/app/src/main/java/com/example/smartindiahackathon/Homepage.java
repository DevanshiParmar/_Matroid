package com.example.smartindiahackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        CardView tasks, graphs;
        TextView name, email;
        CircleImageView profile_image;
        String n, e;

        tasks = findViewById(R.id.tasksCardBtn);
        graphs = findViewById(R.id.graphCardBtn);
        profile_image = findViewById(R.id.profileImage);

        name = findViewById(R.id.nameTv);
        email = findViewById(R.id.emailTv);

        n = SaveSharedPreferences.getUserName(this);
        e = SaveSharedPreferences.getUserEmail(this);

        byte[] imageAsBytes = Base64.decode(SaveSharedPreferences.getUserProfile(this).getBytes(), Base64.DEFAULT);

        profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        name.setText(n);
        email.setText(e);

        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Tasks_All_Activity.class);
                startActivity(intent);
            }
        });

        graphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Graph_Homepage.class);
                startActivity(intent);
            }
        });
    }
}