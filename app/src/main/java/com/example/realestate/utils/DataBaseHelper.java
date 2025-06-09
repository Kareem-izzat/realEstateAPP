package com.example.realestate.utils;

import static com.example.realestate.models.JsonParser.findPropertyById;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.realestate.models.JsonParser;
import com.example.realestate.models.Property;
import com.example.realestate.models.ReservedProperty;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE users (" +
                        "email TEXT PRIMARY KEY, " +
                        "password TEXT, " +
                        "first_name TEXT, " +
                        "last_name TEXT, " +
                        "gender TEXT, " +
                        "country TEXT, " +
                        "city TEXT, " +
                        "phone TEXT, " +
                        "profile_image TEXT, " +
                        "role TEXT NOT NULL" +
                        ");"
        );

        db.execSQL("CREATE TABLE favorites (" +
                "user_email TEXT, " +
                "property_id INTEGER, " +
                "PRIMARY KEY(user_email, property_id), " +
                "FOREIGN KEY(user_email) REFERENCES users(email) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE IF NOT EXISTS reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_email TEXT, " +
                "property_id INTEGER, " +
                "reservation_date TEXT, " +
                "FOREIGN KEY(user_email) REFERENCES users(email));");

        // static admin account
        db.execSQL("INSERT INTO users (email, password, first_name, last_name, gender, country, city, phone, profile_image, role) " +
                "VALUES ('admin@admin.com', 'Admin123!', 'Admin', 'User', 'N/A', 'N/A', 'N/A', 'N/A', '', 'admin');");
        db.execSQL("INSERT INTO users VALUES ('ali.ramallah@mail.com', 'Ali123!', 'Ali', 'Saleh', 'Male', 'Palestine', 'Ramallah', '+970599111111', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('nour.nablus@mail.com', 'Nour123!', 'Nour', 'Hamdan', 'Female', 'Palestine', 'Nablus', '+970599222222', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('huda.hebron@mail.com', 'Huda123!', 'Huda', 'Awad', 'Female', 'Palestine', 'Hebron', '+970599333333', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('yazan.amman@mail.com', 'Yazan123!', 'Yazan', 'Khatib', 'Male', 'Jordan', 'Amman', '+962799444444', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('dana.irbid@mail.com', 'Dana123!', 'Dana', 'Salem', 'Female', 'Jordan', 'Irbid', '+962799555555', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('khaled.cairo@mail.com', 'Khaled123!', 'Khaled', 'Mostafa', 'Male', 'Egypt', 'Cairo', '+201000666666', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('mona.alex@mail.com', 'Mona123!', 'Mona', 'Youssef', 'Female', 'Egypt', 'Alexandria', '+201000777777', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('salma.ramallah@mail.com', 'Salma123!', 'Salma', 'Qasem', 'Female', 'Palestine', 'Ramallah', '+970599888888', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('tariq.amman@mail.com', 'Tariq123!', 'Tariq', 'Zahran', 'Male', 'Jordan', 'Amman', '+962799999999', '', 'customer');");
        db.execSQL("INSERT INTO users VALUES ('amira.cairo@mail.com', 'Amira123!', 'Amira', 'Nabil', 'Female', 'Egypt', 'Cairo', '+201001111111', '', 'customer');");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean isValidUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertUser(String email, String password, String firstName, String lastName,
                              String gender, String country, String city, String phone, String role) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("gender", gender);
        values.put("country", country);
        values.put("city", city);
        values.put("phone", phone);
        values.put("role", role);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean updateUser(String email, String firstName, String lastName, String phone,
                              @Nullable String newPassword, @Nullable String profileImageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("phone", phone);

        if (profileImageUri != null && !profileImageUri.isEmpty()) {
            values.put("profile_image", profileImageUri);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            values.put("password", newPassword);
        }

        int rowsAffected = db.update("users", values, "email = ?", new String[]{email});
        return rowsAffected > 0;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
    }
    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }
    public Cursor getAllCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE role != 'admin'", null);
    }

    public boolean deleteUserByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("users", "email = ?", new String[]{email}) > 0;
    }

    public boolean isFavorite(String email, int propertyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites WHERE user_email = ? AND property_id = ?", new String[]{email, String.valueOf(propertyId)});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    public boolean addFavorite(String email, int propertyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_email", email);
        values.put("property_id", propertyId);

        long result = db.insert("favorites", null, values);
        return result != -1; // <- Make sure this is true
    }

    public void removeFavorite(String email, int propertyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("favorites", "user_email = ? AND property_id = ?", new String[]{email, String.valueOf(propertyId)});
    }

    public boolean addReservation(String email, int propertyId, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM reservations WHERE user_email = ? AND property_id = ?",
                new String[]{email, String.valueOf(propertyId)});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("user_email", email);
        values.put("property_id", propertyId);
        values.put("reservation_date", date);

        long result = db.insert("reservations", null, values);
        return result != -1;
    }

    public List<ReservedProperty> getReservationsForUser(String email) {
        List<ReservedProperty> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT r.property_id, r.reservation_date FROM reservations r WHERE r.user_email = ?", new String[]{email});
        while (cursor.moveToNext()) {
            int propId = cursor.getInt(0);
            String date = cursor.getString(1);
            Property prop = findPropertyById(propId); // Implement this helper
            if (prop != null) {
                list.add(new ReservedProperty(prop, date));
            }
        }
        cursor.close();
        return list;
    }

    public boolean cancelReservation(int propertyId, int userPropertyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("reservations", "property_id = ? AND id = ?", new String[]{
                String.valueOf(propertyId),
                String.valueOf(userPropertyId)
        }) > 0;
    }

    public List<Property> getFavoritesForUser(String email) {
        List<Property> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT property_id FROM favorites WHERE user_email = ?", new String[]{email});
        while (cursor.moveToNext()) {
            int propertyId = cursor.getInt(0);
            Property p = JsonParser.findPropertyById(propertyId); // assumes you already added this method
            if (p != null) favorites.add(p);
        }
        cursor.close();
        return favorites;
    }

}


