package edu.augustana.csc490.understandmeter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by Scott Dav on 4/18/2016.
 */
public class Settings extends AppCompatActivity {

    public String className;
    public int numStudents;
    public double percentage;
    public Boolean alertOnClose;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_settings);
        Intent intent = getIntent();
        int option = intent.getIntExtra("CreateClass", 0);

    }
}
