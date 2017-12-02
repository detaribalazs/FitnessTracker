package hu.bme.aut.dbalazs.fitnesstracker.adapter;

import android.util.Log;
import android.widget.NumberPicker;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.model.Series;

public class SeriesListener {

    public static final String TAG = "SeriesListener";

    private ArrayList<NumberPickerListener> weightListener;
    private ArrayList<NumberPickerListener> repsListener;
    private ArrayList<Series> seriesList;


    private enum ListenerType {
        WEIGHT_LISTENER,
        REPS_LISTENER
    }


    public SeriesListener() {
        seriesList = new ArrayList<>();
        weightListener = new ArrayList<>();
        repsListener = new ArrayList<>();
    }

    public ArrayList<Series> getSeriesList(){
        return this.seriesList;
    }

    public void setSeries(Series series, int position){
        if(seriesList.size() < position + 1){
            seriesList.add(position, series);
        }
        seriesList.set(position, series);
    }

    public NumberPickerListener getWeightListener(int p) {
        if(weightListener.size() < p + 1) {
            weightListener.add(p, new NumberPickerListener(seriesList.get(p), ListenerType.WEIGHT_LISTENER, p));
        }
        else{
            weightListener.set(p, new NumberPickerListener(seriesList.get(p), ListenerType.WEIGHT_LISTENER, p));
        }
        return weightListener.get(p);
    }

    public NumberPickerListener getRepsListener(int p) {
        if(repsListener.size() < p + 1) {
            repsListener.add(p, new NumberPickerListener(seriesList.get(p), ListenerType.REPS_LISTENER, p));
        }
        else{
            repsListener.set(p, new NumberPickerListener(seriesList.get(p), ListenerType.REPS_LISTENER, p));
        }
        return repsListener.get(p);
    }

    private class NumberPickerListener implements NumberPicker.OnValueChangeListener {

        private static final String TAG = "NumberPickerListener";

        private Series series;
        private ListenerType type;
        private int position;

        public NumberPickerListener(Series series, ListenerType type, int p) {
            this.type = type;
            this.series = series;
            this.position = p;
        }

        @Override
        public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
            switch(type){
                case REPS_LISTENER:
                    series.setReps(newValue);
                    Log.d(TAG, "reps updated at position: " + position + ", " + newValue );
                    break;
                case WEIGHT_LISTENER:
                    series.setWeight(newValue);
                    Log.d(TAG, "weight updated: " + position + ", " + newValue);
                    break;
            }
        }
    }
}
