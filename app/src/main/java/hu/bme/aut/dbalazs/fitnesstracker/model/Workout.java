package hu.bme.aut.dbalazs.fitnesstracker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static hu.bme.aut.dbalazs.fitnesstracker.model.Workout.WorkoutType.ARM_WORKOUT;
import static hu.bme.aut.dbalazs.fitnesstracker.model.Workout.WorkoutType.BACK_WORKOUT;
import static hu.bme.aut.dbalazs.fitnesstracker.model.Workout.WorkoutType.CHEST_WORKOUT;
import static hu.bme.aut.dbalazs.fitnesstracker.model.Workout.WorkoutType.LEG_WORKOUT;
import static hu.bme.aut.dbalazs.fitnesstracker.model.Workout.WorkoutType.SHOULDER_WORKOUT;

public class Workout {
    public enum WorkoutType {
        ARM_WORKOUT, CHEST_WORKOUT, SHOULDER_WORKOUT, LEG_WORKOUT, BACK_WORKOUT };

    private WorkoutType woType;
    private Date woDate;
    private ArrayList<Exercise> exerciseList;
    private int id; // TODO set this with its ID in database

    public Workout(WorkoutType woType, ArrayList<Exercise> exerciseList, long milis) {
        this.woType = woType;
        this.exerciseList = exerciseList;
        woDate = new Date(milis);
    }

    public Workout(WorkoutType woType, ArrayList<Exercise> exerciseList){
        this.woType = woType;
        this.exerciseList = exerciseList;
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

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(ArrayList<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String typeToString(WorkoutType type)
    {
        switch (type){
            case ARM_WORKOUT:
                return "Arm";
            case BACK_WORKOUT:
                return "Back";
            case CHEST_WORKOUT:
                return "Chest";
            case SHOULDER_WORKOUT:
                return "Shoulder";
            case LEG_WORKOUT:
                return  "Leg";
            default:
                return "Unknown";
        }
    }
}
