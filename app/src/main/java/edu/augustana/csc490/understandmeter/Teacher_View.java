package edu.augustana.csc490.understandmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Teacher_View extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__view);
        Intent intent = getIntent();
        int option = intent.getIntExtra("CreateClass2", 0);
    }
}
