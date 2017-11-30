package hu.bme.aut.dbalazs.fitnesstracker.model;

/**
 * Created by Balazs on 2017. 11. 08..
 */

public class Exercise {
    private String name;
    private long id;

    public Exercise(long id, String name) {
        this.id  = id;
        this.name = name;
    }

    public Exercise(String name){
        this.name = name;
        this.id = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
