package hu.bme.aut.dbalazs.fitnesstracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.adapter.SeriesAdapter;
import hu.bme.aut.dbalazs.fitnesstracker.model.Series;


public class SeriesListFragment extends Fragment {
    public static final String SERIES_EXERCISE_NAME_TAG = "exercise_name";
    public static final String SERIES_TWO_PANE_TAG = "two_pane";

    private ArrayList<Series> seriesList;
    private boolean twoPane=false;
    private SeriesAdapter adapter;

    public SeriesListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seriesList = createSeriesList();
        adapter = new SeriesAdapter(seriesList, (AppCompatActivity)getActivity(), this, twoPane);

        /* set toolbar title to the current exercise */
        if (getArguments().containsKey(SERIES_EXERCISE_NAME_TAG)) {
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.series_list_activity_toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(SERIES_EXERCISE_NAME_TAG));
            }
        }
        if(getArguments().containsKey(SERIES_TWO_PANE_TAG)){
            twoPane = getArguments().getBoolean(SERIES_TWO_PANE_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.series_list, container, false);
        RecyclerView seriesListRV = (RecyclerView) rootView;
        //exListRV.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        seriesListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        seriesListRV.setAdapter(adapter);
        return rootView;
    }

    public void removeSerie(int position){
        seriesList.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void addSerie(Series newSerie){
        seriesList.add(newSerie);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Series> createSeriesList()
    {
        ArrayList<Series> tmpList = new ArrayList<Series>();
        tmpList.add(new Series(35, 8, 1));
        tmpList.add(new Series(35, 10, 2));
        tmpList.add(new Series(40, 8, 2));
        tmpList.add(new Series(45, 6, 2));
        return tmpList;
    }
}
