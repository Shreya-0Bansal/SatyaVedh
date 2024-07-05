package com.example.satyavedh;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Contact extends AppCompatActivity implements View.OnClickListener {
    EditText anonymousEmailEditText, anonymousPhoneEditText;
    Button submitButton;
    SQLiteDatabase obj;
    ImageButton instagramButton, whatsappButton , linkedinButton ,telegramButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn);
        bottomNavigationView.setSelectedItemId(R.id.bottom_contact);
        anonymousEmailEditText = findViewById(R.id.anonymous_email_edittext);
        anonymousPhoneEditText = findViewById(R.id.anonymous_phone_edittext);
        submitButton=findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        instagramButton = findViewById(R.id.instagram_button);
        instagramButton.setOnClickListener(this);
        whatsappButton = findViewById(R.id.whatsapp_button);
        whatsappButton.setOnClickListener(this);
        linkedinButton = findViewById(R.id.linkedin_button);
        linkedinButton.setOnClickListener(this);
        telegramButton = findViewById(R.id.telegram_button);
        telegramButton.setOnClickListener(this);

        obj = openOrCreateDatabase("vedh", MODE_PRIVATE, null);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_contact) {
                return true;
            } else if (item.getItemId() == R.id.bottom_about) {
                startActivity(new Intent(getApplicationContext(), About.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
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

    @Override
    public void onClick(View v) {
        if (v==submitButton) {
            obj.execSQL("CREATE TABLE IF NOT EXISTS contact (Email VARCHAR, Phone VARCHAR)");
            String email = anonymousEmailEditText.getText().toString().trim();
            String phone = anonymousPhoneEditText.getText().toString().trim();

            try {
                String Insert = "INSERT INTO contact VALUES('" + email + "', '" + phone + "');";
                obj.execSQL(Insert);
                saveDataAndShowAlert();

            } catch (SQLiteException e) {
                Toast.makeText(this, "Error establishing Contact: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.instagram_button) {
            // Handle Instagram button click
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/shreya._.sb")));
        } else if (v.getId() == R.id.whatsapp_button) {
            String message = "Hello, I found your contact via SatyaVedh app.";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://wa.me/9417407577" + message));
            startActivity(intent);
        } else if (v.getId() == R.id.linkedin_button) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/shreya-bansal-8754b9227")));
        } else if (v.getId() == R.id.telegram_button) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Shreya_0sb")));
        }
    }

    private void saveDataAndShowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("Thank you for reaching out! Your message has been successfully received. We'll get back to you as soon as possible. Have a great day!")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onDestroy() {
        obj.close();
        super.onDestroy();
    }
}