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
        sqLiteDatabase.execSQL(DatabaseConstants.Workout.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseConstants.Exercise.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseConstants.Series.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DatabaseConstants.Series.DROP_TABLE);
        sqLiteDatabase.execSQL(DatabaseConstants.Exercise.DROP_TABLE);
        sqLiteDatabase.execSQL(DatabaseConstants.Workout.DROP_TABLE);

        sqLiteDatabase.execSQL(DatabaseConstants.Workout.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseConstants.Exercise.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseConstants.Series.CREATE_TABLE);
    }
}
