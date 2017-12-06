package hu.bme.aut.dbalazs.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Fitness Tracker");
        }

        CardView workoutCv = (CardView) findViewById(R.id.MainWorkoutSelect);
        workoutCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), WorkoutListActivity.class);
                startActivity(i);
            }
        });

        CardView runningCv = (CardView) findViewById(R.id.MainRunningSelect);
        runningCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RunningTrackerActivity.class);
                startActivity(i);
            }
        });
    }
}
