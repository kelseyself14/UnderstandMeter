package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.augustana.csc490.understandmeter.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createClass = (Button) findViewById(R.id.createClass);
        createClass.setOnClickListener(launchSettings);

        Button jClass = (Button)  findViewById(R.id.joinClass);
        jClass.setOnClickListener(launchJoinClass);
    }

    private View.OnClickListener launchSettings = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity.this, "Launching Settings", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Settings.class);
            intent.putExtra("CreateClass", 0);
            startActivity(intent);
        }
    };

    private View.OnClickListener launchJoinClass = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
            Toast.makeText(MainActivity.this, "Joining Class", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, UnderstandButtons.class);
            intent.putExtra("JoinClass", 0);
            startActivity(intent);
        }
    };

}
