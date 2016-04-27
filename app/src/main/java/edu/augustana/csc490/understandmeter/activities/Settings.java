package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.augustana.csc490.understandmeter.R;

/**
 * Created by Scott Dav on 4/18/2016.
 * @author Scott Davis, Nick Caputo
 */
public class Settings extends AppCompatActivity {

    public String className;
    public int numStudents;
    public double percentage;
    public boolean alertOnClose;
    private Button createClass; //startClassButton
    private EditText classNameEnter, maxStudentsEnter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_settings);
        Intent intent = getIntent();
        int option = intent.getIntExtra("CreateClass", 0);

        classNameEnter = (EditText) findViewById(R.id.className);
        maxStudentsEnter = (EditText) findViewById(R.id.numStudents);

        createClass = (Button)  findViewById(R.id.startClassButton);
        createClass.setOnClickListener(startClass);

    }

    private View.OnClickListener startClass = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
//            Intent intent = new Intent(getApplicationContext(), Teacher_View.class);
//            intent.putExtra("CreateClass2", 0);
//            startActivity(intent);
//            Toast.makeText(Settings.this, "Launching......", Toast.LENGTH_LONG).show();

            String test = classNameEnter.getText().toString() + "\n";
        }
    };
}
