package com.example.satyavedh;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    ImageButton b1, b2, b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        b1=findViewById(R.id.submit);
        b2=findViewById(R.id.submit1);
        b3=findViewById(R.id.submit_2);
        // Set onClickListener for Button b1
        b1.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Complaint.class);
            startActivity(intent);
        });

        // Set onClickListener for Button b2
        b2.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Suggestion.class);
            startActivity(intent);
        });

        // Set onClickListener for Button b3
        b3.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Feedback.class);
            startActivity(intent);
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                return true;
            } else if (item.getItemId() == R.id.bottom_about) {
                startActivity(new Intent(getApplicationContext(), About.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_contact) {
                startActivity(new Intent(getApplicationContext(), Contact.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_terms) {
                startActivity(new Intent(getApplicationContext(), Terms.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
                return true;
            }
            return false;
        });

    }
}