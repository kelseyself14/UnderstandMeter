package edu.augustana.csc490.understandmeter.activities;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import edu.augustana.csc490.understandmeter.R;
import edu.augustana.csc490.understandmeter.utilities.SavedValues;

public class UnderstandButtons extends AppCompatActivity {

    private static final String CLASS_SIG = "UnderstandButtons";

    private boolean connected = false;

    private View mainLayout;
    private EditText classIDEditText;
    private TextView countDownText;
    private Button notUnderstandButton, connectToClassroom;

    private CountDownTimer timer;
    private Firebase myFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understand_buttons);
        Firebase.setAndroidContext(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(R.string.studentTitle);
        }

        mainLayout = findViewById(R.id.understandButtonsLayout);

        classIDEditText = (EditText) findViewById(R.id.classIdEnter);

        notUnderstandButton = (Button) findViewById(R.id.NotUnderstand);

        if (notUnderstandButton != null) {
            notUnderstandButton.setOnClickListener(displayNotUnderstand);
        }

        Button logOut = (Button) findViewById(R.id.logOut);

        if (logOut != null) {
            logOut.setOnClickListener(returnMain);
        }

        connectToClassroom = (Button) findViewById(R.id.connectToClass);

        if (connectToClassroom != null) {
            connectToClassroom.setOnClickListener(connectToClassroomListener);
        }

        countDownText = (TextView) findViewById(R.id.mCountDown);
        timer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                String toShow = "Seconds Remaining: " + millisUntilFinished / 1000;
                countDownText.setText(toShow);
            }

            public void onFinish() {
                notUnderstandButton.setEnabled(true);
                notUnderstandButton.setClickable(true);
                countDownText.setText(R.string.pressAgain);
                myFirebase.child("idus").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        mutableData.setValue((long) mutableData.getValue() - 1);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (b) {
                            String toPrint = "Success in decrementing!";
                            Toast.makeText(UnderstandButtons.this, toPrint, Toast.LENGTH_SHORT).show();
                            Log.d(CLASS_SIG, toPrint);
                        }
                    }
                });

                if (Build.VERSION.SDK_INT >= 23) {
                    notUnderstandButton.setBackgroundColor(getColor(R.color.red));
                } else {
                    //noinspection deprecation
                    notUnderstandButton.setBackgroundColor(getResources().getColor(R.color.red));
                }
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
                notUnderstandButton.setClickable(false);
                notUnderstandButton.setEnabled(false);

                if (Build.VERSION.SDK_INT >= 23) {
                    notUnderstandButton.setBackgroundColor(getColor(R.color.grey));
                } else {
                    //noinspection deprecation
                    notUnderstandButton.setBackgroundColor(getResources().getColor(R.color.grey));
                }

                Toast.makeText(UnderstandButtons.this, "Anonymously submitted", Toast.LENGTH_LONG).show();

                myFirebase.child("idus").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        mutableData.setValue((long) mutableData.getValue() + 1);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (b) {
                            Toast.makeText(UnderstandButtons.this, "Success in incrementing!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                // the currentIDUs value will be updated via the callback from the server

                timer.start();
            } else {
                if (mainLayout != null) {
                    Snackbar.make(mainLayout,
                            "Connect to a classroom first.", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    };

    private View.OnClickListener connectToClassroomListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!classIDEditText.getText().toString().isEmpty()) {
                connected = false;
                long classId = Long.parseLong(classIDEditText.getText().toString());
                myFirebase = new Firebase(SavedValues.FIREBASE_URL).child("classrooms/" + classId);

                myFirebase.child("idus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            connected = true;

                            if (mainLayout != null) {
                                Snackbar.make(mainLayout,
                                        "Updated IDU values!", Snackbar.LENGTH_LONG).show();
                            }

                            connectToClassroom.setEnabled(false);
                            connectToClassroom.setText(R.string.connectedToClass);

                        } else {
                            if (mainLayout != null) {
                                Snackbar.make(mainLayout,
                                        "Enter the correct class ID first",
                                        Snackbar.LENGTH_SHORT).show();
                            }
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
