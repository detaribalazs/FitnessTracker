package hu.bme.aut.dbalazs.fitnesstracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;

import hu.bme.aut.dbalazs.fitnesstracker.model.Exercise;
import hu.bme.aut.dbalazs.fitnesstracker.model.Series;
import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;

/**
 * Created by Balazs on 2017. 11. 19..
 */

public class FitnessDatabaseInterface {
    private static String TAG = "Database Interface";
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

    /** inserts a new serie */
    public long insertNewSeries(Series newSeries, long exId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConstants.Series.WEIGHT, newSeries.getWeight());
        contentValues.put(DatabaseConstants.Series.REPS, newSeries.getReps());
        contentValues.put(DatabaseConstants.Series.EXERCISE_ID, exId);

        return database.insert(DatabaseConstants.Series.TABLE_NAME, null, contentValues);
    }

    /** deletes a row with given ID */
    public boolean deleteSeries(long seriesId){
        return database.delete(DatabaseConstants.Series.TABLE_NAME,
                               DatabaseConstants.Series.ID + " = " + seriesId,
                               null) > 0;
    }

    /** deletes all rows with given exercise ID */
    public boolean deleteAllSeries(long exId){
        return database.delete(DatabaseConstants.Series.TABLE_NAME,
                                DatabaseConstants.Series.EXERCISE_ID + " = " + exId,
                                null) >= 0;
    }

    /** queries all series for given exercise */
    public Cursor fetchAllSeries(long exerciseId){
        return database.query(
                DatabaseConstants.Series.TABLE_NAME,
                new String[]{
                        DatabaseConstants.Series.EXERCISE_ID,
                        DatabaseConstants.Series.REPS,
                        DatabaseConstants.Series.WEIGHT,
                        DatabaseConstants.Series.ID
                },
                DatabaseConstants.Series.EXERCISE_ID + " = " + exerciseId,
                null, null, null, null
        );
    }

    /** creates a new series object from cursor data */
    public static Series createSeriesFromCursor(Cursor c){
        return new Series(
                c.getInt(c.getColumnIndex(DatabaseConstants.Series.WEIGHT)),
                c.getInt(c.getColumnIndex(DatabaseConstants.Series.REPS)),
                c.getLong(c.getColumnIndex(DatabaseConstants.Series.ID)));
    }

    public boolean updateSeries(long rowId, Series serie){
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.Series.EXERCISE_ID, serie.getExerciseId());
        values.put(DatabaseConstants.Series.WEIGHT, serie.getWeight());
        values.put(DatabaseConstants.Series.REPS, serie.getReps());
        return database.update(
                DatabaseConstants.Series.TABLE_NAME,
                values,
                DatabaseConstants.Series.ID + "=" + serie.getId(),
                null) > 0;
    }

    /** inserts a new exercise */
    public long insertExercise(Exercise ex, long workoutId)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.Exercise.WORKOUT_ID, workoutId);
        values.put(DatabaseConstants.Exercise.NAME, ex.getName());
        return database.insert(DatabaseConstants.Exercise.TABLE_NAME, null, values);
    }

    /** removes exercise for given ID */
    public boolean removeExercise(long exId){
        if(deleteAllSeries(exId)) {
            return database.delete(DatabaseConstants.Exercise.TABLE_NAME,
                    DatabaseConstants.Exercise.ID + " = " + exId,
                    null) > 0;
        }
        else {
            Log.d(TAG, "removeExercise");
            return false;
        }
    }

    /** fetches exercise ID-s with given workout ID */
    private Cursor fetchExercisesForWorkout(long woId){
        return database.query(DatabaseConstants.Exercise.TABLE_NAME,  // table name
                new String [] {DatabaseConstants.Exercise.ID},        // columns to query
                DatabaseConstants.Exercise.WORKOUT_ID + " = " + woId,         // where workout_id = woId
                null,                 // woId
                null,
                null,
                null);
    }

    public Cursor fetchAllExercises(long woId) {
        return database.query(DatabaseConstants.Exercise.TABLE_NAME,  // table name
                new String[]{
                        DatabaseConstants.Exercise.ID,        // columns to query
                        DatabaseConstants.Exercise.NAME,
                        DatabaseConstants.Exercise.WORKOUT_ID
                },
                DatabaseConstants.Exercise.WORKOUT_ID + " = " + woId,         // where workout_id = woId
                null,                 // woId
                null,
                null,
                null);
    }

    public static Exercise createExerciseFromCursor(@NonNull Cursor c){
        Exercise exercise = new Exercise(
                c.getLong(c.getColumnIndex(DatabaseConstants.Exercise.ID)),
                c.getString(c.getColumnIndex(DatabaseConstants.Exercise.NAME)));
        return exercise;
    }

    public int getRepsCountForExercise(long exId){
        return database.query(DatabaseConstants.Series.TABLE_NAME,  // table name
                new String [] {DatabaseConstants.Series.ID},        // columns to query
                DatabaseConstants.Series.EXERCISE_ID + " = " + exId,         // where exercise_id = exId
                null,
                null,
                null,
                null).getCount();
    }

    /** removes all exercises for given workout, but first queries all of its series */
    public boolean removeAllExercises(long woId){
        // get all exercises for given workout
        Cursor cursor = fetchExercisesForWorkout(woId);
        if(cursor == null)
            Log.d(TAG, "fetchExercisesForWorkout");
        int colIndex = cursor.getColumnIndex(DatabaseConstants.Exercise.ID);
        // iterate over cursor
        try {
            while (cursor.moveToNext()) {
                long exId = cursor.getLong(colIndex);
                if(!deleteAllSeries(exId)){
                    Log.d(TAG, "remove all exercises");
                    return false;
                }
            }
        }
        finally {
            cursor.close();
        }
        return true;
    }

    /** inserts new workout */
    public long insertWorkout(Workout workout){
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.Workout.DATE, workout.getWoDate().getTime());
        values.put(DatabaseConstants.Workout.TYPE, workout.getWoType().name());
        return database.insert(DatabaseConstants.Workout.TABLE_NAME, null, values);
    }

    public boolean removeWorkout(long woId){
        if(removeAllExercises(woId)) {
            return database.delete(DatabaseConstants.Workout.TABLE_NAME,
                    DatabaseConstants.Workout.ID + " = " + woId,
                    null) >= 0;
        }
        else{
            Log.d(TAG, "removeAllExercises");
            return false;
        }

    }

    /** creates a new Workout object from cursor */
    public static Workout createWorkoutFromCursor(@NonNull Cursor c){
        return new Workout(
                Workout.WorkoutType.valueOf(c.getString(c.getColumnIndex(DatabaseConstants.Workout.TYPE))), // type
                c.getLong(c.getColumnIndex(DatabaseConstants.Workout.DATE)),                                // milisecs
                c.getLong(c.getColumnIndex(DatabaseConstants.Workout.ID)));                 // id
    }

    /** fetches all workouts */
    public Cursor fetchAllWorkouts(){
        return database.query(DatabaseConstants.Workout.TABLE_NAME,
                new String[]{
                        DatabaseConstants.Workout.ID,
                        DatabaseConstants.Workout.TYPE,
                        DatabaseConstants.Workout.DATE
                },
                null,
                null,
                null,
                null,
                DatabaseConstants.Workout.DATE + " ASC");
    }

}

