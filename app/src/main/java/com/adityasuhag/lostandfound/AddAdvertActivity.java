package com.adityasuhag.lostandfound;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddAdvertActivity extends AppCompatActivity {

    private static final String[] CATEGORIES = {"Electronics", "Pets", "Wallets", "Other"};

    // Holds the URI of the picked image, or null until the user picks one
    private Uri selectedImageUri = null;

    // The modern Android way of launching another screen and getting a result back
    private ActivityResultLauncher<String[]> imagePickerLauncher;

    private ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advert);

        // Register the picker FIRST — this must happen before the activity is RESUMED.
        // We use OpenDocument (Storage Access Framework) because it returns a URI we
        // can hold onto permanently. GetContent returns a URI that often expires.
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            // Tell Android: "I want to keep reading this image forever,
                            // not just for this screen." Survives app restart.
                            int flags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            getContentResolver().takePersistableUriPermission(uri, flags);

                            selectedImageUri = uri;
                            imagePreview.setImageURI(uri);
                            imagePreview.setVisibility(View.VISIBLE);
                        }
                    }
                });

        // Find all the views
        final RadioGroup radioPostType = findViewById(R.id.radioPostType);
        final EditText editName = findViewById(R.id.editName);
        final EditText editPhone = findViewById(R.id.editPhone);
        final EditText editDescription = findViewById(R.id.editDescription);
        final EditText editDate = findViewById(R.id.editDate);
        final EditText editLocation = findViewById(R.id.editLocation);
        final Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        imagePreview = findViewById(R.id.imagePreview);
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        Button btnSave = findViewById(R.id.btnSave);

        // Set up the category Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, CATEGORIES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // "Choose Image" button — launches the system gallery, filtered to images only
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerLauncher.launch(new String[]{"image/*"});
            }
        });

        // SAVE button — validate, build item, push to DB
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();
                String description = editDescription.getText().toString().trim();
                String date = editDate.getText().toString().trim();
                String location = editLocation.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                int selectedRadioId = radioPostType.getCheckedRadioButtonId();
                String postType = (selectedRadioId == R.id.radioFound) ? "Found" : "Lost";

                if (name.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(AddAdvertActivity.this,
                            "Name and phone are required", Toast.LENGTH_SHORT).show();
                    return;
                }


                String imageUri = (selectedImageUri != null) ? selectedImageUri.toString() : "";
                long timestamp = System.currentTimeMillis();

                LostFoundItem item = new LostFoundItem(
                        postType, name, phone, description,
                        date, location, category, imageUri, timestamp);

                DatabaseHelper db = new DatabaseHelper(AddAdvertActivity.this);
                long newId = db.addItem(item);

                if (newId != -1) {
                    Toast.makeText(AddAdvertActivity.this,
                            "Advert saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddAdvertActivity.this,
                            "Save failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}