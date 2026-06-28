package com.example.travelplannerapp.database;

import com.example.travelplannerapp.models.ReservationAdmin;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.travelplannerapp.models.Reservation;
import androidx.annotation.Nullable;

import com.example.travelplannerapp.models.User;
import com.example.travelplannerapp.models.Trip;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravelPlanner.db";
    private static final int DATABASE_VERSION = 4; // Incremented for hashing implementation

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
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

        // Insert default admin with hashed password
        db.execSQL("INSERT INTO Users (email, firstName, lastName, password, phone, gender, category, isAdmin) " +
                "VALUES ('admin@admin.com', 'Admin', 'User', '" + hashPassword("Admin123!") + "', '0000000000', 'Male', 'Adventure', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Trips");
        db.execSQL("DROP TABLE IF EXISTS Favorites");
        db.execSQL("DROP TABLE IF EXISTS Reservations");
        onCreate(db);
    }



    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password; // Fallback
        }
    }



    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("password", hashPassword(user.getPassword()));
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
                new String[]{email, hashPassword(password)});
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

    public boolean insertAdmin(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("password", hashPassword(user.getPassword()));
        values.put("phone", user.getPhone());
        values.put("gender", user.getGender());
        values.put("category", user.getCategory());
        values.put("isAdmin", 1);
        long result = db.insert("Users", null, values);
        return result != -1;
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
        values.put("password", hashPassword(password));
        int result = db.update("Users", values, "email=?", new String[]{email});
        return result > 0;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Users WHERE email=?", new String[]{email});
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Users WHERE isAdmin=0", null);
    }

    public List<User> getUsersList() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = getAllUsers();
        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("firstName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("lastName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gender")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category"))
                );
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }



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

    public Trip getTripById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Trips WHERE id=?", new String[]{String.valueOf(id)});
        Trip trip = null;
        if (cursor.moveToFirst()) {
            trip = new Trip(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("destination")),
                    cursor.getString(cursor.getColumnIndexOrThrow("country")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("durationDays")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("rating")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image"))
            );
        }
        cursor.close();
        return trip;
    }

    public boolean addTrip(Trip trip){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("destination",trip.getDestination());
        values.put("country",trip.getCountry());
        values.put("durationDays",trip.getDurationDays());
        values.put("price",trip.getPrice());
        values.put("rating",trip.getRating());
        values.put("description",trip.getDescription());
        values.put("image",trip.getImage());
        long result = db.insert("Trips", null, values);
        return result != -1;
    }

    public boolean deleteTrip(int tripId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Favorites", "tripId=?", new String[]{String.valueOf(tripId)});
        db.delete("Reservations", "tripId=?", new String[]{String.valueOf(tripId)});
        int result = db.delete("Trips", "id=?", new String[]{String.valueOf(tripId)});
        return result > 0;
    }

    public boolean updateTrip(Trip trip){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("destination", trip.getDestination());
        values.put("country", trip.getCountry());
        values.put("durationDays", trip.getDurationDays());
        values.put("price", trip.getPrice());
        values.put("rating", trip.getRating());
        values.put("description", trip.getDescription());
        int result = db.update("Trips", values, "id=?", new String[]{String.valueOf(trip.getId())});
        return result > 0;
    }



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
        int result = db.delete("Favorites", "userEmail=? AND tripId=?", new String[]{userEmail, String.valueOf(tripId)});
        return result > 0;
    }

    public boolean isFavorite(String userEmail, int tripId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Favorites WHERE userEmail=? AND tripId=?", new String[]{userEmail, String.valueOf(tripId)});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public List<Trip> getFavoriteTrips(String userEmail) {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT t.* FROM Trips t INNER JOIN Favorites f ON t.id = f.tripId WHERE f.userEmail=?", new String[]{userEmail});
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



    public boolean addReservation(String userEmail, int tripId, String date, int quantity, String type) {
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

    public List<Reservation> getReservationsList(String userEmail) {
        List<Reservation> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r.*, t.destination FROM Reservations r INNER JOIN Trips t ON r.tripId = t.id WHERE r.userEmail=?", new String[]{userEmail});
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

    public List<ReservationAdmin> getAllReservationsList() {
        List<ReservationAdmin> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r.*, t.destination FROM Reservations r INNER JOIN Trips t ON r.tripId = t.id", null);
        if (cursor.moveToFirst()) {
            do {
                ReservationAdmin reservation = new ReservationAdmin(
                        cursor.getString(cursor.getColumnIndexOrThrow("userEmail")),
                        cursor.getString(cursor.getColumnIndexOrThrow("destination")),
                        cursor.getString(cursor.getColumnIndexOrThrow("reservationDate")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("type"))
                );
                list.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }



    public int getUsersCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Users WHERE isAdmin=0", null);
        int count = 0;
        if(cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getReservationsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Reservations", null);
        int count = 0;
        if(cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getTripsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Trips", null);
        int count = 0;
        if(cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }
}
