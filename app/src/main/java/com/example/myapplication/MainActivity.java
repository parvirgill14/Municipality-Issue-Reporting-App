package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button reportButton;
    private static final int REPORT_ISSUE_REQUEST = 1001;



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

        // Find the button by its ID
        reportButton = findViewById(R.id.report_issues_button);

        // Set OnClickListener to the button
        reportButton.setOnClickListener(v -> {
            // Create an Intent to start the ReportIssueActivity
            Intent intent = new Intent(MainActivity.this, ReportIssueActivity.class);
            // Start the activity
            startActivityForResult(intent, REPORT_ISSUE_REQUEST);
        });
    }
    // Handle the result from ReportIssueActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REPORT_ISSUE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                double latitude = data.getDoubleExtra("latitude", 0.0);
                double longitude = data.getDoubleExtra("longitude", 0.0);
                String imageUrl = data.getStringExtra("imageUrl");

                // Do whatever you want with the received data
                // For example, you can display it in a TextView
                TextView resultTextView = findViewById(R.id.resultTextView);
                resultTextView.setText("Title: " + title + "\n"
                        + "Description: " + description + "\n"
                        + "Latitude: " + latitude + "\n"
                        + "Longitude: " + longitude + "\n"
                        + "Image URL: " + imageUrl);
            }
        }
    }
}

