package edu.augustana.csc490.understandmeter.utilities;

import java.util.Date;

/**
 * This is a method to create a classroom.
 * Created by Nick Caputo on 4/25/2016.
 */
public class Classroom {

    //TODO: Maybe make all these private & use getters/setters.  You may need a zero-argument constructor.
    public long IDUs = 0;
    public long threshold = 9999;
    public double warningPercentage = 1.0;
    public String created = new Date().toString();
    public String className = "Classroom";
    public long maxStudents = 9999;

    public Classroom(long threshold, long maxStudents, String className) {
        this.threshold = threshold;
        this.maxStudents = maxStudents;
        this.className = className;
    }

}