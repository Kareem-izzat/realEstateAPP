package com.example.realestate.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.realestate.models.User;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
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
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE role != 'admin'", null);

        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String profileImage = cursor.getString(cursor.getColumnIndexOrThrow("profile_image"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));
                String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));

                User user = new User(
                        email,
                        firstName,
                        lastName,
                        password,
                        gender,
                        country,
                        city,
                        phone,
                        profileImage,
                        role
                );

                userList.add(user);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }

}


