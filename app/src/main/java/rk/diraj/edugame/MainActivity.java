package rk.diraj.edugame;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    MediaPlayer mediaPlayer;
    private Button playBtn, settingBtn, helpBtn, scoreBtn;
    private String[] levelNames = {"Easy", "Medium", "Hard"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hides the action bar
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Song to play
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.menu_theme);
        // Start the song
        mediaPlayer.start();
        // Buttons
        playBtn = (Button) findViewById(R.id.playBtn);
        settingBtn = (Button) findViewById(R.id.settingBtn);
        helpBtn = (Button) findViewById(R.id.helpBtn);
        scoreBtn = (Button) findViewById(R.id.scoreBtn);
        // Button OnClickListener
        playBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        scoreBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.playBtn){
            //Play button

            Intent playIntent = new Intent(this, PlayActivity.class);
            this.startActivity(playIntent);
        }

        else if(view.getId()==R.id.settingBtn){
            //Settings button launches an Alert Dialog which presents difficulty options

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Choose a level")
                    .setSingleChoiceItems(levelNames, 0, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //Start Game
                            startPlay(which);
                        }
                    });
            AlertDialog ad = builder.create();
            ad.show();
        }

        else if(view.getId()==R.id.helpBtn){
            //How To button launches Help Activity

            Intent helpIntent = new Intent(this, HelpActivity.class);
            this.startActivity(helpIntent);
        }
        else if(view.getId()==R.id.scoreBtn){
            //Scores button launches Scores Activity

            Intent scoreIntent = new Intent(this, ScoreActivity.class);
            this.startActivity(scoreIntent);
        }
    }

    private void startPlay(int chosenLevel) {
        //Start the game
        Intent playIntent = new Intent(this, PlayActivity.class);
        playIntent.putExtra("level", chosenLevel);
        this.startActivity(playIntent);
    }

    @Override
    public void onPause() {
        // Pauses the song
        super.onPause();
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void onResume() {
        // Resumes the song
        super.onResume();
        if (!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        // Stops the song
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}
