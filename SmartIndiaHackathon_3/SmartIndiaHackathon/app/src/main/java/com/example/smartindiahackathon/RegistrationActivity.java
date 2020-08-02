package com.example.smartindiahackathon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class RegistrationActivity extends AppCompatActivity {
    EditText eMail, password, confirmPassword, fullName;
    AutoCompleteTextView tv_airportName;
    CircleImageView profileImage;
    Button register;
    private FirebaseAuth firebaseAuth;
    String email, Password, confPassword, fname, airportName;
    DatabaseReference database;
    private final static int GALLERY_PICK = 1;
    private StorageReference imageStorage;
    Uri profileImageUri;
    File thumbnailFile;
    Bitmap thumbnailBitmap;
    ByteArrayOutputStream baos;
    byte[] thumbnailByte;

    String uid;
    String imageName =  "default_profile_image.jpg";
    String thumbnailName =  "default_profile_image.jpg";
    String profileImageURL = "https://firebasestorage.googleapis.com/v0/b/smartindia-hackathon-e92f1.appspot.com/o/profile_images%2Fdefault_profile_image.jpg?alt=media&token=3c1573cf-8d3f-43c6-99af-2ce518ba38c0";
    String thumbnailImageURL = "https://firebasestorage.googleapis.com/v0/b/smartindia-hackathon-e92f1.appspot.com/o/profile_images%2Fdefault_profile_image.jpg?alt=media&token=3c1573cf-8d3f-43c6-99af-2ce518ba38c0";
    private static final String TAG = "RegistrationActivity";

    private static final List AIRPORTS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        eMail = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.c_password);
        register = findViewById(R.id.regis);
        fullName = findViewById(R.id.Username);
        profileImage = findViewById(R.id.profile_pic);
        tv_airportName = findViewById(R.id.airport);

        firebaseAuth = FirebaseAuth.getInstance();
        imageStorage = FirebaseStorage.getInstance().getReference();

        AIRPORTS.removeAll(AIRPORTS);

        AIRPORTS.add("Agartala");
        AIRPORTS.add("Agatti");
        AIRPORTS.add("Agra");
        AIRPORTS.add("Ahmedabad");
        AIRPORTS.add("Akola");
        AIRPORTS.add("Allahabad");
        AIRPORTS.add("Along");
        AIRPORTS.add("Amritsar");
        AIRPORTS.add("Asansol");
        AIRPORTS.add("Aurangabad");
        AIRPORTS.add("Bagdogra");
        AIRPORTS.add("Balurghat");
        AIRPORTS.add("Bathinda");
        AIRPORTS.add("Behala");
        AIRPORTS.add("Belgaum");
        AIRPORTS.add("Bellary");
        AIRPORTS.add("Bengaluru");
        AIRPORTS.add("Bhavnagar");
        AIRPORTS.add("Bhopal");
        AIRPORTS.add("Bhubaneswar");
        AIRPORTS.add("Bhuj");
        AIRPORTS.add("Bikaner");
        AIRPORTS.add("Bilaspur");
        AIRPORTS.add("Calicut");
        AIRPORTS.add("CarNicobar");
        AIRPORTS.add("Chakulia");
        AIRPORTS.add("Chandigarh");
        AIRPORTS.add("Chennai");
        AIRPORTS.add("Cochin");
        AIRPORTS.add("Coimbatore");
        AIRPORTS.add("CoochBehar");
        AIRPORTS.add("Cuddapah");
        AIRPORTS.add("Daman");
        AIRPORTS.add("Daporizo");
        AIRPORTS.add("Dehradun");
        AIRPORTS.add("Dibrugarh");
        AIRPORTS.add("Dimapur");
        AIRPORTS.add("Diu");
        AIRPORTS.add("Donakonda");
        AIRPORTS.add("Gandhinagar");
        AIRPORTS.add("Gaya");
        AIRPORTS.add("Goa");
        AIRPORTS.add("Gondia");
        AIRPORTS.add("Gorakhpur");
        AIRPORTS.add("Guwahati");
        AIRPORTS.add("Gwalior");
        AIRPORTS.add("Hadapsar");
        AIRPORTS.add("Hassan");
        AIRPORTS.add("Hindon");
        AIRPORTS.add("Hubli");
        AIRPORTS.add("Hyderabad");
        AIRPORTS.add("Imphal");
        AIRPORTS.add("Indore");
        AIRPORTS.add("Jabalpur");
        AIRPORTS.add("Jaipur");
        AIRPORTS.add("Jaisalmer");
        AIRPORTS.add("Jalgaon");
        AIRPORTS.add("Jammu");
        AIRPORTS.add("Jamnagar");
        AIRPORTS.add("Jamshedpur");
        AIRPORTS.add("Jharsuguda");
        AIRPORTS.add("Jodhpur");
        AIRPORTS.add("Jogbani");
        AIRPORTS.add("Jorhat");
        AIRPORTS.add("Juhu");
        AIRPORTS.add("Kailashahr");
        AIRPORTS.add("Kamalpur");
        AIRPORTS.add("Kandla");
        AIRPORTS.add("Kangra-Gaggal");
        AIRPORTS.add("Kanpur-Cantt");
        AIRPORTS.add("Kanpur-Civil");
        AIRPORTS.add("Keshod");
        AIRPORTS.add("Khajuraho");
        AIRPORTS.add("Khandwa");
        AIRPORTS.add("Khowai");
        AIRPORTS.add("Kishangarh");
        AIRPORTS.add("Kolhapur");
        AIRPORTS.add("Kolkata");
        AIRPORTS.add("Kota");
        AIRPORTS.add("Kullu");
        AIRPORTS.add("Lalitpur");
        AIRPORTS.add("Leh");
        AIRPORTS.add("Lengpui");
        AIRPORTS.add("Lilabari");
        AIRPORTS.add("Lucknow");
        AIRPORTS.add("Ludhiana");
        AIRPORTS.add("Madurai");
        AIRPORTS.add("Malda");
        AIRPORTS.add("Mangalore");
        AIRPORTS.add("Muzaffarpur");
        AIRPORTS.add("Mysore");
        AIRPORTS.add("Nadirgul");
        AIRPORTS.add("Nagpur");
        AIRPORTS.add("Nanded");
        AIRPORTS.add("Pakyong");
        AIRPORTS.add("Palanpur");
        AIRPORTS.add("Pantnagar");
        AIRPORTS.add("Passighat");
        AIRPORTS.add("Pathankot");
        AIRPORTS.add("Patna");
        AIRPORTS.add("Porbander");
        AIRPORTS.add("Portblair");
        AIRPORTS.add("Puducherry");
        AIRPORTS.add("Pune");
        AIRPORTS.add("Purnea");
        AIRPORTS.add("Puttaparthi");
        AIRPORTS.add("Raipur");
        AIRPORTS.add("Rajahmundry");
        AIRPORTS.add("Rajkot");
        AIRPORTS.add("Ranchi");
        AIRPORTS.add("Ratnagiri");
        AIRPORTS.add("Raxaul");
        AIRPORTS.add("Rupsi");
        AIRPORTS.add("Safdarjung");
        AIRPORTS.add("Salem");
        AIRPORTS.add("Satna");
        AIRPORTS.add("Shella");
        AIRPORTS.add("Shillong");
        AIRPORTS.add("Shimla");
        AIRPORTS.add("Silchar");
        AIRPORTS.add("Solapur");
        AIRPORTS.add("Srinagar");
        AIRPORTS.add("Surat");
        AIRPORTS.add("Surendranagar");
        AIRPORTS.add("Tezpur");
        AIRPORTS.add("Tezu");
        AIRPORTS.add("Thanjavur");
        AIRPORTS.add("Tiruchirapalli");
        AIRPORTS.add("Tirupati");
        AIRPORTS.add("Trivandrum");
        AIRPORTS.add("Tuticorin");
        AIRPORTS.add("Udaipur");
        AIRPORTS.add("Vadodara");
        AIRPORTS.add("Varanasi");
        AIRPORTS.add("Vellore");
        AIRPORTS.add("Vijayawada");
        AIRPORTS.add("Visakhapatnam");
        AIRPORTS.add("Warrangal");
        AIRPORTS.add("Zero");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AIRPORTS);
        tv_airportName.setAdapter(adapter);

        tv_airportName.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                tv_airportName.showDropDown();
                return false;
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = fullName.getText().toString().trim();
                email = eMail.getText().toString().trim();
                Password = password.getText().toString().trim();
                confPassword = confirmPassword.getText().toString().trim();
                airportName = tv_airportName.getText().toString().trim();

                if (fname.equals("")) {
                    fullName.setError("Please enter your name");
                } else if (email.equals("")) {
                    eMail.requestFocus();
                    eMail.setError("Enter email");
                } else if (!email.contains("@nirmauni.ac.in")) {
                    eMail.setText("");
                    Toast.makeText(RegistrationActivity.this, "Please enter your nirma university email.", Toast.LENGTH_LONG).show();
                } else if (Password.equals("")) {
                    password.requestFocus();
                    password.setError("Please enter a password");
                } else if (Password.length() <= 6) {
                    Toast.makeText(RegistrationActivity.this, "Password minimum length is 6 characters.", Toast.LENGTH_SHORT).show();
                } else if (confPassword.equals("")) {
                    confirmPassword.requestFocus();
                    confirmPassword.setError("Please confirm the password");
                } else if (!Password.equals(confPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Passwords don't match, please try again.", Toast.LENGTH_LONG).show();
                    password.setText("");
                    confirmPassword.setText("");
                } else if(!AIRPORTS.contains(airportName)) {
                    tv_airportName.setError("Please select an Airport under AAI");
                } else if(airportName.equals("")) {
                    tv_airportName.setError("Please select and Airport");
                } else {
                    if(profileImageUri != null){
                        firebaseAuth.createUserWithEmailAndPassword(email, Password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        assert user != null;
                                        uid = user.getUid();

                                        imageName = uid + "_profile_image.jpg";
                                        thumbnailName = uid + "_thumbnail_image.jpg";
                                        final StorageReference filePath = imageStorage.child("profile_images").child(imageName);
                                        filePath.putFile(profileImageUri)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                final Uri downloadUrl = uri;
                                                                profileImageURL = downloadUrl.toString();
                                                                final StorageReference thumbnailFilepath = imageStorage.child("profile_images").child(thumbnailName);
                                                                UploadTask uploadTask = thumbnailFilepath.putBytes(thumbnailByte);
                                                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                        if(task.isSuccessful()){
                                                                            thumbnailFilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                @Override
                                                                                public void onSuccess(Uri uri) {
                                                                                    final Uri downloadUrl2 = uri;
                                                                                    thumbnailImageURL = downloadUrl2.toString();
                                                                                    addUser();
                                                                                }
                                                                            })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            imageName =  "default_profile_image.jpg";
                                                                                            thumbnailName =  "default_profile_image.jpg";
                                                                                            addUser();
                                                                                            Toast.makeText(RegistrationActivity.this, "Your account has been created but couldn't upload profile pic. You can re-upload it from the account settings", Toast.LENGTH_LONG).show();
                                                                                            Log.d(TAG, "onFailure: Thumbnail URL failure");
                                                                                        }
                                                                                    });

                                                                        }
                                                                        else {
                                                                            imageName =  "default_profile_image.jpg";
                                                                            thumbnailName =  "default_profile_image.jpg";
                                                                            addUser();
                                                                            Toast.makeText(RegistrationActivity.this, "Your account has been created but couldn't upload profile pic. You can re-upload it from the account settings", Toast.LENGTH_LONG).show();
                                                                            Log.d(TAG, "onComplete: Thumbnail upload failure");
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                imageName =  "default_profile_image.jpg";
                                                                thumbnailName =  "default_profile_image.jpg";
                                                                addUser();
                                                                Toast.makeText(RegistrationActivity.this, "Your account has been created but couldn't upload profile pic. You can re-upload it from the account settings", Toast.LENGTH_LONG).show();
                                                                Log.d(TAG, "onFailure: Profile URL failure");
                                                            }
                                                        });

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        imageName =  "default_profile_image.jpg";
                                                        thumbnailName =  "default_profile_image.jpg";
                                                        addUser();
                                                        Toast.makeText(RegistrationActivity.this, "Your account has been created but couldn't upload profile pic. You can re-upload it from the account settings", Toast.LENGTH_LONG).show();
                                                        Log.d(TAG, "onFailure: Profile upload failure");
                                                    }
                                                });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegistrationActivity.this, "Authentication unsuccessful.", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setMessage("Do you want to continue without a profile picture?");
                        builder.setTitle("Alert !");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                firebaseAuth.createUserWithEmailAndPassword(email, Password)
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                assert user != null;
                                                uid = user.getUid();
                                                addUser();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegistrationActivity.this, "Authentication unsuccessful 2.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });

                        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImageUri = result.getUri();
                profileImage.setImageURI(profileImageUri);
                thumbnailFile = new File(profileImageUri.getPath());
                try {
                    thumbnailBitmap = new Compressor(RegistrationActivity.this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(thumbnailFile);

                    baos = new ByteArrayOutputStream();
                    thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumbnailByte = baos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void sendEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrationActivity.this, "Successfully registered, verification email sent", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Could not send verification mail, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void addUser(){
        //Adding username:
        database = FirebaseDatabase.getInstance().getReference().child("Airports").child("Users").child(uid);
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name", fname);
        userMap.put("profile_image", imageName);
        userMap.put("thumbnail_image", thumbnailName);
        userMap.put("profile_image_url", profileImageURL);
        userMap.put("thumbnail_image_url", thumbnailImageURL);
        userMap.put("airport", airportName);
        database.setValue(userMap);

        sendEmailVerification();
    }
}