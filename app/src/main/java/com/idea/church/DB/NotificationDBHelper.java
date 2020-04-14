//package com.idea.church.DB;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//import com.idea.church.models.Notification;
//
//import java.util.ArrayList;
//
//import static com.idea.church.commons.Constants.DATABASE_NAME;
//
//public class NotificationDBHelper extends SQLiteOpenHelper {
//    private static final String TABLE_NOTIFICATION = "notifications";
//    private static final String COLUMN_ID = "_id";
//    private static final String COLUMN_TITLE = "title";
//    private static final String COLUMN_DETAIL = "detail";
//    private static final String COLUMN_VIEWED = "viewed";
//    private static final String COLUMN_DISPLAYED = "displayed";
//    private static final String COLUMN_DATE = "date";
//
//    public NotificationDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, DATABASE_NAME, factory, version);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "(" +
//                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_TITLE + " TEXT ," +
//                COLUMN_DETAIL + " TEXT ," +
//                COLUMN_DATE + " TEXT ," +
//                COLUMN_VIEWED + " INTEGER ," +
//                COLUMN_DISPLAYED + " INTEGER " +
//                ");";
//
//        db.execSQL(query);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
//        onCreate(db);
//    }
//
//    // Add new row
//    public void addNotification(Notification notification){
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_TITLE, notification.getTitle());
//        values.put(COLUMN_DETAIL, notification.getDetail());
//        values.put(COLUMN_DATE, notification.getDate());
//        values.put(COLUMN_VIEWED, 0);
//        values.put(COLUMN_DISPLAYED, 0);
//
//        SQLiteDatabase db = getWritableDatabase();
//        db.insert(TABLE_NOTIFICATION, null, values);
//        db.close();
//    }
//
//    //    Delete product
//    public void deleteNotification(String id){
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_NOTIFICATION + " WHERE " + COLUMN_ID + "=\"" + id + "\";" );
//    }
//
//    //    Delete product
//    public Notification getNotification(String id){
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
//        Cursor cursor = db.rawQuery(query, null);
//        cursor.moveToFirst();
//
//        String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
//        String detail = cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL));
//        String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//        boolean viewed = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_VIEWED)));
//        boolean displayed = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_DISPLAYED)));
//
//        Notification notification = new Notification(detail, title, date, viewed, displayed);
//        db.close();
//        cursor.close();
//
//        return notification;
//    }
//
//    public ArrayList<Notification> getUnviewedtNotifications(){
//        ArrayList<Notification> notifications = new ArrayList<>();
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + COLUMN_VIEWED + "=\"" + 0 + "\"" + " LIMIT 10;";
//
//        Cursor cursor = db.rawQuery(query, null);
//        cursor.moveToFirst();
//
//        while (!cursor.isAfterLast()){
//            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
//            String detail = cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL));
//            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//            boolean viewed = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_VIEWED)));
//            boolean displayed = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_DISPLAYED)));
//
//            Notification notification = new Notification(detail, title, date, viewed, displayed);
//            notifications.add(notification);
//            cursor.moveToNext();
//        }
//        db.close();
//        cursor.close();
//        return notifications;
//    }
//
//    public ArrayList<Notification> geNonDisplayedtNotifications(){
//        ArrayList<Notification> notifications = new ArrayList<>();
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + COLUMN_DISPLAYED + "=\"" + 0 + "\"" + " LIMIT 10;";
//
//        Cursor cursor = db.rawQuery(query, null);
//        cursor.moveToFirst();
//
//        while (!cursor.isAfterLast()){
//            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
//            String detail = cursor.getString(cursor.getColumnIndex(COLUMN_DETAIL));
//            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//            boolean viewed = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_VIEWED)));
//            boolean displayed = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_DISPLAYED)));
//
//            Notification notification = new Notification(detail, title, date, viewed, displayed);
//            notifications.add(notification);
//            cursor.moveToNext();
//        }
//        db.close();
//        cursor.close();
//        return notifications;
//    }
//}
