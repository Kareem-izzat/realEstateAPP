package com.example.realestate.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper{
    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (email TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL, first_name TEXT, last_name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isValidUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean insertUser(String email, String password, String firstName, String lastName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if user already exists
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

        long result = db.insert("users", null, values);
        return result != -1;
    }
}
