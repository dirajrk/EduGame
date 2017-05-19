package rk.diraj.edugame;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import rk.diraj.edugame.Utils.ScreenShotUtil;
import rk.diraj.edugame.Utils.AccelerometerUtil;

import static android.content.ContentValues.TAG;


public class ScoreActivity extends Activity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private AccelerometerUtil mAccelerometerUtil;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // High score related data that uses Score.java
        TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(PlayActivity.GAME_PREFS, 0);
        final String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score).append("\n");
        }
        scoreView.setText(scoreBuild.toString());

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometerUtil = new AccelerometerUtil();
        mAccelerometerUtil.setOnShakeListener(new AccelerometerUtil.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method takes screenshot and stores it in sdcard.
				 * If permission is not granted, it'll be requested again.
				 */
                if (getStoragePermission())
                    twitterShare(ScreenShotUtil.saveBitmap(ScreenShotUtil.takeScreenshot(ScoreActivity.this)), savedScores[0]);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mAccelerometerUtil, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mAccelerometerUtil);
        super.onPause();
    }

    private boolean getStoragePermission(){
        // Storage permission to save screenshot on Gallery and to share later on Twitter
        if (ActivityCompat.checkSelfPermission(this ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                // If user grants permission
                == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG,"Permission is granted");
            return true;
        } else {
            // If user rejects permission
            Log.v(TAG,"Permission is revoked");
            if(ActivityCompat.shouldShowRequestPermissionRationale(ScoreActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(context)
                        .setTitle(this.getString(R.string.permission_request))
                        .setMessage(this.getString(R.string.storage_permission))
                        .setPositiveButton(this.getString(R.string.continue_settings), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent
                                        (android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(i);
                            }
                        })
                        .setNegativeButton(this.getString(R.string.not_now), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: Cancelled Permission from App Info");
                            }
                        })
                        .show();
            }
            else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    private void twitterShare(String filename, String score) {
        // App shares image to Twitter from gallery saved
        try {
            Intent tweetIntent = new Intent(Intent.ACTION_SEND);

            File imageFile = new File(filename);

            tweetIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.beat_my_score) + getString(R.string.download_link));
            tweetIntent.putExtra(Intent.EXTRA_STREAM,
                    FileProvider.getUriForFile(this,
                            getApplicationContext()
                                    .getPackageName()
                                    .concat(".provider"), imageFile));
            tweetIntent.setType("image/jpeg");
            PackageManager pm = getPackageManager();
            List<ResolveInfo> lract = pm.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
            boolean resolved = false;
            for (ResolveInfo ri : lract) {
                if (ri.activityInfo.name.contains("twitter")) {
                    tweetIntent.setClassName(ri.activityInfo.packageName,
                            ri.activityInfo.name);
                    resolved = true;
                    break;
                }
            }

            startActivity(resolved ?
                    tweetIntent :
                    Intent.createChooser(tweetIntent, "Choose one"));
        } catch (final ActivityNotFoundException e) {
            Toast.makeText(this, "You don't seem to have Twitter installed on this device", Toast.LENGTH_SHORT).show();
        }
    }
}
