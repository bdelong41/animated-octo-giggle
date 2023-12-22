package com.Inventory.inventorytracker.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.Inventory.inventorytracker.model.Box;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "Storage";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "Packages";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    private static final String PackageID = "pID";

    // below variable is for our course name column
    private static final String Owner = "Name";

    // below variable id for our course duration column.
    private static final String Contents = "Contents";

    // below variable for our course description column.
    private static final String Description = "Description";


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
//        dropTable();
//        buildTable();
//        seedTable();
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PackageID + " INTEGER NOT NULL,"
                + Owner + " TEXT,"
                + Contents + " TEXT,"
                + Description + " TEXT)";
//
//        // at last we are calling a exec sql
//        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewData(String packageName, List<String> contents, String packageDescription, Integer packageID) {

        //stringifying contents
        String content = "";
        for(String item: contents){
            content += (item + " ");
        }

        if(content.length() > 2) content = content.substring(0, content.length()-2);

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(Owner, packageName);
        values.put(Contents, content);
        values.put(Description, packageDescription);
        values.put(PackageID, packageID);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }
    public void createBox(int ID){
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        values.put(PackageID, ID);
        values.put(Owner, "owner");
        values.put(Contents, "contents");
        values.put(Description, "Description");
        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }
    public Box getData(Integer ID){
        Log.d("Database", "Called");
        Box box = null;
        String str = "";
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursor
                = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE pID = " + ID + " limit 1", null);

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {

            cursor.getString(1);
            //delimiting db string
            String[] contents = cursor.getString(3).split(",");
            List<String> vals = new ArrayList<>(Arrays.asList("vals", "Vals2"));
            box = new Box("Owner", new ArrayList<>(Arrays.asList(contents)), 1, 1 );
            cursor.close();
        }
        // at last closing our cursor
        // and returning our array list.
        else {
            cursor.close();
        }
        return box;
    }

    public Integer updateData(Integer boxID, String contents, String owner){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Owner, owner);
        values.put(Contents, contents);
        values.put(Description, contents);

        Integer success = db.update(TABLE_NAME, values, PackageID + "=?", new String[]{Integer.toString(boxID)});


        db.close();
        return success;
    }
    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id >?", new String[]{Integer.toString(0)});
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteCourse() {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "name=?", new String[]{TABLE_NAME});
        db.close();
    }

    public void dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.execSQL("drop table if exists " + TABLE_NAME);
        db.close();
    }

    public void buildTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PackageID + " INTEGER NOT NULL,"
                + Owner + " TEXT,"
                + Contents + " TEXT,"
                + Description + " TEXT)";
//
//        // at last we are calling a exec sql
//        // method to execute above sql query
        db.execSQL(query);
    }

    public void seedTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(Owner, "Name");
        values.put(Contents, "Contents1, Contents2");
        values.put(Description, "Package Description");
        values.put(PackageID, 100);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);
        // at last we are closing our
        // database after adding database.
//        db.close();

//        deleteTable();
    }
}
