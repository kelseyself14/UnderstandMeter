package edu.augustana.csc490.understandmeter.utilities;

import java.util.Date;

/**
 * This is a method to create a classroom.
 * Created by Nick Caputo on 4/25/2016.
 */
public class Classroom {

    private long IDUs = 0;
    private long timesReset = 0;
    private long threshold = 9999;
    private double warningPercentage = 1.0;
    private String created = new Date().toString();
    private String className = "Classroom";
    private long maxStudents = 9999;

    public Classroom(long threshold, long maxStudents, String className) {
        this.threshold = threshold;
        this.maxStudents = maxStudents;
        this.className = className;
    }

    public long getIDUs() {
        return IDUs;
    }

    public long getTimesReset() {
        return timesReset;
    }

    public long getThreshold() {
        return threshold;
    }

    public double getWarningPercentage() {
        return warningPercentage;
    }

    public String getCreated() {
        return created;
    }

    public String getClassName() {
        return className;
    }

    public long getMaxStudents() {
        return maxStudents;
    }

}