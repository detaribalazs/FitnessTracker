package hu.bme.aut.dbalazs.fitnesstracker.database;


public class DatabaseConstants {
    public static String DATABASE_NAME = "fitness_database.db";
    public static int DATABASE_VERSION = 1;

    public static String CREATE_ALL = Workout.CREATE_TABLE + Exercise.CREATE_TABLE + Series.CREATE_TABLE;

    public static String DROP_ALL = Series.DROP_TABLE + Exercise.DROP_TABLE + Workout.DROP_TABLE;

    public class Series {
        public static final String TABLE_NAME = "series";
        public static final String ID = "_id";
        public static final String EXERCISE_ID = "exercise_id";
        public static final String REPS = "reps";
        public static final String WEIGHT = "weight";

        public static final String CREATE_TABLE =  "create table if not exists "+ TABLE_NAME + " ( "
                + ID +" integer primary key autoincrement, "
                + REPS + " integer, "
                + WEIGHT + " integer, "
                + EXERCISE_ID + " integer, "
                +"foreign key(" + EXERCISE_ID + ") REFERENCES "+  Exercise.TABLE_NAME + "(" + Exercise.ID + ") "
                + "); ";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME + "; ";
    }

    public class Exercise {
        public static final String TABLE_NAME = "exercise";
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String WORKOUT_ID = "workout_id";

        public static final String CREATE_TABLE = "create table if not exists "+ TABLE_NAME+" ( "
                + ID +" integer primary key autoincrement, "
                + NAME + " text, "
                + WORKOUT_ID + " integer, "
                +"foreign key(" + WORKOUT_ID + ") references "+  Workout.TABLE_NAME + "(" + Workout.ID + ") "
                + "); ";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME + "; ";
    }

    public class Workout{
        public static final String TABLE_NAME = "workout";
        public static  final String ID = "_id";
        public static final String TYPE = "type";
        public static final String DATE = "date";

        public static final String CREATE_TABLE = "create table if not exists "+ TABLE_NAME+" ( "
                + ID +" integer primary key autoincrement, "
                + TYPE + " text, "
                + DATE + " integer); ";

        public static final String DROP_TABLE = "drop table if exists " + TABLE_NAME + "; ";
    }
}
