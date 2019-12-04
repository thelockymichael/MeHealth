package com.mehealth;

import java.util.ArrayList;

public class Exercises {
    private static final Exercises ourInstance = new Exercises();
    private ArrayList<Exercise> exercises;

    public static Exercises getInstance() {
        return ourInstance;
    }

    private Exercises() {
        this.exercises = new ArrayList<>();
        exercises.add(new Exercise("Pyöräily, rento", 4));
        exercises.add(new Exercise("Tanssi", 3));
        exercises.add(new Exercise("Juoksu", 8.301));
        exercises.add(new Exercise("Kävely", 4.301));
        exercises.add(new Exercise("Uinti", 5.795));
    }

    public ArrayList getExercises() {
        return this.exercises;
    }

    public Exercise getExercise(int i) {
        return this.exercises.get(i);
    }
}
