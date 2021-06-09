package com.example.periodapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public final class DatabaseUser {
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_BIRTHDATE = "birthday";
    private static final String COLUMN_NICK = "nick";
    private static final String COLUMN_MAIL = "mail";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_EVENT_DATE= "event";

    private static final String COLUMN_USER_ID= "user_id";
    private static final String COLUMN_DATE=    "diet_date";
    private static final String COLUMN_DESCRIPTION= "description";
    private static final String COLUMN_TITLE= "title";
    private static final String COLUMN_PIC= "picture";
    private static final String COLUMN_DB_ID= "db_id";





    private final String TABLE_NAME = "users";
    private final String TABLE_EVENTS = "eventsTable";
    private final String TABLE_DIET = "dietTable";
    private final String TABLE_MOOD = "moodTable";
    private final int DATABASE_VERSION = 2;
    private final String DATABASE_NAME = "UserDatabase";
    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public DatabaseUser(Context context) {
        ourContext = context;
    }

    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // Tworzenie tabeli w bazie danych
            // CREATE TABLE UserInfo

            String sqlcode = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_MAIL + " TEXT NOT NULL, " +
                    COLUMN_HEIGHT + " TEXT NOT NULL, " +
                    COLUMN_WEIGHT + " TEXT NOT NULL, " +
                    COLUMN_BIRTHDATE + " TEXT NOT NULL, " +
                    COLUMN_NICK + " TEXT NOT NULL " + " ); ";

            db.execSQL(sqlcode);

            String sqlcode2 = "CREATE TABLE " + TABLE_EVENTS + " (" + COLUMN_NICK + " TEXT NOT NULL, " +
                    COLUMN_EVENT_DATE + " TEXT NOT NULL, " +
                    COLUMN_TYPE + " TEXT NOT NULL " + " );";
            db.execSQL(sqlcode2);

            String sqlcode3 = "CREATE TABLE " + TABLE_DIET + " (" + COLUMN_USER_ID + " TEXT NOT NULL, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_PIC + " TEXT NOT NULL, " +
                    COLUMN_DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+ " );";
            db.execSQL(sqlcode3);

            String sqlcode4 = "CREATE TABLE " + TABLE_MOOD + " (" + COLUMN_USER_ID + " TEXT NOT NULL, " +
                    COLUMN_DATE + " TEXT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                    COLUMN_PIC + " TEXT NOT NULL, " +
                    COLUMN_DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"+ " );";
            db.execSQL(sqlcode4);



        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL(("DROP TABLE IF EXISTS "+TABLE_EVENTS ));
            db.execSQL(("DROP TABLE IF EXISTS " + TABLE_DIET));
            db.execSQL(("DROP TABLE IF EXISTS " + TABLE_MOOD));
            onCreate(db);

        }
    }

    public DatabaseUser open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntry(String name, String mail, String weight, String height, String birthday, String nick) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_MAIL, mail);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_HEIGHT, height);
        cv.put(COLUMN_BIRTHDATE, birthday);
        cv.put(COLUMN_NICK, nick);
        return ourDatabase.insert(TABLE_NAME, null, cv);
    }
    public String getData() {

        String[] columns = new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_WEIGHT, COLUMN_HEIGHT, COLUMN_BIRTHDATE};
        Cursor c = ourDatabase.query(TABLE_NAME, columns, null, null, null, null, null);

        String result = "";
        int iColId = c.getColumnIndex(COLUMN_ID);
        int iName = c.getColumnIndex(COLUMN_NAME);
        int iWeight = c.getColumnIndex(COLUMN_WEIGHT);
        int iHeight = c.getColumnIndex(COLUMN_HEIGHT);
        int iBirth = c.getColumnIndex(COLUMN_BIRTHDATE);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result = result + c.getString(iColId) + ":" + c.getString(iName) + ":" + c.getString(iWeight) + ":" + c.getString(iHeight) + ":" + c.getString(iBirth) + "\n";
        }
        c.close();
        return result;
    }

    public long deleteEntry(String id) {
        return ourDatabase.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
    }

    public long updateEntry(String nick, String change, String whatToChange) {
        ContentValues cv = new ContentValues();

        switch (whatToChange) {
            case "name": {
                cv.put(COLUMN_NAME, change);
                return ourDatabase.update(TABLE_NAME, cv, COLUMN_NICK + "=?", new String[]{nick});
            }
            case "mail": {
                cv.put(COLUMN_MAIL, change);
                return ourDatabase.update(TABLE_NAME, cv, COLUMN_NICK + "=?", new String[]{nick});
            }
            default:
                return 0;
        }
    }

    public boolean nickCheck(String nickname1) {
        Cursor cursor = ourDatabase.rawQuery("SELECT *  FROM  users WHERE nick= " + "'" + nickname1 + "'", null);
        return cursor.getCount() == 0;
    }
    public String getID(String nickname1) throws SQLException {
        long recc = 0;
        String rec = null;
        Cursor mCursor = ourDatabase.rawQuery(
                "SELECT id  FROM  users WHERE nick= " + "'" + nickname1 + "'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            recc = mCursor.getLong(0);
            rec = String.valueOf(recc);
        }
        return rec;}

    //funckje dla bazy danych dla kalendarza
    public long addEventToDatabase(String date,String nick, String type)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NICK,nick);
        cv.put(COLUMN_EVENT_DATE,date);
        cv.put(COLUMN_TYPE,type);
        return ourDatabase.insert(TABLE_EVENTS,null,cv);
    }
    public ArrayList<String> getEvents(String nickname1){
        ArrayList<String> lista = new ArrayList<>();
        String result;
        Cursor mCursor = ourDatabase.rawQuery(
                "SELECT * FROM  eventsTable WHERE nick= " + "'" + nickname1 + "'", null);

        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            result = mCursor.getString(0) + "," + mCursor.getString(1) + "," + mCursor.getString(2) ;
            lista.add(result);

        }
        int size= lista.size();
        String g= String.valueOf(size);
        mCursor.close();
        return lista;
    }
    public long deleteEvent(String date){
        return ourDatabase.delete(TABLE_EVENTS,COLUMN_EVENT_DATE+"=?",new String[]{date});
    }
    // funkcje do bazy danych stworzonej dla diet i mood;

    public long addDietItemDB(String userID, String date, String title, String descrpition, String pic){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, userID);
            cv.put(COLUMN_DATE, date);
            cv.put(COLUMN_TITLE, title);
            cv.put(COLUMN_DESCRIPTION, descrpition);
            cv.put(COLUMN_PIC, pic);
            return ourDatabase.insert(TABLE_DIET,null, cv);
        }
    public ArrayList<String> getDietlist(String userID, String date){
            ArrayList<String> lista= new ArrayList<>();
            String result;
            Cursor cursor= ourDatabase.rawQuery("SELECT * FROM dietTable where user_id= "+"'"+userID +"'" +"AND diet_date= "+ "'"+date+ "'",null );
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                result= cursor.getString(2)+","+ cursor.getString(3)+","+cursor.getString(4)+","+ cursor.getString(5);
                lista.add(result);
            }
            cursor.close();
            return lista;
    }

    public long removeDiet(String userid, String date, int id){
        return ourDatabase.delete(TABLE_DIET,COLUMN_DB_ID+"=" +id,null);

    }


    public String getLastMeal(String userid){
        String last;
        Cursor cursor =ourDatabase.rawQuery("SELECT * FROM dietTable ORDER BY diet_date DESC LIMIT 1", null);
        last = cursor.getString(2);
        cursor.close();
        return last;
    }
    public ArrayList<String> getMeals(String nickname1){
        ArrayList<String> lista = new ArrayList<>();
        String result;
        Cursor mCursor = ourDatabase.rawQuery(
                "SELECT * FROM  dietTable WHERE user_id= " + "'" + nickname1 + "'", null);

        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            result = mCursor.getString(2);
            lista.add(result);
        }
        mCursor.close();
        return lista;
    }

    // funkcje do bazy danych moodtable
    public long addmoodItem(String userID, String date, String title, String descrpition, String pic){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID, userID);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DESCRIPTION, descrpition);
        cv.put(COLUMN_PIC, pic);
        return ourDatabase.insert(TABLE_MOOD,null, cv);
    }
    public ArrayList<String> getmoodList(String userID, String date){
        ArrayList<String> lista= new ArrayList<>();
        String result;
        Cursor cursor= ourDatabase.rawQuery("SELECT * FROM moodTable where user_id= "+"'"+userID +"'" +"AND diet_date= "+ "'"+date+ "'",null );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            result= cursor.getString(2)+","+ cursor.getString(3)+","+cursor.getString(4)+","+ cursor.getString(5);
            lista.add(result);
        }
        cursor.close();
        return lista;
    }

    public long removeMood(int id){
       return ourDatabase.delete(TABLE_MOOD, COLUMN_DB_ID + " = "+ id , null);
    }



}