package com.example.periodapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DatabaseUser {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_BIRTHDATE = "birthday";
    public static final String COLUMN_NICK = "nick";
    public static final String COLUMN_MAIL = "mail";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_EVENT = "event";


    private final String TABLE_NAME = "users";
    private final String TABLE_EVENTS = "eventsTable";
    private final int DATABASE_VERSION = 1;
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
                    COLUMN_EVENT + " TEXT NOT NULL, " +
                    COLUMN_TYPE + " TEXT NOT NULL " + " );";
            db.execSQL(sqlcode2);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
    public long addEventToDatabase(String event,String nick, String type)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NICK,nick);
        cv.put(COLUMN_EVENT,event);
        cv.put(COLUMN_TYPE,type);
        return ourDatabase.insert(TABLE_EVENTS,null,cv);
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
        if (cursor.getCount() == 0) {
            return true;
        }
        return false;
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

    public String getEvents{
        Cursor cursor= ourDatabase.rawQuery()

    }
}