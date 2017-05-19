package rk.diraj.edugame.Utils;

// CP3406 Assignment 2 by Diraj Ravikumar (13255244)

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class ScreenShotUtil {

    public static Bitmap takeScreenshot(Activity activity) {
        View rootView = activity.getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public static String saveBitmap(Bitmap bitmap) {
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        File imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File screenshot = new File(imagePath, "FitB" + currentDateTime + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(screenshot);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }
        catch (IOException ignored) { }
        return (screenshot.toString());
    }

}
