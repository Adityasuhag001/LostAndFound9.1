package com.adityasuhag.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab the two buttons from the layout by their IDs
        Button btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        Button btnShowAllItems = findViewById(R.id.btnShowAllItems);

        // When "Create a New Advert" is tapped, open the form screen
        btnCreateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddAdvertActivity.class);
                startActivity(intent);
            }
        });

        // When "Show All Items" is tapped, open the list screen
        btnShowAllItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllItemsActivity.class);
                startActivity(intent);
            }
        });
    }
}