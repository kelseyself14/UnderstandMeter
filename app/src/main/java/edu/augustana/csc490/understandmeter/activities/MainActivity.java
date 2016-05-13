package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.augustana.csc490.understandmeter.R;
/*
This Class runs the activity which is the start up screen. The
Start up screen displays the name of the app and then prompts the
user to either create a class or join a class.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Establishing the Create Class button and click listener for this button
        Button createClass = (Button) findViewById(R.id.createClass);
        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Settings.class));
            }
        });
// Establishing the Join Class button and click listener for this button
        Button joinClass = (Button)  findViewById(R.id.joinClass);
        joinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UnderstandButtons.class));
            }
        });
    }

}
