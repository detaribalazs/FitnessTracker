package hu.bme.aut.dbalazs.fitnesstracker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workout {
    public enum WorkoutType {
        ARM_WORKOUT, CHEST_WORKOUT, SHOULDER_WORKOUT, LEG_WORKOUT, BACK_WORKOUT };

    private WorkoutType woType;
    private Date woDate;
    private ArrayList<Excercise> excerciseList;

    public Workout(WorkoutType woType, ArrayList<Excercise> excerciseList, long milis) {
        this.woType = woType;
        this.excerciseList = excerciseList;
        woDate = new Date(milis);
    }

    public Workout(WorkoutType woType, ArrayList<Excercise> excerciseList){
        this.woType = woType;
        this.excerciseList = excerciseList;
        woDate = new Date();
    }

    public WorkoutType getWoType() {
        return woType;
    }

    public void setWoType(WorkoutType woType) {
        this.woType = woType;
    }

    public Date getWoDate() {
        return woDate;
    }

    public void setWoDate(Date woDate) {
        this.woDate = woDate;
    }

    public List<Excercise> getExcerciseList() {
        return excerciseList;
    }

    public void setExcerciseList(ArrayList<Excercise> excerciseList) {
        this.excerciseList = excerciseList;
    }
}
