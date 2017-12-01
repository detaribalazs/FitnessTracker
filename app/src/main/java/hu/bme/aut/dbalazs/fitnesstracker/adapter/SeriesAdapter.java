package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

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

        public SeriesViewHolder(View itemView, SeriesListener listener) {
            super(itemView);
            repNP = itemView.findViewById(R.id.exerciseRep);
            repNP.setMaxValue(50);
            repNP.setMinValue(0);
            repNP.setOnValueChangedListener(listener.getRepsListener());
            weightNP = itemView.findViewById(R.id.exerciseWeight);
            weightNP.setMinValue(0);
            weightNP.setMaxValue(250);
            weightNP.setOnValueChangedListener(listener.getWeightListener());
            frameCV = itemView.findViewById(R.id.series_item_card);
            number = itemView.findViewById(R.id.series_number);
        }
    }

    public SeriesAdapter(Cursor cursor, AppCompatActivity activity, Fragment fragment, boolean twoPane){
        super(activity, cursor);
        this.activity = activity;
        this.twoPane = twoPane;
        this.fragment = fragment;
        this.cursor = cursor;
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_item, parent, false);
        return new SeriesViewHolder(v, seriesListener);
    }

    @Override
    public void onBindViewHolder(SeriesViewHolder holder, Cursor cursor) {
        final Series currentSerie = FitnessDatabaseInterface.createSeriesFromCursor(cursor);
        holder.weightNP.setValue(currentSerie.getWeight());
        holder.repNP.setValue(currentSerie.getReps());
        holder.number.setText("" + (cursor.getPosition() + 1) + ".");
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

    public class SeriesListener {

        public static final String TAG = "SeriesListener";

        private NumberPickerListener weightListener;
        private NumberPickerListener repsListener;

        public SeriesListener(){
            weightListener = new NumberPickerListener();
            repsListener = new NumberPickerListener();
        }

        public SeriesListener(int count){
            weightListener = new NumberPickerListener(count);
            repsListener = new NumberPickerListener(count);
        }

        public NumberPickerListener getWeightListener() {
            return weightListener;
        }

        public NumberPickerListener getRepsListener() {
            return repsListener;
        }

        public void addElement(int position){
            weightListener.addElement(int position);
            repsListener.addElement(int position);
        }

        public int getPosition(){
            if(weightListener.getPosition() == repsListener.getPosition()){
                Log.e(TAG, "Unbalanced position");
            }
            return weightListener.getPosition();
        }

        public void setPosition(int p){
            weightListener.setPosition(p);
            repsListener.setPosition(p);
        }

        private class NumberPickerListener implements NumberPicker.OnValueChangeListener {

            private ArrayList<Integer> dataSet;
            private int position;

            public NumberPickerListener() {
                this.position = 0;
            }

            private void setCapacity(int count){
                dataSet = new ArrayList<Integer>(count);
            }

            private void setPosition(int p) {
                this.position = p;
            }

            private int getPosition() {
                return this.position;
            }

            private void addElement(int position) {
                if(dataSet.size() < position){
                    while(dataSet.size() < position){
                        dataSet.add(0);
                    }
                }
                dataSet.add(0);
            }

            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                dataSet.set(position, newValue);
            }
        }
    }
}
