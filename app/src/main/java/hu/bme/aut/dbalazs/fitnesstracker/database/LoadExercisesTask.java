package hu.bme.aut.dbalazs.fitnesstracker.database;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import hu.bme.aut.dbalazs.fitnesstracker.ExerciseListFragment;

/**
 * Created by Balazs on 2017. 11. 29..
 */

public class LoadExercisesTask extends AsyncTask<Void, Void, Cursor> {

    public static final String TAG = "LoadExercisesTag";
    private FitnessDatabaseInterface databaseIf;
    private ExerciseListFragment fragment;
    private long woId;

    public LoadExercisesTask(ExerciseListFragment fragment, FitnessDatabaseInterface databaseInterface, long workoutId){
        this.fragment = fragment;
        this.databaseIf = databaseInterface;
        this.woId = workoutId;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        try{
            Cursor result = databaseIf.fetchAllExercises(woId);
            if(!isCancelled())
            {
                return result;
            }
            else{
                Log.d(TAG, "LoadExercisTask cancelled.");
                if(result != null)
                    result.close();
                return null;
            }
        }
        catch(Exception e){
            Log.d(TAG, "Exception: " + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        Log.d(TAG, "Fetch completed, displaying cursor results!");
        try {

            if (fragment != null) {
                fragment.showExercises(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
