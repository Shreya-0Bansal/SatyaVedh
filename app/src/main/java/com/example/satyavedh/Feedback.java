package com.example.satyavedh;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Feedback extends AppCompatActivity implements View.OnClickListener {
    EditText feedbackEditText, anonymousEmailEditText, anonymousPhoneEditText;
    Button submitButton;
    SQLiteDatabase obj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn);
        bottomNavigationView.setSelectedItemId(R.id.bottom_terms);
        feedbackEditText = findViewById(R.id.feedback_edittext);
        anonymousEmailEditText = findViewById(R.id.anonymous_email_edittext);
        anonymousPhoneEditText = findViewById(R.id.anonymous_phone_edittext);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        obj = openOrCreateDatabase("vedh", MODE_PRIVATE, null);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_terms) {
                startActivity(new Intent(getApplicationContext(), Terms.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
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

    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            obj.execSQL("CREATE TABLE IF NOT EXISTS feedback (Feedback VARCHAR NOT NULL, Email VARCHAR, Phone VARCHAR)");
            String feedback = feedbackEditText.getText().toString().trim();
            String email = anonymousEmailEditText.getText().toString().trim();
            String phone = anonymousPhoneEditText.getText().toString().trim();

            try {
                String Insert = "INSERT INTO feedback VALUES('" + feedback + "', '" + email + "', '" + phone + "');";
                obj.execSQL(Insert);
                saveDataAndShowAlert();
            } catch (SQLiteException e) {
                Toast.makeText(this, "Error submitting Feedback: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void saveDataAndShowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("Thank you for your valuable feedback! We appreciate your input and look forward to serving you better in the future.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onDestroy() {
        obj.close();
        super.onDestroy();
    }
}