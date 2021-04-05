package com.example.getfitv30.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.getfitv30.models.UserWeights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseAdapter
{
    private SQLiteDatabase db;

    public DatabaseAdapter(Context context)
    {
        // Init database (create new tables, drop old tables)
        DatabaseHelper adapter = new DatabaseHelper(context);
        this.db = adapter.GetDb();
    }

    public long insertUser(String name, String email, String pass)
    {

        ContentValues cv = new ContentValues();

        // column_name - insertable_value
        cv.put("username", name);
        cv.put("password", pass);
        cv.put("email", email);

        // Inserting values into the users table
        long uid = this.db.insert(DatabaseHelper.TABLE_1_NAME, null, cv);

        return uid;
    }

    public void insertUserData(long uid, int height, int weight, float bmi, String time)
    {
        ContentValues cv = new ContentValues();

        cv.put("uid", uid);
        cv.put("height", height);
        cv.put("weight", weight);
        cv.put("bmi", bmi);
        cv.put("time", time);

        // Inserting values into the user data table
        this.db.insert(DatabaseHelper.TABLE_2_NAME, null, cv);
    }

    public void insertConsumedCalories(long uid, String food_name, int amount, double kcal)
    {
        ContentValues cv = new ContentValues();

        cv.put("uid", uid);
        cv.put("food", food_name);
        cv.put("amount", amount);
        cv.put("cal", kcal);

        // Inserting values into the calories table
        this.db.insert(DatabaseHelper.TABLE_3_NAME, null, cv);
    }

    public int getLastUserHeight(long uid)
    {
        // Selecting the last known height of the user
        String query = "SELECT height FROM " + DatabaseHelper.TABLE_2_NAME + " WHERE uid = " + uid + " ORDER BY id DESC";

        // Selecting the first rows first column (since the first row will be the last inserted record, because of the query-s order by clause)
        Cursor cursor = this.db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public int getLastUserWeight(long uid)
    {
        // Selecting the last known weight of the user
        String query = "SELECT weight FROM " + DatabaseHelper.TABLE_2_NAME + " WHERE uid = " + uid + " ORDER BY id DESC";

        // Selecting the first rows first column (since the first row will be the last inserted record, because of the query-s order by clause)
        Cursor cursor = this.db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public float getLastUserBmi(long uid)
    {
        // Selecting the last known bmi of the user
        String query = "SELECT bmi FROM " + DatabaseHelper.TABLE_2_NAME + " WHERE uid = " + uid + " ORDER BY id DESC";

        // Selecting the first rows first column (since the first row will be the last inserted record, because of the query-s order by clause)
        Cursor cursor = this.db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getFloat(0);
    }

    public ArrayList<UserWeights> getAllWeights(long uid)
    {
        // Selecting the 20 most recent weight records for the current user
        String query = "SELECT id, weight, time FROM " + DatabaseHelper.TABLE_2_NAME + " WHERE uid = " + uid + " ORDER BY time DESC, id DESC LIMIT 20";
        Cursor cursor = this.db.rawQuery(query, null);

        ArrayList<UserWeights> result = new ArrayList<UserWeights>();

        // Moving through the whole result set, and adding the elements to an ArrayList
        while (cursor.moveToNext())
        {
            UserWeights userWeights = new UserWeights();
            userWeights.setId(cursor.getLong(0));
            userWeights.setWeight(cursor.getInt(1));
            userWeights.setDate(cursor.getString(2));

            result.add(userWeights);
        }

        return result;
    }

    public long loginUser(String email, String pass)
    {
        long uid = -1;

        // Preparing SQL query
        String query = "SELECT uid FROM " + DatabaseHelper.TABLE_1_NAME + " WHERE email = '" + email + "' AND password = '" + pass + "'";
        // Executing the raw query
        Cursor cursor = this.db.rawQuery(query, null);

        // If there are no records matching the given username and password, do not log the user in
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();

            // Since an email and password combo is unique, we just need to read the first element
            uid = Long.parseLong(cursor.getString(0));
        }

        cursor.close();

        // If the login failed, we return -1, otherwise the uid
        return uid;
    }

    public String getUserName(long uid)
    {
        String query = "SELECT username FROM users WHERE uid = " + uid;

        // Selecting the first row, and the first column (since we only have a single value returned)
        Cursor cursor = this.db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public int getTotalCalories(long uid)
    {
        // Preparing the query
        String query = "SELECT SUM(cal) FROM calories WHERE uid = " + uid;

        // Selecting the first row, and the first column (since we only have a single value returned)
        Cursor cursor = this.db.rawQuery(query, null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public static final String TAG = "DATABASE INIT";

        public static final String DATABASE_NAME = "get_fit.db";

        public static final String TABLE_1_NAME = "users";
        public static final String TABLE_2_NAME = "user_data";
        public static final String TABLE_3_NAME = "calories";

        private SQLiteDatabase db;

        public DatabaseHelper(Context context)
        {
            // Initializing database, increment version number to get a clean database
            super(context, DATABASE_NAME, null, 3);

            this.db = this.getWritableDatabase();
        }

        public SQLiteDatabase GetDb()
        {
            // Get the writeable database instance
            return this.db;
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            Log.d(TAG, "Creating tables...");

            // Creating database tables
            db.execSQL("CREATE TABLE " + TABLE_1_NAME + " (uid INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, password TEXT NOT NULL, email TEXT NOT NULL)");
            db.execSQL("CREATE TABLE " + TABLE_2_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER NOT NULL, height INTEGER NOT NULL, weight INTEGER NOT NULL, bmi REAL NOT NULL, time TEXT NOT NULL, FOREIGN KEY (uid) REFERENCES " + TABLE_1_NAME + " (uid))");
            db.execSQL("CREATE TABLE " + TABLE_3_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER NOT NULL, food TEXT NOT NULL, amount INTEGER NOT NULL, cal INTEGER NOT NULL, FOREIGN KEY (uid) REFERENCES " + TABLE_1_NAME + " (uid))");

            Log.d(TAG, "Tables created!");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1)
        {
            Log.d(TAG, "Dropping tables...");

            // On upgrade, drop all old existing tables (the data will be lost)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_3_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_2_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_1_NAME);

            Log.d(TAG, "Tables dropped!");

            onCreate(db);
        }
    }
}


