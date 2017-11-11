package hu.bme.aut.dbalazs.fitnesstracker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.aut.dbalazs.fitnesstracker.model.Exercise;

/**
 * Created by Balazs on 2017. 11. 11..
 */

public class ExerciseDetailFragment extends Fragment {

    public static String EXERCISE_DETAIL_TAG = "exercise_detail";
    public static String EXERCISE_ID = "exercise_id";
    private Exercise exercise;
    private Context context;
    private int exerciseId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.exerciseId = getArguments().getInt(EXERCISE_ID);
        Bundle args = new Bundle();

        // show details after querying data
        if(exerciseId != -1){

        }
        // show empty exercise
        else{

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.exercise_fragment, container, false);
        rootView.findViewById(R.id.exerciseNameTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return rootView;
    }
}
