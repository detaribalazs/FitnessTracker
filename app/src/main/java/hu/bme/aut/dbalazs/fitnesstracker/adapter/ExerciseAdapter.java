package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.model.Exercise;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ArrayList<Exercise> exerciseList;
    private AppCompatActivity activity;

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

    public ExerciseAdapter(ArrayList<Exercise> exList, AppCompatActivity activity){
        this.exerciseList = exList;
        this.activity = activity;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new ExerciseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        String reps = "" + exerciseList.get(position).getReps();
        holder.exerciseRepsTV.setText(reps);
        holder.exerciseTypeTV.setText(exerciseList.get(position).getName());

        holder.exerciseFrameRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

}
