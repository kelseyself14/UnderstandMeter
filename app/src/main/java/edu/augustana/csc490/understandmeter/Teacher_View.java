package edu.augustana.csc490.understandmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Teacher_View extends AppCompatActivity {
    private Button endClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__view);
        Intent intent = getIntent();
        int option = intent.getIntExtra("CreateClass2", 0);
        endClass = (Button) findViewById(R.id.endClassButton);
        endClass.setOnClickListener(returnMainScreen);
    }

    private void displayToast(String paintingDescription) {
        // SHOW THE INFORMATION ABOUT THE PAINTING AS
        // A TOAST WITH A SHORT DISPLAY LIFE
        Toast.makeText(this, paintingDescription, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener returnMainScreen = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            displayToast("Returning to Main Menu");
            launchTargetActivity(endClass);
        }
    };

    public void launchTargetActivity (View btn) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("returnMain", 0);
        startActivity(intent);
    }
}
