package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.augustana.csc490.understandmeter.R;
import edu.augustana.csc490.understandmeter.utilities.Classroom;
import edu.augustana.csc490.understandmeter.utilities.SavedValues;

/**
 * Created by Scott Dav on 4/18/2016.
 *
 * @author Scott Davis, Nick Caputo
 */
public class Settings extends AppCompatActivity {

    private static final String CLASS_SIG = "Settings";

    private EditText classNameEnter, maxStudentsEnter, warningPercentage;

    private long nextUniqueId = -1;
    private Firebase myFirebase;
    private boolean showedInitialConnectionNotify = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_class_settings);
        Intent intent = getIntent();
        int option = intent.getIntExtra("CreateClass", 0);

        // sets up firebase connection
        setUpFirebase();


        classNameEnter = (EditText) findViewById(R.id.className);
        maxStudentsEnter = (EditText) findViewById(R.id.numStudents);
        warningPercentage = (EditText) findViewById(R.id.warningPercentage);

        Button createClass = (Button) findViewById(R.id.startClassButton);
        createClass.setOnClickListener(startClass);
    }

    private View.OnClickListener startClass = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Firebase toUsers = myFirebase.child("classrooms");
            long idForClassroom = createClassroom(toUsers);

            if (idForClassroom > -1) {
                Log.d(CLASS_SIG, "Class successfully created.");
                Snackbar.make(findViewById(R.id.settingsLayout),
                        "Classroom with ID number " + idForClassroom + " was created successfully!",
                        Snackbar.LENGTH_LONG).show();

                Intent switchToTeacherV = new Intent(Settings.this, TeacherView.class);
                switchToTeacherV.putExtra("classID", idForClassroom);
                switchToTeacherV.putExtra("classSize", Integer.parseInt(maxStudentsEnter.getText().toString()));
                switchToTeacherV.putExtra("classWarningThreshold", Integer.parseInt(warningPercentage.getText().toString()));
                startActivity(switchToTeacherV);
            }
        }
    };

    /**
     * Sets up the connection to firebase, and automatically updates the next unique ID to create
     * classrooms with.
     */
    private void setUpFirebase() {
        Firebase.setAndroidContext(this);
        myFirebase = new Firebase(SavedValues.FIREBASE_URL);

        myFirebase.child("nextUniqueId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(CLASS_SIG, "getting snapshot of data!");
                nextUniqueId = (long) dataSnapshot.getValue();
//                Toast.makeText(MainActivity.this, "" + count, Toast.LENGTH_LONG).show();
                Log.d(CLASS_SIG, "Next unique ID is: " + nextUniqueId);

                if (!showedInitialConnectionNotify) {
                    Snackbar.make(findViewById(R.id.settingsLayout), "Established connection to the" +
                            " server!", Snackbar.LENGTH_LONG).show();
                    Log.d(CLASS_SIG, "made initial connection to server");
                    showedInitialConnectionNotify = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(CLASS_SIG, firebaseError.getMessage());
                Log.e(CLASS_SIG, firebaseError.getDetails());
            }
        });
    }

    /**
     * Creates a new classroom in Firebase that students can connect with.
     *
     * @param referenceToUsers, the reference to classroom objects in Firebase.
     */
    private long createClassroom(Firebase referenceToUsers) {
        boolean maxStudentsHasContent = !maxStudentsEnter.getText().toString().isEmpty();
        boolean warningThresholdHasContent = !warningPercentage.getText().toString().isEmpty();

        /*
        To create a classroom, a connection to firebase has to have been made,
        and there must be a value entered in the max students field
        and the warning percentage field
         */
        if (nextUniqueId > -1 && maxStudentsHasContent && warningThresholdHasContent) {
            int maxStudents = Integer.parseInt(maxStudentsEnter.getText().toString());
            int warningThreshold = Integer.parseInt(warningPercentage.getText().toString());
            String className = classNameEnter.getText().toString();
            long idForClassroom = nextUniqueId;


            // increments the next unique Id in Firebase
            myFirebase.child("nextUniqueId").setValue(nextUniqueId + 1);
            // the next unique Id is automatically incremented by a callback from Firebase

            // creates classroom in Firebase
            Classroom room = new Classroom(warningThreshold, maxStudents, className); // sets the amount of IDUs and the threshold
            Firebase pathToClassroom = referenceToUsers.child("/" + nextUniqueId + "/");
            pathToClassroom.setValue(room);


            return idForClassroom;
        } else { // examine all errors
            String errorString = "";

            if (nextUniqueId <= -1) {
                Log.e(CLASS_SIG, "tried to create a classroom without connecting to the server first");
            }

            if (!maxStudentsHasContent) {
                Log.d(CLASS_SIG, "tried to create a classroom without specifying max students");
                errorString += "Please specify max students\n";
            }

            if (!warningThresholdHasContent) {
                Log.d(CLASS_SIG, "tried to create a classroom without specifying warning threshold");
                errorString += "Please specify warning threshold.";
            }

            Snackbar.make(findViewById(R.id.settingsLayout), errorString, Snackbar.LENGTH_LONG).show();
            return -1;
        }
    }
}
