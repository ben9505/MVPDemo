package com.example.ben.mvpdemo.models;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ben.mvpdemo.presenters.AppContentUtils;

/**
 * Created by ben on 23/02/2018.
 */

public class AppContentProvider extends ContentProvider {

    private MainDatabaseHelper databaseHelper;
    private static final String DATABASE_NAME = "dbTest";
    private static final int MATCH_TABLE_USER = 1;
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_USER = "create table " + AppContentUtils.USER.TABLE_NAME +
            "(" + AppContentUtils.USER.Columns_ID + " integer primary key autoincrement, "
            + AppContentUtils.USER.Columns_NAME + " text not null, "
            + AppContentUtils.USER.Columns_EMAIL + " text not null);";

    private class MainDatabaseHelper extends SQLiteOpenHelper {

        /*
         * Instantiates an open helper for the provider's SQLite data repository
         * Do not do database creation and upgrade here.
         */
        MainDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /*
         * Creates the data repository. This is called when the provider attempts to open the
         * repository and SQLite reports that it doesn't exist.
         */
        public void onCreate(SQLiteDatabase db) {

            // Creates the main table
            db.execSQL(SQL_CREATE_TABLE_USER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + AppContentUtils.USER.TABLE_NAME);
            onCreate(db);
        }
    }

    // Define constants within the PersonsProvider class


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for table 3 are shown.
         */

        /*
         * Sets the integer value for multiple rows in table 3 to 1. Notice that no wildcard is used
         * in the path
         */
        sUriMatcher.addURI(AppContentUtils.PACKAGE, AppContentUtils.USER.TABLE_NAME, MATCH_TABLE_USER);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.example.app.provider/table3/3" matches, but
         * "content://com.example.app.provider/table3 doesn't.
         */
        sUriMatcher.addURI(AppContentUtils.PACKAGE, "table3/#", 2);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String tableName = "";
        String groupBy = null;
        String limit = null;
        String having = null;
        switch (sUriMatcher.match(uri)) {


            // If the incoming URI was for all of table3
            case MATCH_TABLE_USER:
                tableName = AppContentUtils.USER.TABLE_NAME;
                break;

            // If the incoming URI was for a single row
            case 2:

                /*
                 * Because this URI was for a single row, the _ID value part is
                 * present. Get the last path segment from the URI; this is the _ID value.
                 * Then, append the value to the WHERE clause for the query
                 */
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;

            default:
                // If the URI is not recognized, you should do some error handling here.
        }

        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, groupBy, having, sortOrder, limit);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String table = null;
        switch (sUriMatcher.match(uri)) {
            case MATCH_TABLE_USER:
                table = AppContentUtils.USER.TABLE_NAME;
                break;
        }

        insertInternal(table, values);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            String table = null;
            switch (sUriMatcher.match(uri)) {
                case MATCH_TABLE_USER:
                    table = AppContentUtils.USER.TABLE_NAME;
                    break;
            }
            deleteInternal(table, selection, selectionArgs);
            return 1;
        } catch (Exception ex) {
            return -1;
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            String table = null;
            switch (sUriMatcher.match(uri)) {
                case MATCH_TABLE_USER:
                    table = AppContentUtils.USER.TABLE_NAME;
                    break;
            }
            updateInternal(table, values, selection, selectionArgs);
            return 1;
        } catch (Exception ex) {
            return -1;
        }
    }

    private void insertInternal(String table, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.insert(table, null, values);
        db.close();
    }

    private void updateInternal(String table, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.update(table, values, selection, selectionArgs);
        db.close();
    }

    private void deleteInternal(String table, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(table, selection, selectionArgs);
        db.close();
    }


}
