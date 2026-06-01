# Lost & Found App

An Android app that helps users report and recover lost or found items. Built for SIT305 — extends Pass Task 7.1 with Google Maps and Places integration (Task 9.1).

## Features

### Core (Task 7.1)
- Create a new advert (Lost or Found) with name, phone, description, date, category, optional image
- View all items in a list, sorted newest first
- **Filter** items by category (Electronics, Pets, Wallets, Other)
- **Upload an image** from the device gallery for each post
- **Relative timestamp** ("2 days ago") on every listing
- View full details of any item
- Remove a listing once the owner is found

### Map & Geo (Task 9.1)
- **Places Autocomplete** — tap the Location field to search any place with Google's autocomplete
- **GET CURRENT LOCATION** — one-tap to fill location using the device's GPS + reverse geocoding
- **Show on Map** — view all items as pins on Google Maps (red = Lost, green = Found)
- **Radius-based filter** — slide a SeekBar to show only items within X km of your current location

## Tech Stack

- **Language:** Java
- **Platform:** Android (minSdk 24+)
- **Database:** SQLite via `SQLiteOpenHelper`
- **UI:** XML layouts, `RecyclerView`, `Spinner`, `AlertDialog`, `SeekBar`, `SupportMapFragment`
- **Image picker:** Android Storage Access Framework (`ActivityResultContracts.OpenDocument`) with persistable URI permissions
- **Maps:** Google Maps SDK for Android (`play-services-maps`)
- **Location:** Fused Location Provider (`play-services-location`) + Android `Geocoder` for reverse geocoding
- **Places:** Google Places SDK (`places:3.4.0`) with the `Autocomplete` IntentBuilder
- **Distance:** `Location.distanceBetween` (great-circle distance)

## Project Structure

```
app/src/main/java/com/adityasuhag/lostandfound/
├── MainActivity.java         // Entry screen with three buttons + Places SDK init
├── AddAdvertActivity.java    // Form with Places Autocomplete + Get Current Location
├── AllItemsActivity.java     // List view with category filter
├── ItemDetailActivity.java   // Detail view with Remove button
├── MapActivity.java          // Google Map with item markers + radius slider
├── ItemAdapter.java          // RecyclerView adapter for list rows
├── LostFoundItem.java        // POJO model (incl. latitude/longitude)
└── DatabaseHelper.java       // SQLite schema and CRUD (10 columns + lat/lng)

app/src/main/res/layout/
├── activity_main.xml
├── activity_add_advert.xml
├── activity_all_items.xml
├── activity_item_detail.xml
├── activity_map.xml
└── item_row.xml
```

## How to Run

1. Clone this repository
2. Open the project in Android Studio (Hedgehog or newer recommended)
3. **Set up your Google Maps / Places API key** (required — see next section)
4. Sync Gradle
5. Run on an emulator with Google Play Services (e.g. Pixel 8 API 34+) or a physical device with Play Services
6. Seed the emulator's GPS via Extended Controls → Location → Set Location

## Google API Key Setup

This project needs a Google Cloud API key with **Maps SDK for Android** and **Places API** enabled.

1. Create a project at [Google Cloud Console](https://console.cloud.google.com), enable billing
2. Enable the APIs: **Maps SDK for Android** and **Places API** (legacy, not "Places API (New)")
3. Create an API key under **APIs & Services → Credentials**
4. Restrict the key:
   - API restrictions: Maps SDK for Android + Places API
   - Application restrictions: Android apps with this package name and your debug SHA-1
5. In the project root, add this line to `local.properties`:
```
   MAPS_API_KEY=AIzaSyXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```
6. Sync Gradle — the key is injected into the manifest at build time. `local.properties` is gitignored, so the key never leaves your machine.

To get your debug SHA-1, run from the project root:
```
./gradlew signingReport
```
Copy the `SHA1:` value under the `debug` variant.

## Subtask Implementations

### Task 7.1
| Subtask | Implementation |
|---|---|
| Category filter | `Spinner` in `AllItemsActivity` → `DatabaseHelper.getItemsByCategory(category)` |
| Image upload | `ActivityResultContracts.OpenDocument` + persistable URI permission |
| Date/time stamp | `System.currentTimeMillis()` + `DateUtils.getRelativeTimeSpanString` |

### Task 9.1
| Subtask | Implementation |
|---|---|
| Places autocomplete | `Autocomplete.IntentBuilder` (OVERLAY mode) returning a `Place` with `LatLng` |
| Current location | `FusedLocationProviderClient.getLastLocation` + `Geocoder` for reverse geocoding |
| Show on map | `SupportMapFragment` + markers from DB (`MarkerOptions` with red/green hues for Lost/Found) |
| Radius-based search | `SeekBar` (1–100 km) + `Location.distanceBetween` to filter markers in real time |

## AI / LLM Use

Portions of this project were developed with assistance from Claude (Anthropic). Full details in the Task 9.1 submission document.

## Author

Aditya Suhag — Deakin University, SIT305
