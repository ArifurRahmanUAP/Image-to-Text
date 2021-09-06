package com.example.textscanner;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class Database  extends SQLiteOpenHelper{

    public static final int dbversion =10;
    public static final  String dbname = "Data.db";
    public static final String ID = "_id";
    public static final String TABLE_NAME = "info";
    public static final String NAME = "Name";
    public static final String Create_Table = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY autoincrement,name)";
    public  static final  String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private Context context;

     public Database(Context context) {
            super(context,dbname,null, dbversion);
            this.context = context;

     }

        @Override
        public void onCreate(SQLiteDatabase db) {
         try {
             db.execSQL(Create_Table);
         }
         catch (Exception e)
         {
             Toast.makeText(context,"Exception: "+e,Toast.LENGTH_SHORT).show();
         }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         try {
             db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
             onCreate(db);
         }
         catch (Exception e)
         {
             Toast.makeText(context,"Exception: "+e,Toast.LENGTH_SHORT).show();

         }

        }


    //insert data

    public long insertData(String name) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME,name);
        long rowid= sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return rowid;

    }


    //delete data
    public void delete(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        sqLiteDatabase.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id="+id);
    }



    public Cursor displayAllData()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_ALL, null);

        return cursor;


    }

}
