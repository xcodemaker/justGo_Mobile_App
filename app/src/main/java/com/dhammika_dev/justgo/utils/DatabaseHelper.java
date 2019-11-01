package com.dhammika_dev.justgo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "train.db";
    public static final String TABLE_NAME = "station_table";
    public static final String COL_1 = "STATION_ID";
    public static final String COL_2 = "STATION_NAME";
    public static int TOATAL_STATION_COUNT = 407;
    private static String DATABASE_PATH = "/data/data/com.dhammika_dev.justgo/databases/";
    JSONArray stationList;
    Context context;
    SQLiteDatabase dbs;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;

        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        if (!checkDataBase()) {
            copyDataBase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void makeRequest(SQLiteDatabase db) {

        Log.e("response", "making request");
        dbs = db;

        String end_point = "http://api.lankagate.gov.lk:8280/railway/1.0/station/getAll?lang=en";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, end_point, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Response trainID: 2222", response.toString());
                        try {
                            stationList = response.getJSONObject("RESULTS").getJSONArray("stationList");
                            TOATAL_STATION_COUNT = response.getInt("NOFRESULTS");
                            Log.d("Response:", stationList.getJSONObject(0).get("stationName").toString());
                            for (int i = 0; i < TOATAL_STATION_COUNT; i++) {

                                try {
                                    String stationName = stationList.getJSONObject(i).get("stationName").toString();
                                    Log.d("Response : ", stationName);
                                    int stationID = stationList.getJSONObject(i).getInt("stationID");
                                    //Log.d("stations", stationName);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(COL_1, stationID);
                                    contentValues.put(COL_2, stationName);
                                    dbs.insert(TABLE_NAME, null, contentValues);
                                } catch (JSONException e) {
                                    Log.d("Response:", e.getMessage());
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("Error: 222 ", e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: 222 ", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer 7a69c7ee-1c5f-395b-9a84-f51b22537e04");
                return headers;
            }

        };
        Singleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    public void insertData(SQLiteDatabase db) {

        for (int i = 0; i <= TOATAL_STATION_COUNT; i++) {

            try {
                String stationName = stationList.getJSONObject(i).get("stationName").toString();
                int stationID = stationList.getJSONObject(i).getInt("stationID");
                Log.d("stations", stationName);
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_1, stationID);
                contentValues.put(COL_2, stationName);
                db.insert(TABLE_NAME, null, contentValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public Cursor getStationID(SQLiteDatabase db, String stationName) {
        String[] projection = {COL_1};
        Cursor cursor = db.rawQuery("SELECT " + COL_1 + " FROM " + TABLE_NAME + " WHERE " + COL_2 + "=?", new String[]{stationName});
        return cursor;
    }

    public Cursor getNoOfRows(SQLiteDatabase db) {
        String[] projection = {COL_1};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);
        return cursor;
    }

    public Cursor getStations(SQLiteDatabase db) {
        String[] projection = {COL_2};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);
        return cursor;
    }

    public int getStationCount(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("SELECT " + COL_1 + " from " + TABLE_NAME, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean isTableExists(String tableName, SQLiteDatabase database) {

        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    private boolean checkDataBase() {
        System.out.println("DATABASE_PATH : " + DATABASE_PATH);
        File dbFile = new File(DATABASE_PATH);
        if (dbFile.exists()) return true;
        if (!new File(dbFile.getParent()).exists()) {
            new File(dbFile.getParent()).mkdirs();
        }
        return false;
    }

    private void copyDataBase() {
        Log.i("Database",
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        File dbDirectory = new File(new File(DATABASE_PATH).getParent());
        File dbwal = new File(dbDirectory.getPath() + File.separator + "-wal");
        if (dbwal.exists()) {
            dbwal.delete();
        }
        File dbshm = new File(dbDirectory.getPath() + File.separator + "=shm");
        if (dbshm.exists()) {
            dbshm.delete();
        }
        //<<<<<<<<<< END OF ADDED CODE >>>>>>>>>>

        OutputStream myOutput;
        int length;
        InputStream myInput;
        try {
            myInput = context.getAssets().open(DATABASE_NAME);
            myOutput = new FileOutputStream(DATABASE_PATH);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database",
                    "New database has been copied to device!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
