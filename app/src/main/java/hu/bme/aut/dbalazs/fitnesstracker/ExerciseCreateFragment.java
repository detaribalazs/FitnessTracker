package hu.bme.aut.dbalazs.fitnesstracker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ExerciseCreateFragment extends DialogFragment {

    public static final String TAG = "ExerciseCreateFragment";
    private CreateExerciseListener listener;
    private Context context;

    public interface CreateExerciseListener{
        void onExerciseCreated(String exName);
    }

    public ExerciseCreateFragment() {
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        if (getTargetFragment() != null) {
            try {
                listener = (CreateExerciseListener) getTargetFragment();
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        } else {
            try {
                listener = (CreateExerciseListener) context;
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Parent Activity does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.add_exercise_fragment, container, false);
        getDialog().setTitle(R.string.addWorkoutTitle);

        rootView.findViewById(R.id.exerciseAddOkBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText exName = rootView.findViewById(R.id.exerciseAddET);
                listener.onExerciseCreated(exName.getText().toString());
                dismiss();
            }
        });

        rootView.findViewById(R.id.exerciseAddCancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return rootView;
    }
}
