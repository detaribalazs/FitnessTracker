package hu.bme.aut.dbalazs.fitnesstracker.model;

/**
 * Created by Balazs on 2017. 11. 08..
 */

public class Series {
    private int weight;
    private int reps;
    private long exerciseId;

    public Series(int weight, int reps, long id) {
        this.weight = weight;
        this.reps = reps;
        this.exerciseId = id;
    }

    public Series(int weight, int reps) {
        this.weight = weight;
        this.reps = reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public long getExerciseId() {
        return exerciseId;
    }
}
