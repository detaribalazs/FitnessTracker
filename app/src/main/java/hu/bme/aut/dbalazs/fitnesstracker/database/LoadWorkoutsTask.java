package hu.bme.aut.dbalazs.fitnesstracker.database;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import hu.bme.aut.dbalazs.fitnesstracker.WorkoutListActivity;

/**
 * Created by Balazs on 2017. 11. 27..
 */

public class LoadWorkoutsTask extends AsyncTask<Void, Void, Cursor> {

    private static final String TAG = "LoadWorkoutsTask";
    private FitnessDatabaseInterface dbInterface;
    private WorkoutListActivity workoutListActivity;

    public LoadWorkoutsTask(WorkoutListActivity listActivity, FitnessDatabaseInterface dbIf) {
        this.workoutListActivity = listActivity;
        this.dbInterface = dbIf;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        try{

            Cursor result = dbInterface.fetchAllWorkouts();

            if(!isCancelled()){
                return result;
            }
            else{
                Log.d(TAG, "Cancelled, closing cursor");
                if (result != null) {
                    result.close();
                }

                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Cursor result) {
        super.onPostExecute(result);

        Log.d(TAG, "Fetch completed, displaying cursor results!");
        try {

            if (workoutListActivity != null) {
                workoutListActivity.showWorkouts(result);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
