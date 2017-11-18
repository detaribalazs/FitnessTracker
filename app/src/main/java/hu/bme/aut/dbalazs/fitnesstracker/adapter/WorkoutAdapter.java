package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import hu.bme.aut.dbalazs.fitnesstracker.ExerciseListActivity;
import hu.bme.aut.dbalazs.fitnesstracker.ExerciseListFragment;
import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.WorkoutListActivity;
import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private final List<Workout> workoutList;
    private boolean mTwoPane;
    private AppCompatActivity activity;

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

    public WorkoutAdapter(List<Workout> workoutList, boolean twoPane, AppCompatActivity activity) {
        this.workoutList = workoutList;
        this.mTwoPane = twoPane;
        this.activity = activity;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater.inflate(R.layout.workout_item, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        return new WorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WorkoutViewHolder holder, final int position) {
        holder.woItem = workoutList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, EEE");
        String date = sdf.format(workoutList.get(position).getWoDate());
        holder.woDateTv.setText(date);
        String workoutType = Workout.typeToString(workoutList.get(position).getWoType());
        holder.woTypeTV.setText(workoutType + " training");
        holder.woFrameRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ExerciseListFragment.EXERCISE_TYPE, holder.woItem.getWoType().name());
                    arguments.putLong(ExerciseListFragment.EXERCISE_DATE, holder.woItem.getWoDate().getTime());
                    arguments.putBoolean(ExerciseListFragment.TWO_PANE_TAG, mTwoPane);
                    ExerciseListFragment fragment = new ExerciseListFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.exercise_list_fragment, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ExerciseListActivity.class);
                    intent.putExtra(ExerciseListFragment.EXERCISE_TYPE, holder.woItem.getWoType().name());
                    intent.putExtra(ExerciseListFragment.EXERCISE_DATE, holder.woItem.getWoDate().getTime());

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
                            ((WorkoutListActivity)activity).removeWorkout(position);
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
        return workoutList.size();
    }
}
