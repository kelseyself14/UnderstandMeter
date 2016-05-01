package edu.augustana.csc490.understandmeter.activities;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.augustana.csc490.understandmeter.R;
import edu.augustana.csc490.understandmeter.utilities.SavedValues;

public class UnderstandButtons extends AppCompatActivity {

    private TextView mCountDown;
    private boolean connected = false;
    private EditText classIDEnter;
    private Button notUnderstand;
    private CountDownTimer timer;
    private long currentIDUs = -1;
    private Firebase myFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understand_buttons);
        Firebase.setAndroidContext(this);

        classIDEnter = (EditText) findViewById(R.id.classIdEnter);

        notUnderstand = (Button) findViewById(R.id.NotUnderstand);
        notUnderstand.setOnClickListener(displayNotUnderstand);

        Button logOut = (Button) findViewById(R.id.logOut);
        logOut.setOnClickListener(returnMain);

        Button connectToClassroom = (Button) findViewById(R.id.connectToClass);
        connectToClassroom.setOnClickListener(connectToClassroomListener);

        mCountDown = (TextView) findViewById(R.id.mCountDown);
        timer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountDown.setText("Seconds Remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                notUnderstand.setEnabled(true);
                notUnderstand.setClickable(true);
                mCountDown.setText("Press again");
            }
        };
    }

    private View.OnClickListener returnMain = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(UnderstandButtons.this, "Returning to Main Menu", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= 16) {
                onNavigateUp();
            } else {
                onBackPressed();
            }
        }
    };

    private View.OnClickListener displayNotUnderstand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (connected) { // indicating a connection was made
                notUnderstand.setClickable(false);
                notUnderstand.setEnabled(false);
                Toast.makeText(UnderstandButtons.this, "Anonymously submitted", Toast.LENGTH_LONG).show();

                myFirebase.child("IDUs").setValue(currentIDUs + 1);
                // the currentIDUs value will be updated via the callback from the server

                timer.start();
            } else {
                Snackbar.make(findViewById(R.id.understandButtonsLayout),
                        "Connect to a classroom first.", Snackbar.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener connectToClassroomListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!classIDEnter.getText().toString().isEmpty()) {
                connected = false;
                long classId = Long.parseLong(classIDEnter.getText().toString());
                myFirebase = new Firebase(SavedValues.FIREBASE_URL).child("classrooms/" + classId);

                myFirebase.child("IDUs").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            currentIDUs = (long) dataSnapshot.getValue();
                            connected = true;
                            Snackbar.make(findViewById(R.id.understandButtonsLayout),
                                    "Connected to server!", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(findViewById(R.id.understandButtonsLayout),
                                    "Enter the correct class ID first", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }
    };
}
