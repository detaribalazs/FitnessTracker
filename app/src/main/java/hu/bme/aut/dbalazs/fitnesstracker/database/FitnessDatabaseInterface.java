package hu.bme.aut.dbalazs.fitnesstracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import hu.bme.aut.dbalazs.fitnesstracker.model.Series;
import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;

/**
 * Created by Balazs on 2017. 11. 19..
 */

public class FitnessDatabaseInterface {
    private Context context;
    private FitnessDatabaseHelper fitnessDbHelper;
    private SQLiteDatabase database;

    public FitnessDatabaseInterface(Context context){
        this.context = context;
    }

    public void open() throws SQLiteException {
        fitnessDbHelper = new FitnessDatabaseHelper(context, DatabaseConstants.DATABASE_NAME);

        database = fitnessDbHelper.getWritableDatabase();
        fitnessDbHelper.onCreate(database);
    }

    public void close(){
        database.close();
    }

    public long insertNewSeries(Series newSeries){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.Series.WEIGHT, newSeries.getWeight());
        contentValues.put(DatabaseConstants.Series.REPS, newSeries.getReps());
        contentValues.put(DatabaseConstants.Series.EXERCISE_ID, newSeries.getExerciseId());

        return database.insert(DatabaseConstants.Series.TABLE_NAME, null, contentValues);
    }

    public boolean deleteSeries(long rowId){
        return database.delete(DatabaseConstants.Series.TABLE_NAME,
                               DatabaseConstants.Series.ID + " = " +rowId,
                               null) > 0;
    }

    public long insertWorkout(Workout workout){
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.Workout.DATE, workout.getWoDate().getTime());
        values.put(DatabaseConstants.Workout.TYPE, workout.getWoType().name());
        return database.insert(DatabaseConstants.Workout.TABLE_NAME, null, values);
    }

    public boolean removeWorkout(long woId){
        return database.delete(DatabaseConstants.Workout.TABLE_NAME,
                               DatabaseConstants.Workout.ID + " = " + woId,
                               null) > 0;
    }

    public static Workout getTodoByCursor(Cursor c){
        return new Workout(
                Workout.WorkoutType.valueOf(c.getString(c.getColumnIndex(DatabaseConstants.Workout.TYPE))), // type
                c.getLong(c.getColumnIndex(DatabaseConstants.Workout.DATE)),                                // milisecs
                c.getLong(c.getColumnIndex(DatabaseConstants.Workout.ID)));                 // id
    }

    public Cursor fetchAllWorkouts(){
        return database.query(DatabaseConstants.Workout.TABLE_NAME,
                new String[]{
                        DatabaseConstants.Workout.ID,
                        DatabaseConstants.Workout.TYPE,
                        DatabaseConstants.Workout.DATE
                }, null, null, null, null, DatabaseConstants.Workout.DATE);
    }

    /*
    public boolean updateSeries(long rowId, Todo newTodo){
        ContentValues values = new ContentValues();
        values.put(DbConstants.Todo.KEY_TITLE, newTodo.getTitle());
        values.put(DbConstants.Todo.KEY_DUEDATE, newTodo.getDueDate());
        values.put(DbConstants.Todo.KEY_DESCRIPTION, newTodo.getDescription());
        values.put(DbConstants.Todo.KEY_PRIORITY, newTodo.getPriority().name());
        return db.update(
                DbConstants.Todo.DATABASE_TABLE,
                values,
                DbConstants.Todo.KEY_ROWID + "=" + rowId ,
                null) > 0;
    }
    */
}
