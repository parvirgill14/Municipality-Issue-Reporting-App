package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
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
    private Button upvoteButton, downvoteButton, finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("admin");
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Check if the current user's ID exists in the admin node
        adminRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The user ID exists in the admin node, so this user is an admin
                    setupAdminUI();
                } else {
                    // The user ID does not exist in the admin node, so this user is not an admin
                    setupRegularUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors, such as permission issues
            }
        });
    }

    private void setupAdminUI(){
        setContentView(R.layout.activity_detail_admin);
        setupUI();
        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getIntent().getStringExtra("ID");
                DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference()
                        .child("Issue").child(id);

                // Update the isActive field for this issue
                issueRef.child("active").setValue(false)
                        .addOnSuccessListener(aVoid -> {
                            // Successfully updated isActive in Firebase
                            Toast.makeText(DetailActivity.this, "Issue marked as finished.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DetailActivity.this, MainActivity.class));
                        })
                        .addOnFailureListener(e -> {
                            // Failed to update isActive
                            Toast.makeText(DetailActivity.this, "Failed to update issue.", Toast.LENGTH_SHORT).show();
                        });
            }

        });

    }

    private void setupRegularUI(){
        setContentView(R.layout.activity_detail);
        setupUI();
    }

    private void setupUI(){
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
                    Picasso.get().load(model.getImageURL()).into(image);
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
                processVote(id, true); // true for upvote
            }
        });

        downvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processVote(id, false); // false for downvote
            }
        });
    };



    private void processVote(final String issueId, final boolean upVote) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user ID
        final DatabaseReference issueRef = FirebaseDatabase.getInstance().getReference().child("Issue").child(issueId);
        final DatabaseReference userVoteRef = issueRef.child("userVotes").child(userId);

        userVoteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // User hasn't voted, allow voting
                    issueRef.runTransaction(new Transaction.Handler() {
                        @NonNull
                        @Override
                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                            Model issue = mutableData.getValue(Model.class);
                            if (issue == null) {
                                return Transaction.success(mutableData);
                            }
                            if (upVote) {
                                issue.setVotes(issue.getVotes() + 1);
                            } else {
                                issue.setVotes(issue.getVotes() - 1);
                            }
                            mutableData.setValue(issue);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                            if (committed) {
                                userVoteRef.setValue(true); // Mark this user as having voted
                                votes.setText(String.valueOf(dataSnapshot.getValue(Model.class).getVotes()));
                            }
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(DetailActivity.this, "You have already voted on this issue.", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}
