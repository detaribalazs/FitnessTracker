package hu.bme.aut.dbalazs.fitnesstracker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

import hu.bme.aut.dbalazs.fitnesstracker.adapter.ExerciseAdapter;
import hu.bme.aut.dbalazs.fitnesstracker.application.FitnessApplication;
import hu.bme.aut.dbalazs.fitnesstracker.database.FitnessDatabaseInterface;
import hu.bme.aut.dbalazs.fitnesstracker.database.LoadExercisesTask;
import hu.bme.aut.dbalazs.fitnesstracker.model.Exercise;
import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;

/** This fragment shows the recycler view filled with data from local database. This class is responsible
 *  for managing database operations and showing data. */
public class ExerciseListFragment extends Fragment {

    private static final String TAG = "ExerciseListFragment";
    public static final String WORKOUT_ID = "exercise_id";
    public static final String WORKOUT_TYPE = "exercise_type";
    public static final String WORKOUT_DATE = "exercise_date";
    public static final String TWO_PANE_TAG = "two_pane";

    private ExerciseAdapter adapter;
    private boolean twoPane = false;
    private AppCompatActivity activity;
    private FitnessDatabaseInterface databaseIf = FitnessApplication.getDatabaseInterface();
    private RecyclerView exListRV;
    private LoadExercisesTask loadExercisesTask;
    private long workoutId;

    public ExerciseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getArguments().containsKey(WORKOUT_ID)){
            Log.d(TAG, "Workout id not received");
        }
        workoutId = getArguments().getLong(WORKOUT_ID);
        if(workoutId == -1){
            Log.d(TAG, "Workout id not received");
        }
        // set workout type and workout date in the toolbar
        if (getArguments().containsKey(WORKOUT_TYPE) && getArguments().containsKey(WORKOUT_DATE)) {
            activity = (AppCompatActivity) this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                String title = Workout.typeToString(Workout.WorkoutType.valueOf(getArguments().getString(WORKOUT_TYPE)));
                Date date = new Date(getArguments().getLong(WORKOUT_DATE));
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d. EEE");
                title += ", " + sdf.format(date);
                appBarLayout.setTitle(title);//mItem.content
            }
        }
        /* activity sent two pane flag */
        if(getArguments().containsKey(TWO_PANE_TAG)){
            this.twoPane = getArguments().getBoolean(TWO_PANE_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.exercise_list, container, false);            //workout_detail
        exListRV = (RecyclerView) rootView;
        //exListRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        refreshList();
        return rootView;
    }

    /** Sets the offset between cards on UI */
    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    /** Adds new exercise received as parameter from CreateExerciseFragment through activity */
    public void addExercise(Exercise exercise){
        databaseIf.insertExercise(exercise, workoutId);
        refreshList();
    }

    /** Removes exercise with given ID from database */
    public void removeExercise(long exId){
        databaseIf.removeExercise(exId);
        refreshList();
    }

    /** Creates and sets a new adapter to recycler view. Called by LoadExercisesTask on post execute with new data. */
    public void showExercises(Cursor cursor){
        adapter = new ExerciseAdapter(cursor, activity, this, twoPane);
        if(!twoPane) {
            exListRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else{
            exListRV.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        exListRV.addItemDecoration(itemDecoration);
        exListRV.setAdapter(adapter);
    }

    /** Fetches data from database */
    private void refreshList(){
        if (loadExercisesTask != null) {
            loadExercisesTask.cancel(false);
        }

        loadExercisesTask = new LoadExercisesTask(this, databaseIf, workoutId);
        loadExercisesTask.execute();
    }


}
