package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ReportIssueActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private EditText issueTitle, issueDescription;
    private FusedLocationProviderClient fusedLocationClient;
    private Button uploadBtn;
    private ImageView imageView;
    private ProgressBar progressBar;
    private DatabaseReference root= FirebaseDatabase.getInstance().getReference().child("Image");
    private StorageReference imgref = FirebaseStorage.getInstance().getReference();
    private double latitude, longitude;
    private TextView latitudeTextView, longitudeTextView;
    private Uri imageUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        issueTitle = findViewById(R.id.issueTitleEditText);
        issueDescription = findViewById(R.id.issueDescriptionEditText);
        latitudeTextView = findViewById(R.id.latitudeTextView); // Find the TextViews
        longitudeTextView = findViewById(R.id.longitudeTextView);
        uploadBtn = findViewById(R.id.upload_btn);
        progressBar = findViewById((R.id.progressBar));
        imageView = findViewById(R.id.imageView);

        progressBar.setVisibility(View.INVISIBLE);

        getLocation();

        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(imageUri != null){
                    uploadToFirebase(imageUri);
                }
                else{
                    Toast.makeText(ReportIssueActivity.this, "PLease Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void uploadToFirebase(Uri imageUri){
        StorageReference fileRef = imgref.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Model model = new Model(uri.toString());
                        String modelID = root.push().getKey();
                        root.child(modelID).setValue(model);
                        Toast.makeText(ReportIssueActivity.this,"Uploaded",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ReportIssueActivity.this,"Uploading Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    latitudeTextView.setText("Latitude: " + latitude);
                    longitudeTextView.setText("Longitude: " + longitude);

                }
            });
        }
        else {
            // Request for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            getLocation();
        }
    }

}


