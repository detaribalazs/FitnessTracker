package hu.bme.aut.dbalazs.fitnesstracker.events;

import hu.bme.aut.dbalazs.fitnesstracker.RunningTrackerActivity;

/**
 * Created by Balazs on 2017. 12. 07..
 */

public class ActivityStateChangedEvent {
    private RunningTrackerActivity.ActivityState activityState;

    public ActivityStateChangedEvent(RunningTrackerActivity.ActivityState activityState) {
        this.activityState = activityState;
    }

    public RunningTrackerActivity.ActivityState getActivityState() {
        return activityState;
    }

    public void setActivityState(RunningTrackerActivity.ActivityState activityState) {
        this.activityState = activityState;
    }
}
