package com.lucas.buy.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.PopupWindowCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.lucas.buy.R;
import com.lucas.buy.actiivities.MyApplication;
import com.lucas.buy.contents.UserContents;
import com.lucas.buy.utils.FileUtils;
import com.lucas.buy.utils.ImageUpdateUtil;
import com.lucas.buy.utils.VolleyImageUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 111 on 2017/7/7.
 */

public class MineFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MineFragment";
    private static final int RESULT_LOCAL_IMAGE = 0;
    private static final int RESULT_CAMERA_IMAGE = 1;
    private View view;
    private ImageView img;
    private String mCurrentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mine_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        img = (ImageView) view.findViewById(R.id.mine_img);
        VolleyImageUtils.loadImage(UserContents.imageUrl, img);
        img.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);
    }

    private void showPopWindow() {
        Log.i(TAG, "...showPop:");
        View popView = View.inflate(this.getActivity(), R.layout.popupwindow_camera_layout, null);
        Button fromLocalBtn = (Button) popView.findViewById(R.id.pop_fromlocal);
        Button fromCameraBtn = (Button) popView.findViewById(R.id.pop_fromcamera);
        Button cancelBtn = (Button) popView.findViewById(R.id.pop_cancel);

        //得到屏幕的宽高
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels * 1 / 3;
        final PopupWindow popupWindow = new PopupWindow(popView, width, height);
//        popupWindow.setAnimationStyle(R.style.anim_pop_dir);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);//点击外部pop消失
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 50);

        fromCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeCamera(RESULT_CAMERA_IMAGE);
                popupWindow.dismiss();
            }
        });
        fromLocalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOCAL_IMAGE);
                popupWindow.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

    }

    private void takeCamera(int resultCameraImage) {
//        Intent takePicturIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //确定第三方是否可以被打开
//        if (takePicturIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            File photoFile = null;
//            photoFile = createImageFile();
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                takePicturIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photoFile));
//            }
//        }
//        startActivityForResult(takePicturIntent, resultCameraImage);//跳转界面传回拍照所得数据

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File photoFile = null;
            photoFile = createImageFile();
//            FileUtils.startActionCapture(getActivity(), photoFile, resultCameraImage);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = getUriForFile(this.getActivity(), photoFile);
            Log.i(TAG,"....uri:" + uri.toString());

//            ContentValues values = new ContentValues();
//            values.put(MediaStore.Images.Media.TITLE, photoFile.getName());
//            uri = getActivity().getContentResolver().insert(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            Log.i(TAG,"....uri:" + uri.toString());


//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(this.getActivity(), photoFile));
            startActivityForResult(intent, resultCameraImage);
        } else {
            Toast.makeText(getActivity(),"....",Toast.LENGTH_SHORT).show();
        }

    }

    private Uri getUriForFile(Context context, File photoFile) {
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

    private File createImageFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = null;
        try {
            image = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(TAG,"....mCurrentPhotoPath" + mCurrentPhotoPath);
        return image;
    }

    public static String generateFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_img:
                showPopWindow();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"....onActivityResult" + "...requestCode:" + requestCode + "...resultCode:" + resultCode + "...data:" + data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOCAL_IMAGE && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                final String picturePath = cursor.getString(columnIndex);

                Map<String, String> map = new HashMap<String, String>();
                map.put("id", "111");
                ImageUpdateUtil.getInstance().uploadFile(picturePath, "lucas", UserContents.imageGetUrl, map);

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImageUpdateUtil.upload(picturePath);
//                        Log.i(TAG, "...picturePath" + picturePath);
//                    }
//                }).start();
                cursor.close();
            } else if (requestCode == RESULT_CAMERA_IMAGE ) {
//                Bundle extras = data.getExtras();
//                Bitmap b = (Bitmap) extras.get("data");
//                img.setImageBitmap(b);
//                String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
//                String fileNmae = Environment.getExternalStorageDirectory().toString()+File.separator+"dong/image/"+name+".jpg";
//                String srcPath = fileNmae;
//                Log.i(TAG,"....srcPath:" + srcPath);
//                File myCaptureFile =new File(fileNmae);
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", "111");
                ImageUpdateUtil.getInstance().uploadFile(mCurrentPhotoPath, "lucas", UserContents.imageGetUrl, map);

//                try {
//                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//                        if(!myCaptureFile.getParentFile().exists()){
//                            myCaptureFile.getParentFile().mkdirs();
//                        }
////                        BufferedOutputStream bos;
////                        bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
//                        b.compress(Bitmap.CompressFormat.JPEG, 80, bos);
//                        bos.flush();
//                        bos.close();
//                    }else{
//
//                        Toast toast= Toast.makeText(PhotoActivity.this, "保存失败，SD卡无效", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                SimpleTarget target = new SimpleTarget<Bitmap>() {
//
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        upload(saveMyBitmap(resource).getAbsolutePath());
//                    }
//
//                    @Override
//                    public void onLoadStarted(Drawable placeholder) {
//                        super.onLoadStarted(placeholder);
//
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        super.onLoadFailed(e, errorDrawable);
//
//                    }
//                };
//
//                Glide.with(RegisterUIActivity.this).load(mCurrentPhotoPath)
//                        .asBitmap()
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .override(1080, 1920)//图片压缩
//                        .centerCrop()
//                        .dontAnimate()
//                        .into(target);

            }
        }

    }
}
