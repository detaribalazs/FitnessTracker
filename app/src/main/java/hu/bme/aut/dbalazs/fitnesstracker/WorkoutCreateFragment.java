package hu.bme.aut.dbalazs.fitnesstracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Spinner;

import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;


public class WorkoutCreateFragment extends DialogFragment {
    public static final String TAG = "WorkoutCreateFragment";

    private Spinner spinner;
    private DatePicker datePicker;
    private WorkoutCreatedI listener;

    public interface WorkoutCreatedI {
        public void onWorkoutCreated(Workout newWo);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (getTargetFragment() != null) {
            try {
                listener = (WorkoutCreatedI) getTargetFragment();
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        } else {
            try {
                listener = (WorkoutCreatedI) context;
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Parent Activity does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        
    }

}
