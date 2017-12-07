package hu.bme.aut.dbalazs.fitnesstracker.events;

/**
 * Created by Balazs on 2017. 12. 07..
 */

public class ExerciseStateEvent {
    private boolean started;

    public ExerciseStateEvent(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
