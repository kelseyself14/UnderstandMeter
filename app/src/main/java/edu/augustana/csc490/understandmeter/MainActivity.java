package edu.augustana.csc490.understandmeter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button jClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jClass = (Button)  findViewById(R.id.joinClass);
        jClass.setOnClickListener(launchJoinClass);
    }
    private void displayToast(String paintingDescription) {
        // SHOW THE INFORMATION ABOUT THE PAINTING AS
        // A TOAST WITH A SHORT DISPLAY LIFE
        Toast.makeText(this, paintingDescription, Toast.LENGTH_SHORT).show();
    }
    private View.OnClickListener launchJoinClass = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // MAKE A METHOD CALL TO DISPLAY THE INFORMATION
            displayToast("Joining Class");
            launchTargetActivity(jClass);
        }
    };
    public void launchTargetActivity (View btn){
        Intent intent = new Intent(getApplicationContext(), UnderstandButtons.class);

        /*try {
            numShapes = Integer.parseInt(numImagesEditText.getText().toString());
            Log.d("SUPER_SHAPES_APP", "numShapes = " + numShapes);
        } catch (NumberFormatException e){
            numShapes = 1;
        }*/
        intent.putExtra("JoinClass", 0);
        startActivity(intent);
    }
}
