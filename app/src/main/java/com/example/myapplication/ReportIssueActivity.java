package com.example.myapplication;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ReportIssueActivity extends AppCompatActivity {
    private EditText issueTitle, issueDescription;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        issueTitle = findViewById(R.id.issueTitleEditText);
        issueDescription = findViewById(R.id.issueDescriptionEditText);
    }
}
