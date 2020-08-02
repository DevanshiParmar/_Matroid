package com.example.smartindiahackathon;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tasks_Create extends AppCompatActivity {

    private static final String TAG = "CreateTask";

    EditText taskTitle, taskDesc;
    Button saveTask;
    Toolbar toolBar;

    String title, desc, publisherName;

    DatabaseReference databaseReference;
    Tasks_Data task;
    DatabaseReference mRootRef;

    ProgressDialog progressDialog;

    ListView listView;
    private UsersAdapter adapter;
    final List<UserModel> users = new ArrayList<>();
    final List<String> finalUsers = new ArrayList<>();

    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        taskTitle = findViewById(R.id.tasktitle);
        taskDesc = findViewById(R.id.taskdesc);
        saveTask = findViewById(R.id.savetask);

        toolBar = findViewById(R.id.tbar);
        toolBar.setTitle("Create task");
        setSupportActionBar(toolBar);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Airports").child("Task");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating task");
        progressDialog.setMessage("Please wait while we upload the task");

        listView = findViewById(R.id.allUsers);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("Airports").child("Users").keepSynced(true);

        adapter = new UsersAdapter(this, users);
        listView.setAdapter(adapter);

        loadUsers();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserModel model = users.get(position);
                Log.d(TAG, "Entered the click");
                Log.d(TAG, "onItemClick: " + (model.isSelectedTo()));

                if (model.isSelectedTo()) {
                    model.setSelectedTo(false);
                    finalUsers.remove(model.getKey());
                    adapter.updateRecordsList(users);
                } else {
                    model.setSelectedTo(true);
                    finalUsers.add(model.getKey());
                    adapter.updateRecordsList(users);
                }

                users.set(position, model);
            }
        });

        saveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = taskTitle.getText().toString();
                desc = taskDesc.getText().toString();

                if (title.equals("")) {
                    Toast.makeText(Tasks_Create.this, "Task Title can not be empty", Toast.LENGTH_SHORT).show();
                } else if (desc.equals("")) {
                    alertMessage();
                } else {
                    uploadTask();
                }
            }
        });

    }

    private void loadUsers() {
        mRootRef.child("Airports").child("Users").orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                userModel.setKey(dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded: " + dataSnapshot);
                String keys = String.valueOf(userModel.getKey());
                Log.d(TAG, "Commentor " + keys);

                //String commentedBy = dataSnapshot.getValue();
                users.add(userModel);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void alertMessage() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you want to upload task without any description?");
        dialog.setTitle("Alert");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        uploadTask();
                    }
                });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void uploadTask() {

        progressDialog.show();
        task = new Tasks_Data();
        publisherName = SaveSharedPreferences.getUserName(this);
        task.setPublisher(publisherName.trim());
        task.setTitle(title.trim());
        task.setDesc((desc.trim()));
        task.setMembersId(finalUsers);
        final String key = databaseReference.push().getKey();

        Log.d(TAG, "uploadTask: KEEEEEEEEEEEYYYYYYY: " + key);

        databaseReference.child(key).setValue(task)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            int listSize = finalUsers.size();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Airports").child("Users");

                            for (int i = 0; i < listSize; i++) {
                                ref.child(finalUsers.get(i)).child("Tasks_Id").child(key).setValue(key);
                                databaseReference.child(key).child(finalUsers.get(i)).setValue(finalUsers.get(i));
                            }
                            progressDialog.dismiss();
                            Toast.makeText(Tasks_Create.this, "Task successfully uploaded", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            progressDialog.hide();
                            Log.d(TAG, "onComplete: ", task.getException());
                            Toast.makeText(Tasks_Create.this, "Couldn't upload the task, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}