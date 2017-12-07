package hu.bme.aut.dbalazs.fitnesstracker.location;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import hu.bme.aut.dbalazs.fitnesstracker.MainActivity;
import hu.bme.aut.dbalazs.fitnesstracker.R;
import hu.bme.aut.dbalazs.fitnesstracker.RunningTrackerActivity;
import hu.bme.aut.dbalazs.fitnesstracker.events.ActivityStateChangedEvent;
import hu.bme.aut.dbalazs.fitnesstracker.events.ExerciseStateEvent;
import hu.bme.aut.dbalazs.fitnesstracker.events.LocationEvent;
import hu.bme.aut.dbalazs.fitnesstracker.events.LocationListEvent;

public class LocationService extends Service implements LocationListener {

    private static final int NOTIFICATION_ID = 104;
    public static final String BR_NEW_LOCATION = "BR_NEW_LOCATION";
    public static final String KEY_LOCATION = "KEY_LOCATION";
    private static final String TAG = "LocationService";

    private LDLocationManager ldLocationManager = null;
    private boolean locationMonitorRunning = false;

    private RunningTrackerActivity.ActivityState activityState = RunningTrackerActivity.ActivityState.STARTED;
    private ArrayList<Location> locationList = new ArrayList<>();
    private boolean exerciseInProgress = false;
    private Location lastLocation = null;
    private long startingTime = 0;
    private Notification notification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lastLocation = null;
        if (!locationMonitorRunning) {
            locationMonitorRunning = true;
            ldLocationManager = new LDLocationManager(getApplicationContext(), this);
            ldLocationManager.startLocationMonitoring();
        }
        try {
            EventBus.getDefault().register(this);
        }
        catch (Exception e){

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (ldLocationManager != null) {
            ldLocationManager.stopLocationMonitoring();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if(activityState == RunningTrackerActivity.ActivityState.STARTED) {
            if (!exerciseInProgress) {
                EventBus.getDefault().post(new LocationEvent(location));
            } else {
                locationList.add(location);
                EventBus.getDefault().post(new LocationListEvent(locationList));
            }
        }
        else if(exerciseInProgress){
            locationList.add(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TBD
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TBD
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TBD
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActivityStateChanged(ActivityStateChangedEvent event) {
        Log.d(TAG, "Activity state changed: " + event.getActivityState().toString());
        activityState = event.getActivityState();
        if(event.getActivityState() == RunningTrackerActivity.ActivityState.STARTED){
            stopForeground(true);
            if(exerciseInProgress){
                EventBus.getDefault().post(new LocationListEvent(locationList));
            }
            else{
                EventBus.getDefault().post(new LocationEvent(lastLocation));
            }
        }
        // Activity stopped
        else{
            if(exerciseInProgress) {
                startForeground(NOTIFICATION_ID, createNotification("Running"));
            }
            else{
                stopSelf();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExerciseStateChanged(ExerciseStateEvent event) {
        Log.d(TAG, "Exercise state: " + (event.isStarted() ? "started" : "over"));
        if(event.isStarted()){
            locationList.add(lastLocation);
        }
        exerciseInProgress = event.isStarted();
        startingTime = System.currentTimeMillis();
    }

    private Notification createNotification(String text) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Fitness Tracker")
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(contentIntent).build();
        return  notification;
    }

    private void updateNotification(String text) {
        Notification notification = createNotification(text);
        NotificationManager notifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifMan.notify(NOTIFICATION_ID, notification);
    }


}