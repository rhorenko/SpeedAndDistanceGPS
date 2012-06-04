package mast.avalons.com;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper 
        implements BaseColumns {
    
    public static final String TABLE_NAME = "table_name";
    public static final String LAT = "lat";//latitude
    public static final String LON = "lon";//longitude
    public static final String ALT = "alt";//altitude
    public static final String ACC = "acc";//accuracy
    public static final String TIME = "time";
    public DbHelper(Context context) {
        super(context, Provider.DB, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+ 
                LAT + " TEXT, " +
                LON + " TEXT, " +
                ALT + " TEXT, " +
                ACC + " TEXT, " +
                TIME + " TEXT);");       
    }
 
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}