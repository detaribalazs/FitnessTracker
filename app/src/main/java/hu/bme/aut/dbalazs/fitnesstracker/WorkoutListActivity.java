package hu.bme.aut.dbalazs.fitnesstracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import hu.bme.aut.dbalazs.fitnesstracker.adapter.WorkoutAdapter;
import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;

public class WorkoutListActivity extends AppCompatActivity implements WorkoutCreateFragment.CreateWorkoutListener, DatePickerDialog.OnDateSetListener {

    private boolean mTwoPane;
    private ArrayList<Workout> woList;
    private WorkoutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_list_activity);

        if (findViewById(R.id.exercise_list_fragment) != null) {
            mTwoPane = true;
        }
        else{
            mTwoPane = false;
        }
        if(!mTwoPane) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.workoutFab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WorkoutCreateFragment fragment = new WorkoutCreateFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().add(fragment, WorkoutCreateFragment.TAG)
                            .addToBackStack(null)
                            .show(fragment)
                            .commit();
                }
            });
        }
        else{
            findViewById(R.id.workout_list_add_twoPane).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WorkoutCreateFragment fragment = new WorkoutCreateFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().add(fragment, WorkoutCreateFragment.TAG)
                            .addToBackStack(null)
                            .show(fragment)
                            .commit();
                }
            });
        }
        View recyclerView = findViewById(R.id.workout_list);
        assert recyclerView != null;
        woList = createWorkoutList();

        setupRecyclerView((RecyclerView) recyclerView, woList);
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, ArrayList<Workout> woList) {
        adapter = new WorkoutAdapter(woList, mTwoPane, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onWorkoutCreated(Workout newWo) {
        woList.add(newWo);
        //adapter.addWorkout(newWo);
        adapter.notifyDataSetChanged();
        if(mTwoPane) {
            Snackbar.make(findViewById(android.R.id.content), "New Workout added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            Snackbar.make(findViewById(R.id.workoutFab), "New Workout added", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void removeWorkout(int position){
        woList.remove(position);
        adapter.notifyDataSetChanged();
        if(mTwoPane) {
            Snackbar.make(findViewById(android.R.id.content), "Workout removed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            Snackbar.make(findViewById(R.id.workoutFab), "Workout removed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        WorkoutCreateFragment currentFragment = (WorkoutCreateFragment) getSupportFragmentManager().findFragmentByTag(WorkoutCreateFragment.TAG);
        TextView dateTV = currentFragment.getView().getRootView().findViewById(R.id.workoutAddDateTV);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0);
        Date date = c.getTime();
        currentFragment.setDate(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d. EEE");
        dateTV.setText(sdf.format(date));
    }
}
