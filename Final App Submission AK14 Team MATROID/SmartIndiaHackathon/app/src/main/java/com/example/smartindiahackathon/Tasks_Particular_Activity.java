package com.example.smartindiahackathon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jediburrell.customfab.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

public class Tasks_Particular_Activity extends AppCompatActivity {
    private static final String TAG = "Tasks_Particular_Activi";
    String task_id, task_publisher, task_title, task_desc, uid;
    Toolbar toolBar;
    //TextView title;
    TextView desc;
    EditText comment_box;
    ImageButton post, attachment;
    DatabaseReference mRootRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    RecyclerView commentsRecyclerview;
    private final List<Comments_Data> commentsList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private CommentsAdapter mAdapter;
    private static final int GALLERY_PICK = 1;
    int i = 0;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_particular);

        Intent intent = getIntent();
        task_id = intent.getStringExtra("task_id");
        task_publisher = intent.getStringExtra("publisher");
        task_title = intent.getStringExtra("title");
        task_desc = intent.getStringExtra("desc");

        toolBar = findViewById(R.id.tbar);
        toolBar.setTitle(task_title);
        setSupportActionBar(toolBar);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        comment_box = findViewById(R.id.Comment);
        post = findViewById(R.id.postComment);
        attachment = findViewById(R.id.postImage);

        //title = findViewById(R.id.Title);
        desc = findViewById(R.id.Desc);

        //title.setText(task_title);
        desc.setText(task_desc);

        mAdapter = new CommentsAdapter(this, commentsList);

        commentsRecyclerview = findViewById(R.id.comments_recycler);
        mLinearLayout = new LinearLayoutManager(this);
        mLinearLayout.setReverseLayout(true);
        mLinearLayout.setStackFromEnd(true);
        commentsRecyclerview.setHasFixedSize(true);
        commentsRecyclerview.setLayoutManager(mLinearLayout);
        commentsRecyclerview.setAdapter(mAdapter);
        mRootRef.child("Airports").child("Task").child(task_id).child("Comments").keepSynced(true);
        mRootRef.child("Airports").child("Users").keepSynced(true);

        loadComments();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

    }

    private void loadComments() {
        mRootRef.child("Airports").child("Task").child(task_id).child("Comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                Comments_Data commentsData = dataSnapshot.getValue(Comments_Data.class);
                Log.d(TAG, "onChildAdded: THIS IS THE DATA          " + dataSnapshot);
                commentsList.add(commentsData);
                mAdapter.notifyDataSetChanged();
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

    public void postComment(){
        String comment = comment_box.getText().toString();

        if(!TextUtils.isEmpty(comment)){
            DatabaseReference sendComment = mRootRef.child("Airports").child("Task").child(task_id).child("Comments").push();
            String push_Id = sendComment.getKey();
            comment_box.setText("");
            DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm a");

            Map commentMap = new HashMap();
            commentMap.put("type", "text");
            commentMap.put("commentedBy", uid);
            commentMap.put("name_commentor", SaveSharedPreferences.getUserName(this));
            commentMap.put("thumbnail_image_url", SaveSharedPreferences.getThubmbnailUrl(this));
            commentMap.put("comment", comment);
            commentMap.put("time", df.format(Calendar.getInstance().getTime()));
            Log.d(TAG, "postComment: TIMESTAMP" + ServerValue.TIMESTAMP);

            sendComment.updateChildren(commentMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError != null){
                        Log.d("Comment Log: ",databaseError.getMessage());
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
            imageUri = data.getData();

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialoglayout);
            ImageView imgView=dialog.findViewById(R.id.dialogImage);
            imgView.setImageURI(imageUri);
            FloatingActionButton sendImage = dialog.findViewById(R.id.img);
            dialog.show();

            sendImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatabaseReference sendComment = mRootRef.child("Airports").child("Task").child(task_id).child("Comments").push();
                    String push_Id = sendComment.getKey();
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("message_images").child(push_Id + ".jpg");
                    storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri downloadUrl = uri;
                                    String download_url = downloadUrl.toString();

                                    comment_box.setText("");
                                    DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm a");

                                    Map commentMap = new HashMap();
                                    commentMap.put("type", "image");
                                    commentMap.put("commentedBy", uid);
                                    commentMap.put("name_commentor", SaveSharedPreferences.getUserName(Tasks_Particular_Activity.this));
                                    commentMap.put("thumbnail_image_url", SaveSharedPreferences.getThubmbnailUrl(Tasks_Particular_Activity.this));
                                    commentMap.put("comment", download_url);
                                    commentMap.put("time", df.format(Calendar.getInstance().getTime()));
                                    Log.d(TAG, "postComment: TIMESTAMP" + ServerValue.TIMESTAMP);

                                    sendComment.updateChildren(commentMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if(databaseError != null){
                                                Log.d("Comment Log: ",databaseError.getMessage());
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    dialog.dismiss();
                }
            });
        }
    }
}