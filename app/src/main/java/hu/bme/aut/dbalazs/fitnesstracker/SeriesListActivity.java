package hu.bme.aut.dbalazs.fitnesstracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.model.Series;

/* activity for showing the Series belonging to a single exercise */
public class SeriesListActivity extends AppCompatActivity {

    private ArrayList<Series> seriesList;
    private SeriesListFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragment = new SeriesListFragment();
        setContentView(R.layout.series_list_activity);
        getSupportActionBar();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.series_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            /* adding new serie */
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "New serie added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fragment.addSerie(new Series(0, 0));
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
            arguments.putLong(SeriesListFragment.EXERCISE_ID, getIntent().getLongExtra(SeriesListFragment.EXERCISE_ID, -1));
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.series_list_fragment, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.series_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //TODO save here
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
