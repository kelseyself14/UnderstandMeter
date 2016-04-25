package edu.augustana.csc490.understandmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Scott Dav on 4/18/2016.
 */
public class Settings extends AppCompatActivity {

    public String className;
    public int numStudents;
    public double percentage;
    public Boolean alertOnClose;
    private Button createClass; //startClassButton

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_settings);
        Intent intent = getIntent();
        int option = intent.getIntExtra("CreateClass", 0);
        createClass = (Button)  findViewById(R.id.startClassButton);
        createClass.setOnClickListener(startClass);

    }
    private View.OnClickListener startClass = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
            Intent intent = new Intent(getApplicationContext(), Teacher_View.class);
            intent.putExtra("CreateClass2",0);
            startActivity(intent);
            displayToast("Launching......");



        }
    };
    private void displayToast(String description) {
        Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
    }
}
