package com.lucas.buy.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.lucas.buy.R;

import java.io.File;

/**
 * Created by 111 on 2017/7/17.
 */

public class FileUtils {

    /**
     * 打开相机
     * 兼容7.0  24
     * @param activity
     * @param photoFile
     * @param resultCameraImage
     */
    public static void startActionCapture(FragmentActivity activity, File photoFile, int resultCameraImage) {
        if(activity == null) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, photoFile));
        activity.startActivityForResult(intent, resultCameraImage);

    }

    private static Uri getUriForFile(Context context, File photoFile) {
        if(context == null || photoFile == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if(Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(),"com.lucas.buy.myProvider", photoFile);
        } else {
            uri = Uri.fromFile(photoFile);
        }
        return uri;
    }
}
