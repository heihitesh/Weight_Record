package com.itshiteshverma.weight_record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;

import java.sql.SQLException;

import static java.lang.Integer.parseInt;

/**
 * Created by Hitesh Verma on 6/10/2015.
 */
public class WeightDataBase {
    // we need that some variable does not change like table name , row name

    public static final String KEY_ROWID = "_id"; //gives a row id
    public static final String KEY_NAME = "person_name";
    public static final String KEY_HOTNESS = "person_hotness";

    private static final String DATABASE_NAME = "HotOrNot2";
    private static final String DATABASE_TABLE = "peopleTable"; //table
    private static final int DATABASE_VERSION = 1; //version

    private DbHelper1 ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;


    //Creating a Helper class to Handle the DataBase
    private static class DbHelper1 extends SQLiteOpenHelper {
        public DbHelper1(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);  // super(context, name, factory, version);
        }

        //This method is called when database is created for the first time after that the onUpgrade method is callled
        @Override
        public void onCreate(SQLiteDatabase db) {
            //SET UP THE DATABASE
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_NAME + " TEXT NOT NULL, " +
                            KEY_HOTNESS + " TEXT NOT NULL);"
            );


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);

        }
    }

    public WeightDataBase(Context c) {
        ourContext = c;


    }

    public WeightDataBase open() throws SQLException { // might throw an Exception
        //this constructor allow our database to open and write
        ourHelper = new DbHelper1(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();  //if we can write then we can also read
        return this;
    }

    public void close() {
        //this will close our class DbHelper
        ourHelper.close();
    }

    public long createEntry(String name, String hotness) {
        String sb = name;
        int a = parseInt(String.valueOf(sb.length()));
        if (a >= 20 && a < 100) {

            String EditedTime = sb.substring(0, 20) + "\n" + sb.substring(21, sb.length());
            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, EditedTime); //1 param is where to put the string
            // 2 param is what to put
            cv.put(KEY_HOTNESS, hotness); // putting data into database
            return ourDatabase.insert(DATABASE_TABLE, null, cv);  //1 param which tale to writing to
            //2 null default , 3 param is the things to insert ;


        } else {

            ContentValues cv = new ContentValues();
            cv.put(KEY_NAME, name); //1 param is where to put the string
            // 2 param is what to put
            cv.put(KEY_HOTNESS, hotness); // putting data into database
            return ourDatabase.insert(DATABASE_TABLE, null, cv);  //1 param which tale to writing to
            //2 null default , 3 param is the things to inser ;

        }
    }

    public String getData() {
        String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_HOTNESS};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);//allow to read from the Database
        String result = "";

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iHotness = c.getColumnIndex(KEY_HOTNESS);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext())// for(start form , End to ,Incremment or decrement)
        {   //as the loop start it give the info of the first row then the second row
            result = result + c.getString(iRow) + ") " + c.getString(iName) + " " + c.getString(iHotness) + "\n";
        }

        return result;
    }

    public String getName(long l) throws SQLException { // might throw an Exception
        //we need cursor to read data from the data Base
        String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_HOTNESS};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String name = c.getString(1); //1 = Name Column
            return name;
        }
        return null;

    }

    public String getHotness(long l) throws SQLException { // might throw an Exception
        String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_HOTNESS};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            String hotness = c.getString(2); // Hotness Column
            return hotness;
        }
        return null;
    }

    public void updateEnrty(long lRow, String mName, String mHotness) throws SQLException { // might throw an Exception
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(KEY_NAME, mName);
        cvUpdate.put(KEY_HOTNESS, mHotness);
        ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID + " " + lRow, null);
        //1) table name , 2) what to update , 3)which row to update (eg - KEY_ROWID=4) ,4) default null
    }

    public void deleteEntry(long lRow1) throws SQLException { // might throw an Exception
        ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + lRow1, null);
        //1) table name , 2) what to update , 3)which row to delete ,4) default null

    }


}
