package hu.bme.aut.dbalazs.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import hu.bme.aut.dbalazs.fitnesstracker.model.Exercise;

/**
 * An activity representing a single Workout detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link WorkoutListActivity}.
 */
public class ExerciseListActivity extends AppCompatActivity implements ExerciseCreateFragment.CreateExerciseListener {

    private ExerciseListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragment = new ExerciseListFragment();
        setContentView(R.layout.exercise_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.exercise_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseCreateFragment newFragment = new ExerciseCreateFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(newFragment, ExerciseCreateFragment.TAG)
                           .commit();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            /* Put exercise type and exercise date into the Bundle */
            arguments.putString(ExerciseListFragment.EXERCISE_TYPE,
                    getIntent().getStringExtra(ExerciseListFragment.EXERCISE_TYPE));
            arguments.putLong(ExerciseListFragment.EXERCISE_DATE,
                    getIntent().getLongExtra(ExerciseListFragment.EXERCISE_DATE, 0));
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.exercise_list_fragment, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, WorkoutListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExerciseCreated(String exName) {
        fragment.addExercise(new Exercise(exName));
        Snackbar.make(findViewById(android.R.id.content), "New exercise added!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void removeExercise(int position){
        fragment.removeExerciseList(position);
    }
}
