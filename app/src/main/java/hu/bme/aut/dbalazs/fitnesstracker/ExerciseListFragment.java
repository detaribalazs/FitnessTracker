package hu.bme.aut.dbalazs.fitnesstracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hu.bme.aut.dbalazs.fitnesstracker.adapter.ExerciseAdapter;
import hu.bme.aut.dbalazs.fitnesstracker.model.Exercise;
import hu.bme.aut.dbalazs.fitnesstracker.model.Series;
import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;

public class ExerciseListFragment extends Fragment {

    public static final String EXERCISE_ID = "exercise_id";
    public static final String EXERCISE_TYPE = "exercise_type";
    public static final String EXERCISE_DATE = "exercise_date";
    public static final String TWO_PANE_TAG = "two_pane";
    // TODO create database query according to this id from exercise table

    private ArrayList<Exercise> exerciseList; //TODO init this with query
    private ExerciseAdapter adapter;
    private boolean twoPane = false;

    public ExerciseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ExerciseAdapter(createExerciseList(), (AppCompatActivity)getActivity(), twoPane);

        if (getArguments().containsKey(EXERCISE_TYPE) && getArguments().containsKey(EXERCISE_DATE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                String title = Workout.typeToString(Workout.WorkoutType.valueOf(getArguments().getString(EXERCISE_TYPE)));
                Date date = new Date(getArguments().getLong(EXERCISE_DATE));
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
        RecyclerView exListRV = (RecyclerView) rootView;
        //exListRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        exListRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        exListRV.addItemDecoration(itemDecoration);
        exListRV.setAdapter(adapter);
        return rootView;
    }

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

    private ArrayList<Exercise> createExerciseList(){
        ArrayList<Series> s1 = new ArrayList<Series>();
        s1.add(new Series(50, 8, 1));
        s1.add(new Series(50, 8, 2));
        s1.add(new Series(50, 8, 3));
        s1.add(new Series(50, 8, 4));

        ArrayList<Series> s2 = new ArrayList<Series>();
        s2.add(new Series(70, 6, 5));
        s2.add(new Series(50, 7, 6));
        s2.add(new Series(50, 8, 7));

        ArrayList<Exercise> exList = new ArrayList<Exercise>();
        exList.add(new Exercise(s1, "Fekvenyomás"));
        exList.add(new Exercise(s2, "Felülés"));
        exList.add(new Exercise(s1, "Tárogatás"));
        exList.add(new Exercise(s2, "Gugolás"));

        return exList;
    }

    // TODO delete
    private ArrayList<Workout> createWorkoutList(){
        ArrayList<Workout> wo = new ArrayList<Workout>();
        wo.add(new Workout(Workout.WorkoutType.ARM_WORKOUT, null, new Date().getTime()));
        wo.add(new Workout(Workout.WorkoutType.CHEST_WORKOUT, null, new Date().getTime()));
        wo.add(new Workout(Workout.WorkoutType.SHOULDER_WORKOUT, null, new Date().getTime()));
        wo.add(new Workout(Workout.WorkoutType.LEG_WORKOUT, null, new Date().getTime()));
        return wo;
    }
}
