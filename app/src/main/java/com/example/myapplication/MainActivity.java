package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button reportButton;
    private LinearLayout buttonContainer;
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
        buttonContainer = findViewById((R.id.buttonContainer));

        fetchAndDisplayButtons();
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

    private void fetchAndDisplayButtons() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Issue");
        Query query = databaseReference.orderByChild("order");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Model model = snapshot.getValue(Model.class);
                    String key = snapshot.getKey(); // This is the automatically generated ID by Firebase

                    Button button = new Button(MainActivity.this);
                    button.setText(model.getTitle() + " (Votes: " + model.getVotes()+ ")");
                    button.setOnClickListener(v -> {
                        // Implement navigation logic here
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("ID", key); // Pass the Firebase-generated ID to the detail activity
                        startActivity(intent);
                    });

                    // Add the button to the LinearLayout within the ScrollView
                    buttonContainer.addView(button);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}
