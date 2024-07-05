package com.example.satyavedh;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Terms extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn);
        bottomNavigationView.setSelectedItemId(R.id.bottom_terms);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_terms) {
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
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
                return true;
            }
            return false;
        });

    }
}