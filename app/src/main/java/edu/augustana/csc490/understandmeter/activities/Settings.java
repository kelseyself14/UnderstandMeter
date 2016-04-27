package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.augustana.csc490.understandmeter.R;
import edu.augustana.csc490.understandmeter.utilities.Classroom;

/**
 * Created by Scott Dav on 4/18/2016.
 * @author Scott Davis, Nick Caputo
 */
public class Settings extends AppCompatActivity {

    private static final String CLASS_SIG = "Settings";
    private EditText classNameEnter, maxStudentsEnter, warningPercentage;
    private long nextUniqueId = -1;
    private Firebase myFirebase;

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
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
//            Intent intent = new Intent(getApplicationContext(), TeacherView.class);
//            intent.putExtra("CreateClass2", 0);
//            startActivity(intent);
//            Toast.makeText(Settings.this, "Launching......", Toast.LENGTH_LONG).show();

            Firebase toUsers = myFirebase.child("classrooms");
            createClassroom(toUsers);
        }
    };

    /**
     * Sets up the connection to firebase, and automatically updates the next unique ID to create
     * classrooms with.
     */
    private void setUpFirebase() {
        Firebase.setAndroidContext(this);
        final String firebase_url = "https://dazzling-heat-4680.firebaseIO.com/";
        myFirebase = new Firebase(firebase_url);

        myFirebase.child("nextUniqueId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(CLASS_SIG, "getting snapshot of data!");
                nextUniqueId = (long) dataSnapshot.getValue();
//                Toast.makeText(MainActivity.this, "" + count, Toast.LENGTH_LONG).show();
                Log.d(CLASS_SIG, "Next unique ID is: " + nextUniqueId);
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
     * @param referenceToUsers, the reference to classroom objects in Firebase.
     */
    private void createClassroom(Firebase referenceToUsers) {
        if (nextUniqueId > -1) { // ensures that a connection to Firebase has been made
            int maxStudents = Integer.parseInt(maxStudentsEnter.getText().toString());
            int warningThreshold = Integer.parseInt(warningPercentage.getText().toString());
            String className = classNameEnter.getText().toString();

            Classroom room = new Classroom(warningThreshold, maxStudents, className); // sets the amount of IDUs and the threshold
            Firebase pathToClassroom = referenceToUsers.child("/" + nextUniqueId + "/");
            pathToClassroom.setValue(room);

            nextUniqueId++;
            myFirebase.child("nextUniqueId").setValue(nextUniqueId);
        }
    }
}
