package hu.bme.aut.dbalazs.fitnesstracker.application;

import android.app.Application;

import hu.bme.aut.dbalazs.fitnesstracker.database.FitnessDatabaseInterface;

public class FitnessApplication extends Application {
    private static FitnessDatabaseInterface dbInterface;

    public static FitnessDatabaseInterface getDatabaseInterface() {
        return dbInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbInterface = new FitnessDatabaseInterface(this);
        dbInterface.open();
    }

    @Override
    public void onTerminate() {
        dbInterface.close();
        super.onTerminate();
    }
}