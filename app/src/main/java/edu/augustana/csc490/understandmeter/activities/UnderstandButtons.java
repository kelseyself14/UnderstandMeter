package edu.augustana.csc490.understandmeter.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.augustana.csc490.understandmeter.R;

public class UnderstandButtons extends AppCompatActivity {

    private TextView mCountDown;
    private boolean value = true;
    private Button notUnderstand;
    private Button logOut;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_understand_buttons);
        Intent intent = getIntent();
        int option = intent.getIntExtra("JoinClass", 0);
        notUnderstand = (Button)  findViewById(R.id.NotUnderstand);
        notUnderstand.setOnClickListener(displayNotUnderstand);
        logOut = (Button) findViewById(R.id.LogOut);
        logOut.setOnClickListener(returnMain);
        mCountDown=(TextView) findViewById(R.id.mCountDown);
        timer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                mCountDown.setText("Seconds Remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                value = true;
                notUnderstand.setClickable(value);
                mCountDown.setText("Press Again");
                // Add code for sending a "negative tick" via fire base!!!

            }
        };
    }

    private View.OnClickListener returnMain= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            displayToast("Returning to Main Menu");
            launchTargetActivity(logOut);
        }
    };

    private View.OnClickListener displayNotUnderstand = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
            value = false;
            notUnderstand.setClickable(value);
            displayToast("You Don't Understand! Teacher Notified");

            // Add code for send info via fire base!!!

            startCountdown();
        }
    };
    private void displayToast(String description) {
        Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
    }

    public void launchTargetActivity (View btn) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("returnMain", 0);
        startActivity(intent);
    }

    public void startCountdown(){
        timer.start();

    }
}
