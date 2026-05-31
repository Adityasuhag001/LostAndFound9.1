package com.adityasuhag.lostandfound;

public class LostFoundItem {
    private int id;
    private String postType;     // "Lost" or "Found"
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private String category;     // "Electronics", "Pets", "Wallets", "Other"
    private String imageUri;     // empty string "" if no image
    private long timestamp;      // milliseconds since epoch — used for "2 days ago"

    // Used when creating a NEW item (no ID yet — DB assigns it on insert)
    public LostFoundItem(String postType, String name, String phone, String description,
                         String date, String location, String category,
                         String imageUri, long timestamp) {
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.category = category;
        this.imageUri = imageUri;
        this.timestamp = timestamp;
    }

    // Used when READING an item from the DB (includes the assigned ID)
    public LostFoundItem(int id, String postType, String name, String phone, String description,
                         String date, String location, String category,
                         String imageUri, long timestamp) {
        this(postType, name, phone, description, date, location, category, imageUri, timestamp);
        this.id = id;
    }

    // Getters — read-only access to the fields
    public int getId() { return id; }
    public String getPostType() { return postType; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public String getImageUri() { return imageUri; }
    public long getTimestamp() { return timestamp; }
}