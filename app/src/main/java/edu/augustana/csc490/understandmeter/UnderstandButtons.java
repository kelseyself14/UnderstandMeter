package edu.augustana.csc490.understandmeter;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UnderstandButtons extends AppCompatActivity {

    private TextView mCountDown;
    private boolean value = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understand_buttons);
        Intent intent = getIntent();
        int option = intent.getIntExtra("JoinClass", 0);
        Button NotUnderstand = (Button)  findViewById(R.id.NotUnderstand);
        NotUnderstand.setOnClickListener(displayNotUnderstand);
        mCountDown=(TextView) findViewById(R.id.mCountDown);

    }
    private View.OnClickListener displayNotUnderstand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
            if(value == false) {
                value = true;
                displayToast("Not Understood");
                startCountdown();
            } else {
                displayToast("Wait for countdown to finish");
            }

        }
    };
    private void displayToast(String description) {
        Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
    }

    public void startCountdown(){
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
               mCountDown.setText("Seconds Remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mCountDown.setText("Press Again");
            }
        }.start();
        value = false;
    }
}
