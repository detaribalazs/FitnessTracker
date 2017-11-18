package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.SeriesListFragment;
import hu.bme.aut.dbalazs.fitnesstracker.model.Series;

/**
 * Created by Balazs on 2017. 11. 11..
 */

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {
    private AppCompatActivity activity;
    private Fragment fragment;
    private ArrayList<Series> seriesList;
    private int exerciseId;
    private boolean twoPane;

    public class SeriesViewHolder extends RecyclerView.ViewHolder{
        public NumberPicker repNP;
        public NumberPicker weightNP;
        public CardView frameCV;
        public TextView number;
        public Series serieItem;

        public SeriesViewHolder(View itemView) {
            super(itemView);
            repNP = itemView.findViewById(R.id.exerciseRep);
            repNP.setMaxValue(50);
            repNP.setMinValue(0);
            weightNP = itemView.findViewById(R.id.exerciseWeight);
            weightNP.setMinValue(0);
            weightNP.setMaxValue(250);
            frameCV = itemView.findViewById(R.id.series_item_card);
            number = itemView.findViewById(R.id.series_number);
        }
    }

    public SeriesAdapter(ArrayList<Series> series, AppCompatActivity activity, Fragment fragment, boolean twoPane){
        this.activity = activity;
        this.seriesList = series;
        this.twoPane = twoPane;
        this.fragment = fragment;
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_item, parent, false);
        return new SeriesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SeriesViewHolder holder, final int position) {
        holder.weightNP.setValue(seriesList.get(position).getWeight());
        holder.repNP.setValue(seriesList.get(position).getReps());
        holder.number.setText("" + (position + 1) + ".");
        holder.frameCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.workout_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (R.id.workoutDeleteDel == item.getItemId()) {
                            ((SeriesListFragment)fragment).removeSerie(position);
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
        return seriesList.size();
    }

}
