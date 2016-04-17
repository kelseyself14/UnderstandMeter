package edu.augustana.csc490.understandmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UnderstandButtons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understand_buttons);
        Intent intent = getIntent();
        int option = intent.getIntExtra("JoinClass", 0);
        Button understand = (Button)  findViewById(R.id.IUnderstand);
        understand.setOnClickListener(displayUnderstand);
        Button NotUnderstand = (Button)  findViewById(R.id.NotUnderstand);
        NotUnderstand.setOnClickListener(displayNotUnderstand);
    }
    private View.OnClickListener displayUnderstand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
            displayToast("Understood");

        }
    };
    private View.OnClickListener displayNotUnderstand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
            displayToast("Not Understood");

        }
    };
    private void displayToast(String paintingDescription) {
        // SHOW THE INFORMATION ABOUT THE PAINTING AS
        // A TOAST WITH A SHORT DISPLAY LIFE
        Toast.makeText(this, paintingDescription, Toast.LENGTH_SHORT).show();
    }
}
