package hu.bme.aut.dbalazs.fitnesstracker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.events.ActivityStateChangedEvent;
import hu.bme.aut.dbalazs.fitnesstracker.events.ExerciseStateEvent;
import hu.bme.aut.dbalazs.fitnesstracker.events.LocationEvent;
import hu.bme.aut.dbalazs.fitnesstracker.events.LocationListEvent;
import hu.bme.aut.dbalazs.fitnesstracker.location.LocationService;

import static hu.bme.aut.dbalazs.fitnesstracker.R.id.map;

public class RunningTrackerActivity extends AppCompatActivity implements OnMapReadyCallback  {

    private static final int MY_PERMISSION_REQUEST_LOCATION = 102;
    private static final String TAG = "RunningTrackerActivity";
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private Marker currentMarker;
    private Marker startingMarker;
    private Marker endingMarker;
    private Polyline exerciseRoute;
    private long startingTime;
    private ArrayList<Location> locationList;
    private Location lastLocation;

    public enum ActivityState {
        STARTED, STOPPED
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setContentView(R.layout.running_activity);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // register to location updates
        EventBus.getDefault().register(this);
        // try to start service
        Intent i = new Intent(getApplicationContext(),LocationService.class);
        startService(i);
        // notify location service, that activity started
        EventBus.getDefault().post(new ActivityStateChangedEvent(ActivityState.STARTED));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check permissions
        handleLocationPermission();
        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        // notify location service, that UI is no longer visible
        EventBus.getDefault().post(new ActivityStateChangedEvent(ActivityState.STOPPED));
        // unregsiter from receiving events
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(locationList != null){
            drawRoute(locationList);
            locationList = null;
        }
        if(lastLocation != null){
            updateLocation(lastLocation);
            lastLocation = null;
        }
    }

    /** update marker on the map according to the new location received from LocationService
     *
      * @param location the newest location value
     */
    private void updateLocation(Location location){
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        if( currentMarker != null ){
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(position).title("Current position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.running_tracker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        else if (id == R.id.runningStart)
        {
            // get current position from the last marker and set it as starting point
            LatLng tmp = currentMarker.getPosition();
            currentMarker.remove();
            startingMarker = mMap.addMarker(new MarkerOptions().position(tmp).title("Current position"));
            Toast.makeText(getApplicationContext(), "Exercise started", Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new ExerciseStateEvent(true));
        }
        else if (id == R.id.runningEnd){
            Toast.makeText(getApplicationContext(), "Exercise over", Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new ExerciseStateEvent(false));
        }

        return super.onOptionsItemSelected(item);
    }

    /** Handles the permission for accessing the location of the device. */
    private void handleLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.locationPermissionTitle);
                alertDialogBuilder
                        .setMessage(R.string.locationPermissionMessage)
                        .setCancelable(false)
                        .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(R.string.forward, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(RunningTrackerActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSION_REQUEST_LOCATION);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_LOCATION);
            }
        }
    }

    private void drawRoute(ArrayList<Location> locationList){
        if(mMap == null){
            return;
        }
        if(locationList.size() < 2 ){
            return;
        }
        if(exerciseRoute != null)
        {
            exerciseRoute.remove();
        }
        PolylineOptions options = new PolylineOptions();

        options.color( Color.parseColor( "#CC0000FF" ) );
        options.width( 5 );
        options.visible( true );

        int i = 0;
        for ( Location loc : locationList )
        {
            // starting marker needs to be redrawn
            if(startingMarker == null){
                LatLng position = new LatLng(loc.getLatitude(), loc.getLongitude());
                startingMarker = mMap.addMarker(new MarkerOptions().position(position));
            }
            options.add( new LatLng( loc.getLatitude(),
                    loc.getLongitude() ) );
        }

        exerciseRoute = mMap.addPolyline( options );
    }

    /** The callback called when new location arrives. */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewLocation(LocationEvent event) {
        Log.d(TAG, "New location: " + event.getLocation().getLongitude());
        if(mMap == null){
            lastLocation = event.getLocation();
        }
        updateLocation(event.getLocation());
    }

    /** The callback called when new location list arrives. */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewLocationList(LocationListEvent event) {
        Log.d(TAG, "New location list arrived: " + event.getLocationList().size());
        if(mMap == null){
            locationList = event.getLocationList();
        }
        drawRoute(event.getLocationList());
    }
}
