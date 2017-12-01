package hu.bme.aut.dbalazs.fitnesstracker;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.aut.dbalazs.fitnesstracker.adapter.SeriesAdapter;
import hu.bme.aut.dbalazs.fitnesstracker.application.FitnessApplication;
import hu.bme.aut.dbalazs.fitnesstracker.database.FitnessDatabaseInterface;
import hu.bme.aut.dbalazs.fitnesstracker.database.LoadSeriesTask;
import hu.bme.aut.dbalazs.fitnesstracker.model.Series;


public class SeriesListFragment extends Fragment {

    private static final String TAG = "SeriesListFragment";
    public static final String SERIES_EXERCISE_NAME_TAG = "exercise_name";
    public static final String SERIES_TWO_PANE_TAG = "two_pane";
    public static final String EXERCISE_ID = "exercise_id";

    private boolean twoPane=false;
    private SeriesAdapter adapter;
    private LoadSeriesTask loadSeriesTask;
    private FitnessDatabaseInterface databaseIf;
    private long exerciseId;
    private RecyclerView recyclerView;

    public SeriesListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseIf = FitnessApplication.getDatabaseInterface();
        //adapter = new SeriesAdapter(seriesList, (AppCompatActivity)getActivity(), this, twoPane);

        if(!getArguments().containsKey(EXERCISE_ID)){
            Log.d(TAG, "Didn't receive exercis id.");
            return;
        }
        exerciseId = getArguments().getLong(EXERCISE_ID);

        /* set toolbar title to the current exercise */
        if (getArguments().containsKey(SERIES_EXERCISE_NAME_TAG)) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.series_list_activity_toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(SERIES_EXERCISE_NAME_TAG));
            }
        }
        else
        {
            Log.d(TAG, "Didn't receive exercise name.");
        }
        if(getArguments().containsKey(SERIES_TWO_PANE_TAG)){
            twoPane = getArguments().getBoolean(SERIES_TWO_PANE_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.series_list, container, false);
        recyclerView = (RecyclerView) rootView;
        //exListRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        refreshList();
        return rootView;
    }

    /** Adds new serie received as parameter from CreateExerciseFragment through activity */
    public void addSerie(Series series){
        databaseIf.insertNewSeries(series, exerciseId);
        refreshList();
    }

    /** Fetches data from database */
    private void refreshList(){
        if (loadSeriesTask != null) {
            loadSeriesTask.cancel(false);
        }

        loadSeriesTask = new LoadSeriesTask(this, databaseIf, exerciseId);
        loadSeriesTask.execute();
    }

    /** */
    public void removeSerie(long seriesId){
        databaseIf.deleteSeries(seriesId);
        refreshList();
    }

    public void showSeries(Cursor cursor) {
        adapter = new SeriesAdapter(cursor, (AppCompatActivity) getActivity(), this, twoPane);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void saveCurrentSeries(){
        Cursor currentCursor = adapter.getCursor();
        try{
            while(currentCursor.moveToNext()){
                Series currentSeries =
            }
        }
        catch (Exception e){
            Log.d(TAG, "Couldn't save all series.");
            e.printStackTrace();
        }
        finally {
            currentCursor.close();
        }
    }
}
