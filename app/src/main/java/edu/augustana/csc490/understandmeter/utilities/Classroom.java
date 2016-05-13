package edu.augustana.csc490.understandmeter.utilities;

import java.util.Date;

/**
 * This is a method to create a classroom,
 * deserialized by Firebase to save this information
 * on the server.
 *
 * Created by Nick Caputo on 4/25/2016.
 */
public class Classroom {

    // these are all sent and modified on Firebase
    private long IDUs = 0;
    private long msToReset = 20000;
    private long threshold = 9999;
    private double warningPercentage = 1.0;
    private boolean reset = false;
    private String created = new Date().toString();
    private String className = null;
    private long maxStudents = 9999;

    public Classroom(long threshold, long maxStudents, String className, long msToReset) {
        this.threshold = threshold;
        this.maxStudents = maxStudents;
        this.className = className;
        this.msToReset = msToReset;
    }

    public long getIDUs() {
        return IDUs;
    }

    public boolean isReset() {
        return reset;
    }

    public long getThreshold() {
        return threshold;
    }

    public long getMsToReset() {
        return msToReset;
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