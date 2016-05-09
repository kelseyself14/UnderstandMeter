package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
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
import java.util.Arrays;

import edu.augustana.csc490.understandmeter.R;
import edu.augustana.csc490.understandmeter.utilities.SavedValues;

public class TeacherView extends AppCompatActivity {

    private static final String CLASS_SIG = "TeacherView";

    private Firebase myFirebase;
    private TextView showIDUs;

    private long IDUs = -1;
    private int classWarningThreshold = -1;
    private AlertDialog alert;
    private CountDownTimer timer;
    Number[] series1Numbers;

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
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

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
       /* XYPlot plot = (XYPlot) findViewById(R.id.plot);
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(series1Numbers),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        plot.addSeries(series1, series1Format);*/

        timer = new CountDownTimer(20000, 1000) {
            //http://androidplot.com/docs/dynamically-plotting-sensor-data/
            int position = 0;
            XYPlot plot = (XYPlot) findViewById(R.id.plot);
            SimpleXYSeries IDUSeries=new SimpleXYSeries("IDUs");
            public void onTick(long millis) {
                //Number[] temp = new Number[position+1];
                Number[] series1Numbers = {5};
                IDUSeries.setModel(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
                IDUSeries.useImplicitXVals();
                plot.addSeries(IDUSeries, new LineAndPointFormatter());
                /*for(int i=0; i<=position;i++){
                    temp[i]=series1Numbers[i];
                }
                int value = 5;
                temp[position] = value;
                series1Numbers=temp;
                XYSeries series1 = new SimpleXYSeries(Arrays.asList(series1Numbers),
                        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");*/
                //LineAndPointFormatter series1Format = new LineAndPointFormatter();
                // series1Format.setPointLabelFormatter(new PointLabelFormatter());
                // plot.addSeries(IDUSeries, series1Format);
                plot.redraw();
            }

            public void onFinish() {

            }

        };
        Button endClass = (Button) findViewById(R.id.endClassButton);

        //if (endClass != null) {
        // endClass.setOnClickListener(returnMainScreen);
        //   }

        View.OnClickListener returnMainScreen = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//            if (myFirebase != null) {
//                myFirebase.removeValue();
//                classIsActive = false;
//            }

                if (Build.VERSION.SDK_INT >= 16) {
                    onNavigateUp();
                } else {
                    onBackPressed();
                }

            }
        };
    }
}
