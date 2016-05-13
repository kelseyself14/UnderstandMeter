package edu.augustana.csc490.understandmeter.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import edu.augustana.csc490.understandmeter.R;

public class MainActivity extends AppCompatActivity {

    private static final String CLASS_SIG = "MainActivity";
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createClass = (Button) findViewById(R.id.createClass);
        if (createClass != null) {
            createClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, Settings.class));
                }
            });
        }

        Button joinClass = (Button) findViewById(R.id.joinClass);
        if (joinClass != null) {
            joinClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // this onClickListener made with help from
                    // http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
                    if (alert != null) {
                        alert.dismiss();
                    }

                    LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                    View alertView = layoutInflater.inflate(R.layout.alert_join_class, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(alertView);

                    builder.setTitle(R.string.joinClass);

                    final EditText getClassId = (EditText) alertView.findViewById(R.id.classIdEnter);

                    builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String classId = getClassId.getText().toString();

                            Intent goToUnderstandButtons = new Intent(MainActivity.this, UnderstandButtons.class);
                            goToUnderstandButtons.putExtra("classId", classId);
                            startActivity(goToUnderstandButtons);
                        }
                    });

                    builder.setNegativeButton("Nah, I\'ll stay here", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (alert != null) {
                                alert.dismiss();
                            }
                        }
                    });

                    alert = builder.create();
                    alert.show();
                }
            });
        }

        ImageButton goToHelp = (ImageButton) findViewById(R.id.about);
        if (goToHelp != null) {
            goToHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, Help.class));
                }
            });
        }
    }

}
