package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView title, desc, votes;
    private ImageView image;
    private Button upvoteButton, downvoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize TextViews and ImageView
        title = findViewById(R.id.show_title);
        desc = findViewById(R.id.show_Desc);
        votes = findViewById(R.id.show_Votes);
        image = findViewById(R.id.imageView2);

        // Initialize Buttons
        upvoteButton = findViewById(R.id.upvoteButton);
        downvoteButton = findViewById(R.id.downvoteButton);

        // Get the ID passed from the previous activity
        String id = getIntent().getStringExtra("ID");

        // Reference to the specific item in the database
        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference().child("Issue").child(id);

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Model model = dataSnapshot.getValue(Model.class);
                    // Assuming Model includes title, description, votes, and imageURL fields
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

        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateVoteCount(id, true); // true for upvote
            }
        });

        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateVoteCount(id, false); // false for downvote
            }
        });
    }

    private void updateVoteCount(String issueId, boolean upvote) {
        DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference().child("Issue").child(issueId);
        issueRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Model issue = mutableData.getValue(Model.class);
                if (issue == null) {
                    return Transaction.success(mutableData);
                }

                if (upvote) {
                    issue.setVotes(issue.getVotes() + 1);
                } else {
                    issue.setVotes(issue.getVotes() - 1);
                }

                mutableData.setValue(issue);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println("Firebase voting transaction failed.");
                } else {
                    System.out.println("Firebase voting transaction succeeded.");
                    Model updatedIssue = dataSnapshot.getValue(Model.class);
                    if (updatedIssue != null) {
                        votes.setText(String.valueOf(updatedIssue.getVotes()));
                    }
                }
            }
        });
    }
}
