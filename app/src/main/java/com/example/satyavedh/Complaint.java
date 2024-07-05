package com.example.satyavedh;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.OpenableColumns;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Complaint extends AppCompatActivity implements View.OnClickListener {

    EditText dateEditText, descriptionEditText, locationEditText, witnessNameEditText, witnessContactEditText, anonymousEmailEditText, anonymousPhoneEditText, additionalCommentsEditText;
    Button submit, file;
    SQLiteDatabase obj;
    Uri selectedFileUri;
    String filePath;
    String Insert;
    ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complaint);

        dateEditText = findViewById(R.id.date_edittext);
        descriptionEditText = findViewById(R.id.description_edittext);
        locationEditText = findViewById(R.id.location_edittext);
        witnessNameEditText = findViewById(R.id.witness_name_edittext);
        witnessContactEditText = findViewById(R.id.witness_contact_edittext);
        anonymousEmailEditText = findViewById(R.id.anonymous_email_edittext);
        anonymousPhoneEditText = findViewById(R.id.anonymous_phone_edittext);
        additionalCommentsEditText = findViewById(R.id.additional_comments_edittext);
        file = findViewById(R.id.file_upload_button);
        file.setOnClickListener(this);
        submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), this::onFilePicked);
        obj = openOrCreateDatabase("vedh", Context.MODE_PRIVATE, null);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bn);
        bottomNavigationView.setSelectedItemId(R.id.bottom_terms);
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
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme != null && scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }
    @Override
    public void onClick(View v) {
        if (v==submit) {
            try {
                obj.execSQL("CREATE TABLE IF NOT EXISTS complaints (Date VARCHAR NOT NULL, Location VARCHAR, Description VARCHAR,W_Name VARCHAR,W_Contact VARCHAR, Email VARCHAR, Phone VARCHAR,Comments VARCHAR,  FileUri VARCHAR)");
                String date = dateEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String witnessName = witnessNameEditText.getText().toString();
                String witnessContact = witnessContactEditText.getText().toString();
                String anonymousEmail = anonymousEmailEditText.getText().toString();
                String anonymousPhone = anonymousPhoneEditText.getText().toString();
                String additionalComments = additionalCommentsEditText.getText().toString();
                String fileUri = selectedFileUri != null ? selectedFileUri.toString() : "";

                Insert = "INSERT INTO complaints VALUES('" + date + "', '" + description + "', '" + location + "', '" + witnessName + "','" + witnessContact + "','" + anonymousEmail  + "','" + anonymousPhone  + "','"  + additionalComments + "','" + fileUri + "');";
                obj.execSQL(Insert);
                saveDataAndShowAlert();
            } catch (SQLiteException e) {
                Toast.makeText(this, "Error submitting complaint: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (v==file) {
            filePickerLauncher.launch("*/*");
        }
    }

    private void onFilePicked(Uri uri) {
        selectedFileUri = uri;
        try {
            String fileName = getFileNameFromUri(selectedFileUri);
            InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
            File file = new File(getFilesDir(), fileName);
            filePath = file.getAbsolutePath();

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


    private void saveDataAndShowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success")
                .setMessage("Thank you for bringing your concerns to our attention. We will promptly address your complaint and strive to ensure a better experience in the future.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onDestroy() {
        obj.close();
        super.onDestroy();
    }
}