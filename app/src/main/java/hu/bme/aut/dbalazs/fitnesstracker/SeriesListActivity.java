package hu.bme.aut.dbalazs.fitnesstracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/* activity for showing the Series belonging to a single exercise */
public class SeriesListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.series_list_activity);
        getSupportActionBar().hide();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.series_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /* adding new serie */
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            /* set exercise name and two pane flag for list fragment*/
            Bundle arguments = new Bundle();
            arguments.putString(SeriesListFragment.SERIES_EXERCISE_NAME_TAG,
                    getIntent().getStringExtra(SeriesListFragment.SERIES_EXERCISE_NAME_TAG));
            arguments.putBoolean(SeriesListFragment.SERIES_TWO_PANE_TAG, false);
            SeriesListFragment fragment = new SeriesListFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.series_list_fragment, fragment)
                    .commit();
        }
    }


}
