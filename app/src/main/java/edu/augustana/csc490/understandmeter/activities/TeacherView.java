package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.augustana.csc490.understandmeter.R;
import edu.augustana.csc490.understandmeter.utilities.SavedValues;
import edu.augustana.csc490.understandmeter.utilities.ScrollableSeries;

/*
This Class lacunches the Teacher View activity. This view displays the current number of IDUs, the
class ID, as well as a graph which shows the number of IDUs over time.
*/
public class TeacherView extends AppCompatActivity {
    //This sets the title seen at the top of the app
    private static final String CLASS_SIG = "TeacherView";
    //This creates the firebase object which we communicate with to send/recieve data
    private Firebase myFirebase;
    //This shows the title for the current number of IDUs
    private TextView showIDUs;
    //These values store the current number of IDUs, the warning threshold, and the reset time for
    //the graphy
    private long IDUs, classWarningThreshold, msToReset;
    //This stores the graphing points for the IDUs
    private ScrollableSeries series1;
    //This is the graph which is used to display the IDUs over time
    private XYPlot plot;
    //This is the alert that will pop up if IDU warning threshold has been met
    private AlertDialog alert;
    //This thread is created to run and update the graph
    private Thread updater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher_view);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.teacherTitle);
        }

        Intent intent = getIntent();
        long classId = intent.getLongExtra("classID", -1);
        int classSize = intent.getIntExtra("classSize", -1);
        classWarningThreshold = intent.getIntExtra("classWarningThreshold", -1);
        msToReset = 1000 * intent.getIntExtra("msTimeout", -1);

        if (classId > -1 && classSize > -1 && classWarningThreshold > -1) {
            TextView classIdView = (TextView) findViewById(R.id.numberCounter);
            String classIDStr = "" + classId;

            if (classIdView != null) {
                classIdView.setText(classIDStr);
            }

            showIDUs = (TextView) findViewById(R.id.showIDUs);

            Firebase.setAndroidContext(this);
            myFirebase = new Firebase(SavedValues.FIREBASE_URL)
                    .child("classrooms/" + classId);
            //handles the alert to teacher
            myFirebase.child("idus").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        IDUs = (long) dataSnapshot.getValue();
                        String IDUStr = "" + IDUs;
                        Log.d(CLASS_SIG, "IDUs updated to " + IDUStr);

                        if (showIDUs != null) {
                            showIDUs.setText(IDUStr);
                        }

                        if (IDUs >= classWarningThreshold) {
                            if (alert != null) {
                                alert.dismiss();
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(TeacherView.this);
                            builder.setTitle(R.string.studentsNotifyTitle);
                            builder.setMessage(R.string.studentsNotifyContent);
                            builder.setCancelable(true);
                            alert = builder.create();
                            alert.show();

                            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(5000);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            IDUPoll();

            Button endClass = (Button) findViewById(R.id.endClassButton);

            if (endClass != null) {
                endClass.setOnClickListener(returnMainScreen);
            }
        }

        XYPlotter(classSize);
    }

//Polls firebase to retrieve and reset the IDUs
    private void IDUPoll() {
        Button resetIDUs = (Button) findViewById(R.id.resetIDUCounter);

        if (resetIDUs != null) {
            resetIDUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myFirebase.child("idus").setValue(0);
                    myFirebase.child("resetCounter").runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            mutableData.setValue((long) mutableData.getValue() + 1);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                            if (b) {
                                String toPrint = "Reset the students IDUs";
                                Toast.makeText(TeacherView.this, toPrint, Toast.LENGTH_SHORT).show();
                                Log.d(CLASS_SIG, toPrint);
                            } else {
                                Log.e(CLASS_SIG, firebaseError.getDetails());
                            }
                        }
                    });
                }
            });
        }
    }

    /*this method plots a graph for the teacher to know how many students do not understand anything at a given minute
    during a lecture it implements a thread and an outside library Android plot
    Code extensively used: http://androidplot.com/docs/a-dynamic-xy-plot/ */
    private void XYPlotter(int classSize) {
        Number[] series1Numbers = {};
        series1 = new ScrollableSeries(Arrays.asList(series1Numbers), 0, "IDUs");
        plot = (XYPlot) findViewById(R.id.plot);

        if (plot != null) {
            plot.setRangeBoundaries(0, classSize, BoundaryMode.FIXED);
            plot.setRangeValueFormat(new DecimalFormat("0"));
            plot.setTicksPerDomainLabel(15);
        }

        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(0,0,200), Color.BLACK, null, null);
        plot.addSeries(series1, series1Format);

        updater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (IDUs > -1) {
                            series1.add(IDUs);
                            plot.redraw();
                            Thread.sleep(msToReset);
                        }
                    } catch (InterruptedException err) {
                        Log.e(CLASS_SIG, err.getMessage());
                    }
                }
            }
        });

        updater.start();
    }

    //resets the class
    View.OnClickListener returnMainScreen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (myFirebase != null) {
                myFirebase.removeValue();
                myFirebase.child("reset").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        mutableData.setValue(true);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (b) {
                            String toPrint = "Reset the class";
                            Log.d(CLASS_SIG, toPrint);
                            Toast.makeText(TeacherView.this, toPrint, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            goBack();

        }
    };


    @Override
    public void onBackPressed() {
        try {
            updater.join();
        } catch (InterruptedException err) {
            Log.e(CLASS_SIG, err.getMessage());
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigateUp() {
        try {
            updater.join();
        } catch (InterruptedException err) {
            Log.e(CLASS_SIG, err.getMessage());
        }
        return super.onNavigateUp();
    }

    private void goBack() {
        if (Build.VERSION.SDK_INT >= 16) {
            onNavigateUp();
        } else {
            onBackPressed();
        }
    }


}

