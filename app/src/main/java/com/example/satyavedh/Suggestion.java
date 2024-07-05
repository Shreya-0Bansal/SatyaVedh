package com.example.satyavedh;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Suggestion extends AppCompatActivity implements View.OnClickListener {
    EditText titleEditText, suggestionEditText, descriptionEditText, anonymousEmailEditText, anonymousPhoneEditText;
    Button submitButton, attach;
    SQLiteDatabase obj;
    Uri selectedFileUri;
    String Insert;
    ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestion);

        titleEditText = findViewById(R.id.title);
        suggestionEditText = findViewById(R.id.suggestion);
        descriptionEditText = findViewById(R.id.details);
        anonymousEmailEditText = findViewById(R.id.email);
        anonymousPhoneEditText = findViewById(R.id.phone);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
        attach = findViewById(R.id.file_upload_button);
        attach.setOnClickListener(this);

        obj = openOrCreateDatabase("vedh", Context.MODE_PRIVATE,null);

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), this::onFilePicked);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bn);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_about) {
                startActivity(new Intent(getApplicationContext(), About.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
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
    public void onClick(View v) {
        if(v==submitButton) {
            obj.execSQL("CREATE TABLE IF NOT EXISTS suggestions (Title VARCHAR NOT NULL, Suggestion VARCHAR, Description VARCHAR, Email VARCHAR, Phone VARCHAR, FileUri VARCHAR)");
            String title = titleEditText.getText().toString();
            String suggestion = suggestionEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String anonymousEmail = anonymousEmailEditText.getText().toString();
            String anonymousPhone = anonymousPhoneEditText.getText().toString();
            String fileUri = selectedFileUri != null ? selectedFileUri.toString() : "";
            try {
                Insert = "INSERT INTO suggestions VALUES('" + title + "', '" + suggestion + "', '" + description + "', '" + anonymousEmail + "','" + anonymousPhone + "', '" + fileUri + "');";
                obj.execSQL(Insert);
                saveDataAndShowAlert();
            } catch (Exception e) {
                Toast.makeText(this, "Error submitting Suggestion: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else if (v== attach) {
            filePickerLauncher.launch("*/*");
        }
}

    private void onFilePicked(Uri uri) {
        selectedFileUri = uri;
        try {
            String fileName = getFileNameFromUri(selectedFileUri);
            InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
            File file = new File(getFilesDir(), fileName);
            String filePath = file.getAbsolutePath();
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error uploading file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }

    private void saveDataAndShowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("Thank you for your suggestion! We appreciate your input and will carefully consider it. Keep the great ideas coming!")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onDestroy() {
        obj.close();
        super.onDestroy();
    }
}
