package com.mehealth;

import java.util.ArrayList;

/**
 * Singleton to keep track of the exercises available.
 */
public class Exercises {
    private static final Exercises ourInstance = new Exercises();
    private ArrayList<Exercise> exercises;

    /**
     *
     * @return Instance of the singleton.
     */
    public static Exercises getInstance() {
        return ourInstance;
    }

    /**
     * List of the exercises.
     */
    private Exercises() {
        this.exercises = new ArrayList<>();
        exercises.add(new Exercise("Pyöräily", 0.1));
        exercises.add(new Exercise("Tanssi", 0.07094363514055884364701160536119));
        exercises.add(new Exercise("Juoksu", 0.13340795134001503665892222203379));
        exercises.add(new Exercise("Kävely", 0.05512557311716722912815642647583));
        exercises.add(new Exercise("Aerobic", 0.10847457627118644067796610169492));
        exercises.add(new Exercise("Koripallo", 0.11666666666666666666666666666667));
        exercises.add(new Exercise("Jalkapallo", 0.13333333333333333333333333333333));
        exercises.add(new Exercise("Boulderointi", 0.18333333333333333333333333333333));
        exercises.add(new Exercise("Tennis", 0.11666666666666666666666666666667));
        exercises.add(new Exercise("Lentopallo", 0.13333333333333333333333333333333));
        exercises.add(new Exercise("Vaellus", 0.0968926553672316384180790960452));
        exercises.add(new Exercise("Uinti", 0.09887005649717514124293785310734));
    }

    /**
     *
     * @return The exercise list.
     */
    public ArrayList getExercises() {
        return this.exercises;
    }

    /**
     *
     * @param i The index of the exercise in the list.
     * @return A single exercise from the list.
     */
    public Exercise getExercise(int i) {
        return this.exercises.get(i);
    }
}