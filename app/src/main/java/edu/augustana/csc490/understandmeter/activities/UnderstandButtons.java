package edu.augustana.csc490.understandmeter.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private boolean submitted = false;
    private String className = null;
    private AlertDialog prompt;
    private View mainLayout;
    private TextView classNameLabel, countDownText;
    private Button notUnderstandButton, logOut;
    private long secondsToTimeout = 20;

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
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mainLayout = findViewById(R.id.understandButtonsLayout);
        classNameLabel = (TextView) findViewById(R.id.className);

        notUnderstandButton = (Button) findViewById(R.id.NotUnderstand);
        if (notUnderstandButton != null) {
            notUnderstandButton.setOnClickListener(displayNotUnderstand);
        }

        logOut = (Button) findViewById(R.id.logOut);
        if (logOut != null) {
            logOut.setOnClickListener(returnMain);
            logOut.setEnabled(false);
        }

        countDownText = (TextView) findViewById(R.id.mCountDown);

        setUpFirebase();
    }

    private View.OnClickListener returnMain = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder builder = new AlertDialog.Builder(UnderstandButtons.this);

            builder.setTitle("Are you sure?");
            builder.setMessage("You will be leaving this class.");
            builder.setPositiveButton("Yes, log me out.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    goBack();
                }
            });

            builder.setNegativeButton("Nah, I\'ll stay here", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (prompt != null) {
                        prompt.dismiss();
                    }
                }
            });

            prompt = builder.create();
            prompt.show();


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

                if (!submitted) {
                    incrementIDUs();
                }

                timer.start();
            } else {
                if (mainLayout != null) {
                    Snackbar.make(mainLayout,
                            "Connect to a classroom first.", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    };

    private void setUpFirebase() {
        connected = false;
        String classId = getIntent().getStringExtra("classId");
        myFirebase = new Firebase(SavedValues.FIREBASE_URL).child("classrooms/" + classId);

        myFirebase.child("className").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                className = (String) dataSnapshot.getValue();

                //
                classNameLabel.setText(className);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(CLASS_SIG, firebaseError.getMessage());
            }
        });

        myFirebase.child("idus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    connected = true;

//                    long IDUs = (long) dataSnapshot.getValue();
//                    if (mainLayout != null) {
//                        Snackbar.make(mainLayout,
//                                "Updated IDU values!", Snackbar.LENGTH_LONG).show();
//                    }

                    logOut.setEnabled(true);

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
                Log.e(CLASS_SIG, firebaseError.getMessage());
            }
        });

        myFirebase.child("reset").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isReset = (boolean) dataSnapshot.getValue();
                    if (isReset) {
                        Toast.makeText(UnderstandButtons.this, "The class has ended.", Toast.LENGTH_LONG).show();
                        goBack();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(CLASS_SIG, firebaseError.getMessage());
            }
        });

        myFirebase.child("msToReset").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    secondsToTimeout = (long) dataSnapshot.getValue();
                    Toast.makeText(UnderstandButtons.this, "Timeout is set to " + secondsToTimeout + " seconds.", Toast.LENGTH_SHORT).show();

                    timer = new CountDownTimer((secondsToTimeout * 1000), 1000) {

                        public void onTick(long millisUntilFinished) {
                            String toShow = "Seconds Remaining: " + millisUntilFinished / 1000;
                            countDownText.setText(toShow);
                        }

                        public void onFinish() {
                            notUnderstandButton.setEnabled(true);
                            notUnderstandButton.setClickable(true);
                            countDownText.setText(R.string.pressAgain);

                            if (submitted) {
                                decrementIDUs();
                            }

                            if (Build.VERSION.SDK_INT >= 23) {
                                notUnderstandButton.setBackgroundColor(getColor(R.color.red));
                            } else {
                                //noinspection deprecation
                                notUnderstandButton.setBackgroundColor(getResources().getColor(R.color.red));
                            }
                        }
                    };
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(CLASS_SIG, firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (submitted) {
            decrementIDUs();
        }

        super.onBackPressed();
    }

    @Override
    public boolean onNavigateUp() {

        if (submitted) {
            decrementIDUs();
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

    /*
    This will cause the IDUs on the server to increase by 1.
     */
    private void incrementIDUs() {
        myFirebase.child("idus").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.setValue((long) mutableData.getValue() + 1);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                submitted = b;
                if (b) {
                    String toPrint = "Success in increment operation";
//                            Toast.makeText(UnderstandButtons.this, toPrint, Toast.LENGTH_SHORT).show();
                    Log.d(CLASS_SIG, toPrint);
                }
            }
        });
    }

    /*
     * This will cause the IDUs on the server to go down.
     */
    private void decrementIDUs() {
        myFirebase.child("idus").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (!((long) mutableData.getValue() <= 0)) {
                    mutableData.setValue((long) mutableData.getValue() - 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                submitted = !b;
                if (b) {
                    String toPrint = "Success in decrement operation";
//                            Toast.makeText(UnderstandButtons.this, toPrint, Toast.LENGTH_SHORT).show();
                    Log.d(CLASS_SIG, toPrint);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (submitted) {
                decrementIDUs();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
