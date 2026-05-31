package com.adityasuhag.lostandfound;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    private int itemId;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        db = new DatabaseHelper(this);

        // Pull the item's ID out of the Intent that launched this screen
        itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid item", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Look it up in the database
        final LostFoundItem item = db.getItemById(itemId);
        if (item == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Grab all views
        TextView textDetailPostType = findViewById(R.id.textDetailPostType);
        TextView textDetailName = findViewById(R.id.textDetailName);
        TextView textDetailTimeAgo = findViewById(R.id.textDetailTimeAgo);
        TextView textDetailCategory = findViewById(R.id.textDetailCategory);
        TextView textDetailDescription = findViewById(R.id.textDetailDescription);
        TextView textDetailDate = findViewById(R.id.textDetailDate);
        TextView textDetailLocation = findViewById(R.id.textDetailLocation);
        TextView textDetailPhone = findViewById(R.id.textDetailPhone);
        ImageView imageDetailPhoto = findViewById(R.id.imageDetailPhoto);
        Button btnRemove = findViewById(R.id.btnRemove);

        // Fill them with the item's data
        textDetailPostType.setText(item.getPostType());
        textDetailName.setText(item.getName());
        textDetailTimeAgo.setText(DateUtils.getRelativeTimeSpanString(
                item.getTimestamp(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS));
        textDetailCategory.setText(item.getCategory());
        textDetailDescription.setText(item.getDescription());
        textDetailDate.setText(item.getDate());
        textDetailLocation.setText(item.getLocation());
        textDetailPhone.setText(item.getPhone());

        // Show the image only if one was uploaded — wired up in the next step
        String uriString = item.getImageUri();
        if (uriString != null && !uriString.isEmpty()) {
            try {
                imageDetailPhoto.setImageURI(Uri.parse(uriString));
                imageDetailPhoto.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                imageDetailPhoto.setVisibility(View.GONE);
            }
        }

        // REMOVE button → confirm with a dialog → delete → close screen
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ItemDetailActivity.this)
                        .setTitle("Remove item")
                        .setMessage("Are you sure you want to remove this listing?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteItem(itemId);
                                Toast.makeText(ItemDetailActivity.this,
                                        "Item removed", Toast.LENGTH_SHORT).show();
                                finish(); // back to list — onResume there refreshes the data
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }
}
