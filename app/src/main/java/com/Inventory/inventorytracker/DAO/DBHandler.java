package com.Inventory.inventorytracker.DAO;

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

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "Storage";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Packages";
    private static final String ID_COL = "id";
    private static final String PackageID = "pID";
    private static final String Owner = "Name";
    private static final String Contents = "Contents";
    private static final String Description = "Description";

    private final String delimiter = "@";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //db methods
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
            content += (item + delimiter);//delimiting the contents
            int num;
        }
        if(content.length() > 2) content = content.substring(0, content.length()-2);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Owner, packageName);
        values.put(Contents, content);
        values.put(Description, packageDescription);
        values.put(PackageID, packageID);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Box createBox(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Box box = null;
        ContentValues values = new ContentValues();
        values.put(PackageID, ID);
        values.put(Owner, "");
        values.put(Contents, "");
        values.put(Description, "");
        Integer lastID = (int) db.insert(TABLE_NAME, null, values);
        db.close();
        if (lastID != -1) {
            box = new Box("", new ArrayList<String>(), lastID, ID, "");
        }
        return box;
    }
    public Box getPackage(Integer ID){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("Database", "Called");
        Box box = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE pID = " + ID + " limit 1", null);
        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            cursor.getString(1);
            //delimiting db string
            String[] contents = cursor.getString(3).split(delimiter);
            box = new Box("Owner", new ArrayList<>(Arrays.asList(contents)), cursor.getInt(1),
                    cursor.getInt(2), cursor.getString(4));
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
    public Integer updateData(Box box){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Owner, box.getOwner());
        String content = getContent(box.getContents());
        values.put(Contents, getContent(box.getContents()));
        values.put(Description, box.getDescription());
        Integer success = db.update(TABLE_NAME, values, PackageID + "=?", new String[]{Integer.toString(box.getBoxID())});
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

    public void deletePackage() {
        SQLiteDatabase db = this.getWritableDatabase();
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
        ContentValues values = new ContentValues();
        values.put(Owner, "Name");
        values.put(Contents, "Contents1, Contents2");
        values.put(Description, "Package Description");
        values.put(PackageID, 100);
        db.insert(TABLE_NAME, null, values);
//        db.close();
//        deleteTable();
    }
    String getContent(List<String> contents){
        String content = "";
        for(String item: contents){
            content += item + delimiter;
        }
        if(content.length() > 2) content = content.substring(0, content.length()-2);
        return content;
    }

    public List<Box> getAllPackages(){
        List<Box> packages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("Database", "Called");
        Box box = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        // moving our cursor to first position.
        while (cursor.moveToNext()) {
            cursor.getString(1);
            //delimiting db string
            String[] contents = cursor.getString(3).split(delimiter);
            box = new Box("Owner", new ArrayList<>(Arrays.asList(contents)), cursor.getInt(1),
                    cursor.getInt(2), cursor.getString(4));
            packages.add(box);
        }
        // at last closing our cursor
        // and returning our array list.

        cursor.close();
        return packages;
    }
}
