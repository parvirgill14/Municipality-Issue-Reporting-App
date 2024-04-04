package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class ReportIssueActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private EditText issueTitle, issueDescription;
    private FusedLocationProviderClient fusedLocationClient;

    private double latitude, longitude;
    private TextView latitudeTextView, longitudeTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        issueTitle = findViewById(R.id.issueTitleEditText);
        issueDescription = findViewById(R.id.issueDescriptionEditText);
        latitudeTextView = findViewById(R.id.latitudeTextView); // Find the TextViews
        longitudeTextView = findViewById(R.id.longitudeTextView);
        getLocation();

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


