package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.ExerciseListActivity;
import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.SeriesListActivity;
import hu.bme.aut.dbalazs.fitnesstracker.SeriesListFragment;
import hu.bme.aut.dbalazs.fitnesstracker.WorkoutListActivity;
import hu.bme.aut.dbalazs.fitnesstracker.model.Exercise;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ArrayList<Exercise> exerciseList;
    private AppCompatActivity activity;
    private boolean twoPane;

    public class ExerciseViewHolder extends RecyclerView.ViewHolder{
        public TextView exerciseTypeTV;
        public TextView exerciseRepsTV;
        public RelativeLayout exerciseFrameRL;
        public Exercise woItem;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            exerciseTypeTV = itemView.findViewById(R.id.exerciseTypeTV);
            exerciseRepsTV = itemView.findViewById(R.id.exerciseRepsTV);
            exerciseFrameRL = itemView.findViewById(R.id.exerciseFrameRL);
        }
    }

    public ExerciseAdapter(ArrayList<Exercise> exList, AppCompatActivity activity, boolean twoPane){
        this.exerciseList = exList;
        this.activity = activity;
        this.twoPane = twoPane;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, final int position) {
        String reps = "" + exerciseList.get(position).getReps();
        holder.exerciseRepsTV.setText(reps);
        holder.exerciseTypeTV.setText(exerciseList.get(position).getName());

        holder.exerciseFrameRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start different activities according to the display size
                if(!twoPane) {
                    Intent intent = new Intent(activity, SeriesListActivity.class);
                    intent.putExtra(SeriesListFragment.SERIES_EXERCISE_NAME_TAG, exerciseList.get(position).getName());
                    activity.startActivity(intent);//, ExerciseListActivity.EXERCISE_LIST_ACTVITY_REQUEST_CODE);
                }
                else {
                    /*
                    Intent intent = new Intent(activity, TwoPaneExerciseListActivity.class);
                    intent.putExtra(SeriesListFragment.SERIES_EXERCISE_NAME_TAG, exerciseList.get(position).getName());
                    activity.startActivity(intent);
                    */
                    Toast.makeText(activity, "Two pane activity still not ready", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.exerciseFrameRL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.workout_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (R.id.workoutDeleteDel == item.getItemId()) {
                            ((ExerciseListActivity)activity).removeExercise(position);
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

}
