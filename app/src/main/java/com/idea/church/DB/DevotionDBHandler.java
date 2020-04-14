package com.idea.church.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.idea.church.models.Devotion;

import static com.idea.church.commons.Constants.DATABASE_NAME;
import static com.idea.church.commons.HelperFunctions.getBitmapAsByteArray;
import static com.idea.church.commons.HelperFunctions.getImageFromByteArray;

public class DevotionDBHandler extends SQLiteOpenHelper {
    private static final String TABLE_DEVOTION = "devotions";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_VERSE_TITLE = "title";
    private static final String COLUMN_VERSE_DETAIL = "detail";
    private static final String COLUMN_SERVER_ID = "server_id";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_DATE = "date";

    public DevotionDBHandler(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_DEVOTION + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VERSE_TITLE + " TEXT ," +
                COLUMN_VERSE_DETAIL + " TEXT ," +
                COLUMN_SERVER_ID + " TEXT ," +
                COLUMN_IMAGE + " BLOB ," +
                COLUMN_DATE + " TEXT " +
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVOTION);
        onCreate(db);
    }

    // Add new row
    public void addDevotion(Devotion devotion){
        ContentValues values = new ContentValues();
        values.put(COLUMN_VERSE_TITLE, devotion.getVerse_title());
        values.put(COLUMN_VERSE_DETAIL, devotion.getVerse_detail());
        values.put(COLUMN_SERVER_ID, devotion.getServerID());
        values.put(COLUMN_DATE, devotion.getDate());
        values.put(COLUMN_IMAGE, getBitmapAsByteArray(devotion.getImage()));

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_DEVOTION, null, values);
        db.close();
    }

    public void deleteDevotion(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DEVOTION + " WHERE " + COLUMN_ID + "=\"" + id + "\";" );
    }

    public Devotion getDevotion(String id){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DEVOTION + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        String title = cursor.getString(cursor.getColumnIndex(COLUMN_VERSE_TITLE));
        String detail = cursor.getString(cursor.getColumnIndex(COLUMN_VERSE_DETAIL));
        String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        String serverID = cursor.getString(cursor.getColumnIndex(COLUMN_SERVER_ID));

        byte[] bytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
        Devotion devotion = new Devotion(getImageFromByteArray(bytes), null, title, detail, date, serverID);

        db.close();
        cursor.close();
        return devotion;
    }

    public Devotion getCurrentDevotion(){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DEVOTION + " WHERE 1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();

        if(cursor.getCount() < 1){
            return null;
        }
        String title  = cursor.getString(cursor.getColumnIndex(COLUMN_VERSE_TITLE));
        String detail = cursor.getString(cursor.getColumnIndex(COLUMN_VERSE_DETAIL));
        String date   = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        String serverID = cursor.getString(cursor.getColumnIndex(COLUMN_SERVER_ID));

        byte[] bytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
        Devotion devotion = new Devotion(getImageFromByteArray(bytes), null, title, detail, date, serverID);

        db.close();
        cursor.close();
        return devotion;
    }

    public Devotion getPreviousDevotion(String currentID){
        String id = String.valueOf(Integer.valueOf(currentID)-1);
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DEVOTION + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();

        String title  = cursor.getString(cursor.getColumnIndex(COLUMN_VERSE_TITLE));
        String detail = cursor.getString(cursor.getColumnIndex(COLUMN_VERSE_DETAIL));
        String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        String serverID = cursor.getString(cursor.getColumnIndex(COLUMN_SERVER_ID));

        byte[] bytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
        Devotion devotion = new Devotion(getImageFromByteArray(bytes), null, title, detail, date, serverID);

        db.close();
        cursor.close();
        return devotion;
    }
}
