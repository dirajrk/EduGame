package rk.diraj.edugame;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;

public class HelpActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Loads the activity_help.xml content and guides the user on how the app works
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Song to play
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.menu_theme);
        // Start the song
        mediaPlayer.start();
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
