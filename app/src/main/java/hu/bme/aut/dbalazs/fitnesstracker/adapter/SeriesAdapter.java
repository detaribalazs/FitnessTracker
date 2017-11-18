package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.model.Exercise;
import hu.bme.aut.dbalazs.fitnesstracker.model.Series;

/**
 * Created by Balazs on 2017. 11. 11..
 */

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {
    private AppCompatActivity activity;
    private ArrayList<Series> seriesList;
    private int exerciseId;

    public class SeriesViewHolder extends RecyclerView.ViewHolder{
        public TextView exerciseTypeTV;
        public TextView exerciseRepsTV;
        public RelativeLayout exerciseFrameRL;
        public Exercise woItem;

        public SeriesViewHolder(View itemView) {
            super(itemView);
            exerciseTypeTV = itemView.findViewById(R.id.exerciseTypeTV);
            exerciseRepsTV = itemView.findViewById(R.id.exerciseRepsTV);
            exerciseFrameRL = itemView.findViewById(R.id.exerciseFrameRL);
        }
    }

    public SeriesAdapter(ArrayList<Series> series, AppCompatActivity activity){
        this.activity = activity;
        this.seriesList = series;
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        return new SeriesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SeriesViewHolder holder, int position) {
    }

    /*
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
*/
    @Override
    public int getItemCount() {
        return seriesList.size();
    }

}
