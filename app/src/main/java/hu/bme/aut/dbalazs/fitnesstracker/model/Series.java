package hu.bme.aut.dbalazs.fitnesstracker.model;

/**
 * Created by Balazs on 2017. 11. 08..
 */

public class Series {
    private int weight;
    private int reps;
    private int seriesId;

    public Series(int weight, int reps, int id) {
        this.weight = weight;
        this.reps = reps;
        this.seriesId = id;
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
}
