package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button reportButton;
    private Button showMapsButton; // Add reference to the Show Maps button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the buttons by their IDs
        reportButton = findViewById(R.id.report_issues_button);
        showMapsButton = findViewById(R.id.show_maps_button); // Find the Show Maps button

        // Set OnClickListener to the reportButton
        reportButton.setOnClickListener(v -> {
            // Create an Intent to start the ReportIssueActivity
            Intent intent = new Intent(MainActivity.this, ReportIssueActivity.class);
            // Start the activity
            startActivity(intent);
        });

        // Set OnClickListener to the showMapsButton
        showMapsButton.setOnClickListener(v -> {
            // Create an Intent to start the MapsActivity
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            // Start the activity
            startActivity(intent);
        });
    }
}
