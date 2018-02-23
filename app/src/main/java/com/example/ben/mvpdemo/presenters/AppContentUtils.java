package com.example.ben.mvpdemo.presenters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.ben.mvpdemo.models.User;

import java.util.ArrayList;

/**
 * Created by ben on 23/02/2018.
 */

public class AppContentUtils {

    public static final String CONTENT = "content://";
    public static final String PACKAGE = "com.example.ben.mvpdemo";
    public static final String PROVIDER_NAME = CONTENT + PACKAGE;
    public static final class USER {
        public final static String TABLE_NAME = "user";
        public final static String Columns_ID = "_id";
        public final static String Columns_NAME = "name";
        public final static String Columns_EMAIL = "email";
        public final static Uri CONTENT_URI = Uri.parse(PROVIDER_NAME + "/" + TABLE_NAME);

        public static void insertOrUpdateUser(ContentResolver cr, User user) {
            String select = Columns_NAME + "=?";
            String[] selectionArgs = {user.getName()};
            ContentValues contentValues = new ContentValues();
            contentValues.put(Columns_NAME,user.getName());
            contentValues.put(Columns_EMAIL,user.getEmail());
            Cursor cursor = cr.query(CONTENT_URI, null, select, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst())
                cr.update(CONTENT_URI, contentValues, select, selectionArgs);
            else
                cr.insert(CONTENT_URI, contentValues);
        }

        public static ArrayList<User> getAllUsers(ContentResolver cr) {
            ArrayList<User> users = new ArrayList<>();
            User user;
            Cursor cursor = cr.query(CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(Columns_NAME));
                    String email = cursor.getString(cursor.getColumnIndex(Columns_EMAIL));
                    user = new User(name,email);
                    users.add(user);
                } while (cursor.moveToNext());
            }
            return users;
        }

    }

}
