package com.example.realestate.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE users (" +
                        "email TEXT PRIMARY KEY NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "first_name TEXT, " +
                        "last_name TEXT, " +
                        "gender TEXT, " +
                        "country TEXT, " +
                        "city TEXT, " +
                        "phone TEXT" +
                        ");"
        );
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
                              String gender, String country, String city, String phone) {
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

        long result = db.insert("users", null, values);
        return result != -1;
    }
    public boolean updateUser(String email, String firstName, String lastName, String phone, @Nullable String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("phone", phone);

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

}


