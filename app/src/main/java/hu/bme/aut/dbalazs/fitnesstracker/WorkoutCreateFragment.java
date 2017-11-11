package hu.bme.aut.dbalazs.fitnesstracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import hu.bme.aut.dbalazs.fitnesstracker.model.Workout;


public class WorkoutCreateFragment extends DialogFragment {
    public static final String TAG = "WorkoutCreateFragment";

    private CreateWorkoutListener listener;
    private Context context;
    private Date woDate;

    public void setDate(Date date) {
        this.woDate = date;
    }

    public interface CreateWorkoutListener {
        public void onWorkoutCreated(Workout newWo);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        if (getTargetFragment() != null) {
            try {
                listener = (CreateWorkoutListener) getTargetFragment();
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        } else {
            try {
                listener = (CreateWorkoutListener) context;
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.add_workout_fragment, container, false);

        getDialog().setTitle(R.string.addWorkoutTitle);

        final TextView date = rootView.findViewById(R.id.workoutAddDateTV);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        final Spinner spinner = rootView.findViewById(R.id.workoutAddSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.workout_types, R.layout.workout_add_spinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Button okBtn = rootView.findViewById(R.id.workoutAddOkBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Workout.WorkoutType woType;
                switch (spinner.getSelectedItemPosition())
                {
                    case 0:
                        woType = Workout.WorkoutType.ARM_WORKOUT;
                        break;
                    case 1:
                        woType = Workout.WorkoutType.BACK_WORKOUT;
                        break;
                    case 2:
                        woType = Workout.WorkoutType.CHEST_WORKOUT;
                        break;
                    case 3:
                        woType = Workout.WorkoutType.LEG_WORKOUT;
                        break;
                    case 4:
                        woType = Workout.WorkoutType.SHOULDER_WORKOUT;
                        break;
                    default:
                        woType = Workout.WorkoutType.BACK_WORKOUT;
                }

                Workout newWo = new Workout(woType, null, woDate);
                listener.onWorkoutCreated(newWo);
                dismiss();
            }
        });

        Button cancelBtn = rootView.findViewById(R.id.workoutAddCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootView;
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();

        Calendar calender = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calender = Calendar.getInstance();
            Bundle args = new Bundle();
            args.putInt("year", calender.get(Calendar.YEAR));
            args.putInt("month", calender.get(Calendar.MONTH));
            args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
            date.setArguments(args);
        }
        date.show(getChildFragmentManager(), "Date Picker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public static String DATE_PICKER_KEY = "text_view";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int year,month, day;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                final Calendar c;
                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }
            else{
                final Date d = new Date();
                year = d.getYear();
                month = d.getMonth();
                day = d.getDay();
            }

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((WorkoutListActivity)getContext()).onDateSet(view, year, month, day);
        }

    }
}
