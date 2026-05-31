package com.adityasuhag.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AllItemsActivity extends AppCompatActivity {

    // "All" = no filter; the rest match the categories from the form
    private static final String[] FILTER_OPTIONS = {"All", "Electronics", "Pets", "Wallets", "Other"};

    private DatabaseHelper db;
    private ItemAdapter adapter;
    private Spinner spinnerFilter;
    private TextView textEmpty;
    private RecyclerView recyclerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        db = new DatabaseHelper(this);

        spinnerFilter = findViewById(R.id.spinnerFilter);
        textEmpty = findViewById(R.id.textEmpty);
        recyclerItems = findViewById(R.id.recyclerItems);

        // Set up the filter Spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, FILTER_OPTIONS);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        // RecyclerView needs a LayoutManager — LinearLayoutManager = vertical scrolling list
        recyclerItems.setLayoutManager(new LinearLayoutManager(this));

        // Empty adapter to start; loadItems() will populate it
        adapter = new ItemAdapter(new ArrayList<LostFoundItem>(),
                new ItemAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(LostFoundItem item) {
                        // Tapping a row opens the detail screen, passing the item's DB ID
                        Intent intent = new Intent(AllItemsActivity.this, ItemDetailActivity.class);
                        intent.putExtra("item_id", item.getId());
                        startActivity(intent);
                    }
                });
        recyclerItems.setAdapter(adapter);

        // Reload the list whenever the filter changes
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadItems();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    // onResume runs every time this screen comes to the foreground.
    // Important: keeps the list fresh after adding a new item or deleting one.
    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        String selectedFilter = spinnerFilter.getSelectedItem().toString();
        List<LostFoundItem> items;
        if (selectedFilter.equals("All")) {
            items = db.getAllItems();
        } else {
            items = db.getItemsByCategory(selectedFilter);
        }
        adapter.updateItems(items);

        // Show "No items" hint when the list is empty
        if (items.isEmpty()) {
            textEmpty.setVisibility(View.VISIBLE);
            recyclerItems.setVisibility(View.GONE);
        } else {
            textEmpty.setVisibility(View.GONE);
            recyclerItems.setVisibility(View.VISIBLE);
        }
    }
}
