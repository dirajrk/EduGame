package rk.diraj.edugame;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Loads the activity_help.xml content and guides the user on how the app works
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
}
