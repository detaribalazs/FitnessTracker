package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.database.Cursor;
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

import java.util.List;

import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.SeriesListFragment;
import hu.bme.aut.dbalazs.fitnesstracker.database.FitnessDatabaseInterface;
import hu.bme.aut.dbalazs.fitnesstracker.model.Series;

public class SeriesAdapter extends CursorRecyclerViewAdapter<SeriesAdapter.SeriesViewHolder> {
    private Cursor cursor;
    private AppCompatActivity activity;
    private Fragment fragment;
    private boolean twoPane;
    private SeriesListener seriesListener;

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

        public void setListeners(int position){
            repNP.setOnValueChangedListener(seriesListener.getRepsListener(position));
            weightNP.setOnValueChangedListener(seriesListener.getWeightListener(position));
        }
    }

    public SeriesAdapter(Cursor cursor, AppCompatActivity activity, Fragment fragment, boolean twoPane){
        super(activity, cursor);
        this.activity = activity;
        this.twoPane = twoPane;
        this.fragment = fragment;
        this.cursor = cursor;
        seriesListener = new SeriesListener();
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_item, parent, false);
        return new SeriesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SeriesViewHolder holder, Cursor cursor) {
        final Series currentSerie = FitnessDatabaseInterface.createSeriesFromCursor(cursor);
        holder.weightNP.setValue(currentSerie.getWeight());
        holder.repNP.setValue(currentSerie.getReps());
        holder.number.setText("" + (cursor.getPosition() + 1) + ".");

        seriesListener.setSeries(currentSerie, holder.getAdapterPosition());
        holder.setListeners(holder.getAdapterPosition());

        holder.frameCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.workout_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (R.id.workoutDeleteDel == item.getItemId()) {
                            ((SeriesListFragment)fragment).removeSerie(currentSerie.getId());
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
    }

    public List<Series> getSeriesList(){
        return seriesListener.getSeriesList();
    }
}
