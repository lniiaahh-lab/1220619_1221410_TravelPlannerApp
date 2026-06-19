package com.example.travelplannerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.travelplannerapp.models.Reservation;
import androidx.annotation.Nullable;

import com.example.travelplannerapp.models.User;
import com.example.travelplannerapp.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelPlanner.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users Table
        db.execSQL("CREATE TABLE Users (" +
                "email TEXT PRIMARY KEY, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "password TEXT, " +
                "phone TEXT, " +
                "gender TEXT, " +
                "category TEXT, " +
                "isAdmin INTEGER DEFAULT 0)");

        // Trips Table
        db.execSQL("CREATE TABLE Trips (" +
                "id INTEGER PRIMARY KEY, " +
                "destination TEXT, " +
                "country TEXT, " +
                "durationDays INTEGER, " +
                "price REAL, " +
                "rating REAL, " +
                "description TEXT, " +
                "image TEXT)");

        // Favorites Table
        db.execSQL("CREATE TABLE Favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userEmail TEXT, " +
                "tripId INTEGER)");

        // Reservations Table
        db.execSQL("CREATE TABLE Reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userEmail TEXT, " +
                "tripId INTEGER, " +
                "reservationDate TEXT, " +
                "quantity INTEGER, " +
                "type TEXT, " +
                "status TEXT DEFAULT 'Confirmed')");

        // Insert default admin
        db.execSQL("INSERT INTO Users (email, firstName, lastName, password, phone, gender, category, isAdmin) " +
                "VALUES ('admin@admin.com', 'Admin', 'User', 'Admin123!', '0000000000', 'Male', 'Adventure', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Trips");
        db.execSQL("DROP TABLE IF EXISTS Favorites");
        db.execSQL("DROP TABLE IF EXISTS Reservations");
        onCreate(db);
    }

    // ========== USER METHODS ==========

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("password", user.getPassword());
        values.put("phone", user.getPhone());
        values.put("gender", user.getGender());
        values.put("category", user.getCategory());
        values.put("isAdmin", 0);
        long result = db.insert("Users", null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email=? AND password=?",
                new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE email=?",
                new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean isAdmin(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT isAdmin FROM Users WHERE email=?",
                new String[]{email});
        boolean admin = false;
        if (cursor.moveToFirst()) {
            admin = cursor.getInt(0) == 1;
        }
        cursor.close();
        return admin;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
    }

    // ========== TRIP METHODS ==========

    public void insertTrips(List<Trip> trips) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("Trips", null, null);
            for (Trip trip : trips) {
                ContentValues values = new ContentValues();
                values.put("id", trip.getId());
                values.put("destination", trip.getDestination());
                values.put("country", trip.getCountry());
                values.put("durationDays", trip.getDurationDays());
                values.put("price", trip.getPrice());
                values.put("rating", trip.getRating());
                values.put("description", trip.getDescription());
                values.put("image", trip.getImage());
                db.insert("Trips", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Trips", null);
        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("destination")),
                        cursor.getString(cursor.getColumnIndexOrThrow("country")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("durationDays")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("rating")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image"))
                );
                trips.add(trip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return trips;
    }

    // ========== FAVORITES METHODS ==========

    public boolean addFavorite(String userEmail, int tripId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userEmail", userEmail);
        values.put("tripId", tripId);
        long result = db.insert("Favorites", null, values);
        return result != -1;
    }

    public boolean removeFavorite(String userEmail, int tripId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Favorites",
                "userEmail=? AND tripId=?",
                new String[]{userEmail, String.valueOf(tripId)});
        return result > 0;
    }

    public boolean isFavorite(String userEmail, int tripId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Favorites WHERE userEmail=? AND tripId=?",
                new String[]{userEmail, String.valueOf(tripId)});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public List<Trip> getFavoriteTrips(String userEmail) {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT t.* FROM Trips t " +
                "INNER JOIN Favorites f ON t.id = f.tripId " +
                "WHERE f.userEmail=?", new String[]{userEmail});
        if (cursor.moveToFirst()) {
            do {
                Trip trip = new Trip(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("destination")),
                        cursor.getString(cursor.getColumnIndexOrThrow("country")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("durationDays")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("rating")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image"))
                );
                trips.add(trip);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return trips;
    }

    // ========== RESERVATION METHODS ==========

    public boolean addReservation(String userEmail, int tripId, String date,
                                  int quantity, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userEmail", userEmail);
        values.put("tripId", tripId);
        values.put("reservationDate", date);
        values.put("quantity", quantity);
        values.put("type", type);
        values.put("status", "Confirmed");
        long result = db.insert("Reservations", null, values);
        return result != -1;
    }

    public Cursor getUserReservations(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT r.*, t.destination FROM Reservations r " +
                "INNER JOIN Trips t ON r.tripId = t.id " +
                "WHERE r.userEmail=?", new String[]{userEmail});
    }

    public Cursor getAllReservations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT r.*, t.destination FROM Reservations r " +
                "INNER JOIN Trips t ON r.tripId = t.id", null);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Users WHERE isAdmin=0", null);
    }

    public boolean deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Users", "email=?", new String[]{email});
        return result > 0;
    }
    public boolean updateUserProfile(
            String email,
            String firstName,
            String lastName,
            String phone,
            String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("phone", phone);
        values.put("password", password);

        int result = db.update(
                "Users",
                values,
                "email=?",
                new String[]{email});

        return result > 0;
    }
    public List<Reservation> getReservationsList(String userEmail) {

        List<Reservation> reservations = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT r.*, t.destination FROM Reservations r " +
                        "INNER JOIN Trips t ON r.tripId = t.id " +
                        "WHERE r.userEmail=?",
                new String[]{userEmail});

        if (cursor.moveToFirst()) {

            do {

                Reservation reservation = new Reservation(
                        cursor.getString(cursor.getColumnIndexOrThrow("destination")),
                        cursor.getString(cursor.getColumnIndexOrThrow("reservationDate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("type"))
                );

                reservations.add(reservation);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return reservations;
    }
}