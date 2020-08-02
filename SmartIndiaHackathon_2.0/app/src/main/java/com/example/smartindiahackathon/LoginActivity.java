package com.example.smartindiahackathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    EditText email, password;
    ImageButton login;
    FirebaseAuth firebaseAuth;
    String e_mail, pass;
    String name, airportName, thumbnailImageURL;
    DatabaseReference usersDatabase;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.btnLogin);

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Airports").child("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e_mail = email.getText().toString().trim();
                pass = password.getText().toString().trim();

                if (e_mail.equals("")) {
                    email.requestFocus();
                    email.setError("Please enter an email id");
                } else if (pass.equals("")) {
                    password.requestFocus();
                    password.setError("Please enter the password");
                } else {
                    firebaseAuth.signInWithEmailAndPassword(e_mail, pass)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                    /*Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    updateUI(user);*/
                                        checkEmailVerification();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        /*Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);*/
                                        Toast.makeText(LoginActivity.this, "Invalid credentials, please try again.", Toast.LENGTH_LONG).show();
                                        email.setText("");
                                        password.setText("");
                                    }

                                }
                            });
                }
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String uid = user.getUid();
        Boolean emailFlag = user.isEmailVerified();

        if (emailFlag) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Airports").child("Users").child(uid);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child("name").getValue().toString();
                    airportName = dataSnapshot.child("airport").getValue().toString();
                    thumbnailImageURL = dataSnapshot.child("thumbnail_image_url").getValue().toString();
                    SaveSharedPreferences.setUserName(LoginActivity.this, name);
                    SaveSharedPreferences.setAirportName(LoginActivity.this, airportName);
                    SaveSharedPreferences.setThubmbnailUrl(LoginActivity.this, thumbnailImageURL);
                    Log.d(TAG + "AIRPORT", airportName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            //Get device Token
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    Log.d(TAG, "Token : " + token);
                    SaveSharedPreferences.setDeviceToken(LoginActivity.this, token);
                    // send it to server
                    usersDatabase.child(uid).child("device_token").setValue(token).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images").child(uid + "_profile_image.jpg");

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri downloadUrl = uri;
                                    Context ctx = LoginActivity.this;
                                    try {
                                         bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), uri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    SaveSharedPreferences.setUserEmail(LoginActivity.this, e_mail);

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                                    byte[] b = baos.toByteArray();

                                    String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                                    SaveSharedPreferences.setUserProfile(LoginActivity.this, encoded);

                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, Homepage.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            });
                        }
                    });
                }
            });


        } else {
            Toast.makeText(this, "Please verify your email first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
