package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.augustana.csc490.understandmeter.R;
import edu.augustana.csc490.understandmeter.utilities.SavedValues;

public class TeacherView extends AppCompatActivity {

    private static final String CLASS_SIG = "TeacherView";

    private Firebase myFirebase;
    private boolean classIsActive = true;

    private long classId = -1;
    private long IDUs = -1;
    private int classWarningThreshold = -1;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);

        Intent intent = getIntent();
        classId = intent.getLongExtra("classID", -1);
        int classSize = intent.getIntExtra("classSize", -1);
        classWarningThreshold = intent.getIntExtra("classWarningThreshold", -1);

        if (classIsActive && classId > -1 && classSize > -1 && classWarningThreshold > -1) {
            TextView classIdView = (TextView) findViewById(R.id.numberCounter);
            classIdView.setText("" + classId);

            final TextView showIDUs = (TextView) findViewById(R.id.showIDUs);

            Firebase.setAndroidContext(this);
            myFirebase = new Firebase(SavedValues.FIREBASE_URL)
                    .child("classrooms/" + classId);

            myFirebase.child("IDUs").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    IDUs = (long) dataSnapshot.getValue();
                    Log.d(CLASS_SIG, "IDUs updated to " + IDUs);
                    showIDUs.setText("" + IDUs);

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

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            Button resetIDUs = (Button) findViewById(R.id.resetIDUCounter);
            resetIDUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myFirebase.child("IDUs").setValue(0);
                }
            });
        }

        Button endClass = (Button) findViewById(R.id.endClassButton);
        endClass.setOnClickListener(returnMainScreen);


    }

    private View.OnClickListener returnMainScreen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (myFirebase != null) {
                myFirebase.removeValue();
                classIsActive = false;
            }

            if (Build.VERSION.SDK_INT >= 16) {
                onNavigateUp();
            } else {
                onBackPressed();
            }

        }
    };
}
