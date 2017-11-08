package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.WorkoutDetailActivity;
import hu.bme.aut.dbalazs.fitnesstracker.WorkoutDetailFragment;
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
        //inflater.inflate(R.layout.workout_list_content, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_content, parent, false);
        return new WorkoutViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WorkoutViewHolder holder, int position) {
        holder.woItem = workoutList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, EEE");
        String date = sdf.format(workoutList.get(position).getWoDate());
        holder.woDateTv.setText(date);
        String workoutType;
        switch (workoutList.get(position).getWoType()){
            case ARM_WORKOUT:
                workoutType = "Arm";
                break;
            case BACK_WORKOUT:
                workoutType = "Back";
                break;
            case CHEST_WORKOUT:
                workoutType = "Chest";
                break;
            case SHOULDER_WORKOUT:
                workoutType = "Shoulder";
                break;
            case LEG_WORKOUT:
                workoutType = "Leg";
                break;
            default:
                workoutType = "Unknown";
                break;
        }
        holder.woTypeTV.setText(workoutType);
        holder.woFrameRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(WorkoutDetailFragment.ARG_ITEM_ID, holder.woItem.getWoType().name());
                    WorkoutDetailFragment fragment = new WorkoutDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.workout_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, WorkoutDetailActivity.class);
                    intent.putExtra(WorkoutDetailFragment.ARG_ITEM_ID, holder.woItem.getWoType().name());

                    context.startActivity(intent);
                }
            }
        });
        holder.woFrameRL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }
}
