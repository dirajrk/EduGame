package rk.diraj.edugame;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

// Accelerometer imports
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

// Screenshot imports
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class ScoreActivity extends Activity implements SensorEventListener{

    private Sensor mySensor;
    private SensorManager sM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Create SensorManager
        sM = (SensorManager)getSystemService(SENSOR_SERVICE);
        // Accelerometer sensor
        mySensor = sM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Registering SensorListener
        sM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        // High score related data that uses Score.java
        TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(PlayActivity.GAME_PREFS, 0);
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score+"\n");
        }
        scoreView.setText(scoreBuild.toString());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final ScrollView layout = (ScrollView) findViewById(R.id.scorelayout);

        layout.post(new Runnable() {
            @Override
            public void run() {
                Bitmap pic = getScreenshot(layout);

                try {
                    if (pic != null){
                        saveScreenshot(pic);
                        Toast.makeText(getApplicationContext(), "Screenshot works", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        final ScrollView layout = (ScrollView) findViewById(R.id.scorelayout);

        layout.post(new Runnable() {
            @Override
            public void run() {
                Bitmap pic = getScreenshot(layout);

                try {
                    if (pic != null){
                        saveScreenshot(pic);
                        Toast.makeText(getApplicationContext(), "Screenshot successful", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // Screenshot method
    private Bitmap getScreenshot(View v) {
        Bitmap screenShot = null;

        try {
            // Width and Height of the device
            int width = v.getMeasuredWidth();
            int height = v.getMeasuredHeight();

            screenShot = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            // Drawing to canvas
            Canvas c = new Canvas(screenShot);
            v.draw(c);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return screenShot;

    }

    // Saving screenshot to external storage
    private void saveScreenshot (Bitmap bm) {
        ByteArrayOutputStream bao = null;
        File file = null;

        try {
            // Compress and write to output stream
            bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 50, bao);

            // Write as a file to SD card
            file = new File(Environment.getExternalStorageDirectory()+File.separator+"highscore.png");
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bao.toByteArray());
            fos.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
