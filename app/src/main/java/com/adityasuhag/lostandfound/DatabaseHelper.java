package com.adityasuhag.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database file info
    private static final String DATABASE_NAME = "lostandfound.db";
    private static final int DATABASE_VERSION = 2;

    // Table name and column names — using constants prevents typos
    private static final String TABLE_ITEMS = "items";
    private static final String COL_ID = "id";
    private static final String COL_POST_TYPE = "post_type";
    private static final String COL_NAME = "name";
    private static final String COL_PHONE = "phone";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_DATE = "date";
    private static final String COL_LOCATION = "location";
    private static final String COL_CATEGORY = "category";
    private static final String COL_IMAGE_URI = "image_uri";
    private static final String COL_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Runs ONCE when the app is first installed — creates the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_POST_TYPE + " TEXT, " +
                COL_NAME + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_IMAGE_URI + " TEXT, " +
                COL_TIMESTAMP + " INTEGER)";
        db.execSQL(createTable);
    }

    // Runs when DATABASE_VERSION is incremented — wipe and rebuild for simplicity
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // INSERT — returns the new row's auto-generated ID, or -1 on failure
    public long addItem(LostFoundItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_POST_TYPE, item.getPostType());
        values.put(COL_NAME, item.getName());
        values.put(COL_PHONE, item.getPhone());
        values.put(COL_DESCRIPTION, item.getDescription());
        values.put(COL_DATE, item.getDate());
        values.put(COL_LOCATION, item.getLocation());
        values.put(COL_CATEGORY, item.getCategory());
        values.put(COL_IMAGE_URI, item.getImageUri());
        values.put(COL_TIMESTAMP, item.getTimestamp());

        long newRowId = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return newRowId;
    }

    // SELECT all — newest first (sorted by timestamp descending)
    public List<LostFoundItem> getAllItems() {
        List<LostFoundItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, null, null, null, null, null,
                COL_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                items.add(cursorToItem(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    // SELECT filtered by category — used by the Spinner filter on the list screen
    public List<LostFoundItem> getItemsByCategory(String category) {
        List<LostFoundItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, null,
                COL_CATEGORY + " = ?",
                new String[]{category},
                null, null, COL_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                items.add(cursorToItem(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    // SELECT one — used when the detail screen needs a single item
    public LostFoundItem getItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ITEMS, null,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        LostFoundItem item = null;
        if (cursor.moveToFirst()) {
            item = cursorToItem(cursor);
        }
        cursor.close();
        db.close();
        return item;
    }

    // DELETE by ID — called when user taps the Remove button
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Helper: build a LostFoundItem from the cursor's current row position
    private LostFoundItem cursorToItem(Cursor cursor) {
        return new LostFoundItem(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_POST_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_URI)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
        );
    }
}