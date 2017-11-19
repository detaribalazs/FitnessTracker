package hu.bme.aut.dbalazs.fitnesstracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FitnessDatabaseHelper extends SQLiteOpenHelper {

    public FitnessDatabaseHelper(Context context, String name) {
        super(context, name, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseConstants.CREATE_ALL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseConstants.DROP_ALL);
        sqLiteDatabase.execSQL(DatabaseConstants.CREATE_ALL);
    }
}
