package hu.bme.aut.dbalazs.fitnesstracker.database;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import hu.bme.aut.dbalazs.fitnesstracker.SeriesListFragment;

/**
 * Created by Balazs on 2017. 11. 30..
 */

public class LoadSeriesTask extends AsyncTask<Void, Void, Cursor> {

    private static final String TAG = "LoadSeriesTag";
    private final SeriesListFragment fragment;

    private FitnessDatabaseInterface dbIf;
    private long exId;

    public LoadSeriesTask(SeriesListFragment fragment, FitnessDatabaseInterface databaseInterface, long exerciseId) {
        this.dbIf = databaseInterface;
        this.exId = exerciseId;
        this.fragment = fragment;
    }

    @Override
    protected Cursor doInBackground(Void... voids) {
        try {
            Cursor result = dbIf.fetchAllSeries(exId);
            if(isCancelled()){
                Log.d(TAG, "canceled database query");
                if(result != null){
                    result.close();
                    return null;
                }
            }
            return result;
        }
        catch (Exception e){
            Log.d(TAG, e.toString());
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
                fragment.showSeries(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.showSeries(cursor);
    }
}
