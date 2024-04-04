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

    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        issueTitle = findViewById(R.id.issueTitleEditText);
        issueDescription = findViewById(R.id.issueDescriptionEditText);
        latitudeTextView = findViewById(R.id.latitudeTextView); // Find the TextViews
        longitudeTextView = findViewById(R.id.longitudeTextView);
        submitButton = findViewById(R.id.submitButton);

        getLocation();

        submitButton.setOnClickListener(v -> {
            String title = issueTitle.getText().toString();
            String description = issueDescription.getText().toString();
            String imageUrl = ""; // You need to implement image capturing and URL generation here

            // Validate if title and description are not empty
            if (!title.isEmpty() && !description.isEmpty()) {
                // Create an intent to pass data back to MainActivity
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("imageUrl", imageUrl);

                // Set the result and finish the activity
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(ReportIssueActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




