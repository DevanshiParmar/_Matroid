package com.example.smartindiahackathon;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GenerateData extends AppCompatActivity {
    DatabaseReference myRefEntries, myRefHourly, myRefDaily, myRefMonthly;
    FirebaseDatabase database;
    String path;

    int flag = 0, j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_data);
        String S_entry, S_exit, key, S_rand_min, S_rand_sec, S_exit_rand_min, S_exit_rand_sec;
        int i, max = 58, rand_min, rand_sec, exit_rand_min, exit_rand_sec, min = 0;
        int range = max - min + 1;

        database = FirebaseDatabase.getInstance();
        path = "Ahmedabad/Check_in/N1";
        myRefEntries = database.getReference(path + "/Entries/");

        int j_max = 12, j_min = 11;

        ////////entries

        Calendar calendar_1 = Calendar.getInstance();
        Date time_set = calendar_1.getTime();
        SimpleDateFormat path_df = new SimpleDateFormat("MM-dd", Locale.getDefault());
        time_set = new Date(time_set.getTime());//-3600 * 1000*24);
        final String formattedDate = path_df.format(time_set);

        for (j = j_min; j < j_max; j++) {
            System.out.println("j=" + j);
            i = 0;
            for (; i <= 12; i++) {

                // generate random numbers within 1 to 10
                rand_sec = (int) (Math.random() * range);
                rand_min = (int) (Math.random() * range);
                exit_rand_sec = (int) (Math.random() * range);


                if (rand_min <= 55) {
                    exit_rand_min = rand_min + (int) (Math.random() * 3) + 1;
                } else {
                    exit_rand_min = rand_min + 1 + (int) (Math.random() * (58 - rand_min));
                }

                S_exit_rand_min = Integer.toString(exit_rand_min);
                S_exit_rand_sec = Integer.toString(exit_rand_sec);
                S_rand_min = Integer.toString(rand_min);
                S_rand_sec = Integer.toString(rand_sec);

                if (rand_min < 10) {
                    S_rand_min = "" + forValueLessThanTen(S_rand_min);
                }
                if (rand_sec < 10) {
                    S_rand_sec = "" + forValueLessThanTen(S_rand_sec);
                }
                if (exit_rand_min < 10) {
                    S_exit_rand_min = "" + forValueLessThanTen(S_exit_rand_min);
                }
                if (exit_rand_sec < 10) {
                    S_exit_rand_sec = "" + forValueLessThanTen(S_exit_rand_sec);
                }
                // Output is different everytime this code is executed

                if (j < 10) {
                    S_entry = "2020-" + formattedDate + " " + "0" + j + ":" + S_rand_min + ":" + S_rand_sec;
                    S_exit = "2020-" + formattedDate + " " + "0" + j + ":" + S_exit_rand_min + ":" + S_exit_rand_sec;

                    System.out.println(S_entry + "   " + S_exit);

                    key = GenerateRandomString.randomString(5);
                    myRefEntries.child(formattedDate + "_" + "0" + j).child((S_entry.substring(11)).concat(key)).child("entry_time").setValue(S_entry);
                    myRefEntries.child(formattedDate + "_" + "0" + j).child((S_entry.substring(11).concat(key))).child("exit_time").setValue(S_exit);

                } else {
                    S_entry = "2020-" + formattedDate + " " + j + ":" + S_rand_min + ":" + S_rand_sec;
                    S_exit = "2020-" + formattedDate + " " + j + ":" + S_exit_rand_min + ":" + S_exit_rand_sec;

                    System.out.println(S_entry + "   " + S_exit);

                    key = GenerateRandomString.randomString(5);
                    myRefEntries.child(formattedDate + "_" + j).child((S_entry.substring(11)).concat(key)).child("entry_time").setValue(S_entry);
                    myRefEntries.child(formattedDate + "_" + j).child((S_entry.substring(11)).concat(key)).child("exit_time").setValue(S_exit);

                }
            }
        }

        //////////hourly auto

        int range2 = 200, range3 = 100;
        int footfall, avg_dwell;

        for (j = j_min; j < j_max; j++) {
            System.out.println("j=" + j);

            avg_dwell = 90 + (int) (Math.random() * range2);
            footfall = 200 + (int) (Math.random() * range3);

            if (j < 10) {
                myRefDaily = database.getReference(path + "/Hourly/" + formattedDate + "_0" + j);
                myRefDaily.child("hour").setValue(formattedDate + "_0" + j);
                myRefDaily.child("avg_dwell").setValue("" + avg_dwell);
                myRefDaily.child("footfall").setValue("" + footfall);

            } else {
                myRefDaily = database.getReference(path + "/Hourly/" + formattedDate + "_" + j);
                myRefDaily.child("hour").setValue(formattedDate + "_" + j);
                myRefDaily.child("avg_dwell").setValue("" + avg_dwell);
                myRefDaily.child("footfall").setValue("" + footfall);
                System.out.println("j=" + j + formattedDate);

            }
        }
/////daily

        range2 = 200;
        range3 = 1000;

        for (j = time_set.getDate()-1; j < time_set.getDate(); j++) {
            System.out.println("j=" + j);

            avg_dwell = 90 + (int) (Math.random() * range2);
            footfall = 2000 + (int) (Math.random() * range3);

            if (j < 10) {
                myRefDaily = database.getReference(path + "/Daily/" + formattedDate.substring(0, 3) + "0" + j);
                myRefDaily.child("date").setValue(formattedDate.substring(0, 3) + "0" + j);
                myRefDaily.child("avg_dwell").setValue("" + avg_dwell);
                myRefDaily.child("footfall").setValue("" + footfall);

            } else {
                myRefDaily = database.getReference(path + "/Daily/" + formattedDate.substring(0, 3) + "" + j);
                myRefDaily.child("date").setValue(formattedDate.substring(0, 3) + "" + j);
                myRefDaily.child("avg_dwell").setValue("" + avg_dwell);
                myRefDaily.child("footfall").setValue("" + footfall);
                System.out.println("j=" + j + formattedDate);

            }
        }

        //////montly
/*
        range2 = 200;
        range3 = 20000;
        //        int footfall,avg_dwell;

        myRefMonthly = database.getReference(path + "/Monthly/");
        for (j = 7; j < 8; j++) {
            System.out.println("j=" + j);

            // generate random numbers within 1 to 10
            avg_dwell = 90 + (int) (Math.random() * range2);
            footfall = 60000 + (int) (Math.random() * range3);

            if (j < 10) {
                myRefDaily = database.getReference(path + "/Monthly/" + "0" + j);
                myRefDaily.child("month").setValue("0" + j);
                myRefDaily.child("avg_dwell").setValue("" + avg_dwell);
                myRefDaily.child("footfall").setValue("" + footfall);

            } else {
                myRefDaily = database.getReference(path + "/Monthly/" + "" + j);
                myRefDaily.child("month").setValue("" + j);
                myRefDaily.child("avg_dwell").setValue("" + avg_dwell);
                myRefDaily.child("footfall").setValue("" + footfall);

            }
        }*/
    }

    protected String forValueLessThanTen(String s) {
        String sp = "0" + s;
        System.out.println("s=" + sp);
        return sp;
    }
}