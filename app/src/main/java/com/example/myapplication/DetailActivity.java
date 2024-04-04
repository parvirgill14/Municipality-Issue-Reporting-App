package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
public class DetailActivity extends AppCompatActivity {
    private TextView title, desc, votes;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize TextViews
        title = findViewById(R.id.show_title);
        desc = findViewById(R.id.show_Desc);
        votes = findViewById(R.id.show_Votes);
        image = findViewById(R.id.imageView2);

        // Get the ID passed from the previous activity
        String id = getIntent().getStringExtra("ID");

        // Reference to the specific item in the database
        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference().child("Issue").child(id);

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Model model = dataSnapshot.getValue(Model.class);
                    // Assuming MyModel includes name, description, and votes fields
                    title.setText(model.getTitle());
                    desc.setText(model.getDescription());
                    votes.setText(String.valueOf(model.getVotes())); // votes is an int
                    Picasso.get()
                            .load(model.getImageURL())
                            .into(image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
}
