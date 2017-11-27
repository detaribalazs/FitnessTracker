package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import hu.bme.aut.dbalazs.fitnesstracker.ExerciseListActivity;
import hu.bme.aut.dbalazs.fitnesstracker.ExerciseListFragment;
import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.WorkoutListActivity;
import hu.bme.aut.dbalazs.fitnesstracker.database.FitnessDatabaseInterface;
import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;

public class WorkoutAdapter extends CursorRecyclerViewAdapter<WorkoutAdapter.WorkoutViewHolder> {
    public static String WORKOUT_ID_TAG = "workout_id_tag";

    private boolean mTwoPane;
    private Context context;

    public class WorkoutViewHolder extends ViewHolder{

        public TextView woTypeTV;
        public TextView woDateTv;
        public RelativeLayout woFrameRL;
        public Workout woItem;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            woTypeTV = (TextView) itemView.findViewById(R.id.workoutTypeTV);
            woDateTv = (TextView) itemView.findViewById(R.id.workoutDateTV);
            woFrameRL = (RelativeLayout) itemView.findViewById(R.id.workoutFrameRL);
        }
    }

    public WorkoutAdapter(Context context, Cursor cursor, boolean twoPane) {
        super(context, cursor);
        this.mTwoPane = twoPane;
        this.context = context;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new WorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WorkoutViewHolder holder, Cursor cursor) {
        Workout currentWorkout = FitnessDatabaseInterface.getTodoByCursor(cursor);
        holder.woItem = currentWorkout;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, EEE");
        String date = sdf.format(currentWorkout.getWoDate());
        holder.woDateTv.setText(date);
        String workoutType = Workout.typeToString(currentWorkout.getWoType());
        holder.woTypeTV.setText(workoutType + " training");
        holder.woFrameRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ExerciseListFragment.EXERCISE_TYPE, holder.woItem.getWoType().name());
                    arguments.putLong(ExerciseListFragment.EXERCISE_DATE, holder.woItem.getWoDate().getTime());
                    // set the workout ID too
                    arguments.putLong(WORKOUT_ID_TAG, holder.woItem.getId());
                    arguments.putBoolean(ExerciseListFragment.TWO_PANE_TAG, mTwoPane);
                    ExerciseListFragment fragment = new ExerciseListFragment();
                    fragment.setArguments(arguments);
                    ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.exercise_list_fragment, fragment)
                            .commit();
                } else {
                    // set the workout ID too
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ExerciseListActivity.class);
                    intent.putExtra(ExerciseListFragment.EXERCISE_TYPE, holder.woItem.getWoType().name());
                    intent.putExtra(ExerciseListFragment.EXERCISE_DATE, holder.woItem.getWoDate().getTime());
                    intent.putExtra(WORKOUT_ID_TAG, holder.woItem.getId());
                    context.startActivity(intent);
                }
            }
        });
        holder.woFrameRL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.workout_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (R.id.workoutDeleteDel == item.getItemId()) {
                            ((WorkoutListActivity)context).removeWorkout(holder.woItem.getId());
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
    }
}


