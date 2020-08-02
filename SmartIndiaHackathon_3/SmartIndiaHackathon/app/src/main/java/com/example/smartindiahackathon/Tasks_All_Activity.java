package com.example.smartindiahackathon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

//Firebase imports:


public class Tasks_All_Activity extends AppCompatActivity {
    private static final String TAG = "Tasks_All_Activity";
    private static final String TAG2 = "Task_Key";
    public static List<String> task_keys = new ArrayList<String>();
    Toolbar toolBar;
    FloatingActionButton newTask;
    FirebaseInstanceId firebaseInstanceId;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference mRef, tasksDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String name = SaveSharedPreferences.getUserName(this);

        toolBar = findViewById(R.id.tbar);
        toolBar.setTitle("Hello, " + name);
        setSupportActionBar(toolBar);

        newTask = findViewById(R.id.newtask);

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        FirebaseAuth firebase = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.child("Airports").child("Users").child(user.getUid()).child("Tasks_Id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Keysssssss" + childDataSnapshot.getValue());
                    task_keys.add(childDataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        tasksDatabase = FirebaseDatabase.getInstance().getReference().child("Airports").child("Task");
        tasksDatabase.keepSynced(true);

        FirebaseRecyclerOptions<Tasks_Data> options =
                new FirebaseRecyclerOptions.Builder<Tasks_Data>()
                        .setQuery(tasksDatabase.orderByChild(user.getUid()).equalTo(user.getUid()), Tasks_Data.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Tasks_Data, TViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TViewHolder holder, int position, @NonNull Tasks_Data model) {
                final String title = model.getTitle();
                final String desc = model.getDesc();
                final String publisher = model.getPublisher();
                holder.taskTitle.setText(model.getTitle());
                holder.taskDesc.setText(model.getDesc());

                final String task_id = getRef(position).getKey();

                holder.tView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent particularTask = new Intent(Tasks_All_Activity.this, Tasks_Particular_Activity.class);
                        particularTask.putExtra("task_id", task_id);
                        particularTask.putExtra("title", title);
                        particularTask.putExtra("desc", desc);
                        particularTask.putExtra("publisher", publisher);
                        startActivity(particularTask);
                    }
                });
            }

            @NonNull
            @Override
            public TViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tasks_recycler_layout, parent, false);
                return new TViewHolder(view);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

        newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tasks_All_Activity.this, Tasks_Create.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout: {
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(Tasks_All_Activity.this, MainActivity.class));
                                    finish();
                                } else Log.d(TAG, "onComplete: ", task.getException());
                            }
                        });

                SaveSharedPreferences.setUserName(Tasks_All_Activity.this, "");
                SaveSharedPreferences.setDeviceToken(Tasks_All_Activity.this, "");
            }break;

            case R.id.BarGraph: {
                startActivity(new Intent(Tasks_All_Activity.this, Graph_Homepage.class));
            }break;
/*
            case R.id.: {
                startActivity(new Intent(Tasks_All_Activity.this, Graph_Daily.class));
            }break;

            case R.id.GenerateData: {
                startActivity(new Intent(Tasks_All_Activity.this, GenerateData.class));
            }break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    public class TViewHolder extends RecyclerView.ViewHolder {
        View tView;
        TextView taskTitle, taskDesc;

        public TViewHolder(@NonNull View itemView) {
            super(itemView);
            tView = itemView;
            taskTitle = itemView.findViewById(R.id.task_title);
            taskDesc = itemView.findViewById(R.id.task_desc);

        }
    }

}