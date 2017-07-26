package com.lucas.buy.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lucas.buy.R;
import com.lucas.buy.contents.UserContents;
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

public class MineFragment extends Fragment implements View.OnClickListener, ImageUpdateUtil.OnUploadProcessListener {
    private static final String TAG = "MineFragment";
    private static final int RESULT_LOCAL_IMAGE = 0;
    private static final int RESULT_CAMERA_IMAGE = 1;
    private View view;
    private ImageView img;
    private String mCurrentPhotoPath;

    private ProgressBar progressBar;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.i(TAG,"....max:" + msg.obj);
                    progressBar.setMax((int)msg.obj);
                    break;
                case 1:
                    Log.i(TAG,"....progress:" + msg.obj);
                    progressBar.setProgress((int)msg.obj);
                    break;
                case 2:
                    Log.i(TAG,"....ok:" + msg.obj);
//                    progressBar.setProgress(Integer.valueOf(msg.obj.toString()));
                    progressBar.setProgress((int) msg.obj);
                    Toast.makeText(getActivity(),"文件上传成功！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

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

        progressBar = (ProgressBar) view.findViewById(R.id.mine_progress_bar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);
    }

    /**
     * 显示popWindow
     *
     */
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

    /**
     * 打开相机拍照
     * @param resultCameraImage
     */
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
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//            Uri uri = getUriForFile(this.getActivity(), photoFile);
//            Log.i(TAG,"....uri:" + uri.toString());

            //这部分代码感觉就是鸡肋
//            ContentValues values = new ContentValues();
//            values.put(MediaStore.Images.Media.TITLE, photoFile.getName());
//            uri = getActivity().getContentResolver().insert(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            Log.i(TAG,"....uri:" + uri.toString());
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            //测试发现，不加传递内容，onActivityResult中data才不为空，但是传递过去一个缩略图，要想获取原图，需要从保存图片的路径中拿
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(this.getActivity(), photoFile));
            intent.putExtra("imagePath", photoFile.getAbsoluteFile());
            startActivityForResult(intent, resultCameraImage);
        } else {
            Toast.makeText(getActivity(),"....",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 鸡肋啊，根本用不着
     * @param context
     * @param photoFile
     * @return
     */
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


    /**
     * 在onActivityResult方法中获取图片数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                final String picturePath = cursor.getString(columnIndex);//拿出了图片的路径
                Log.i(TAG,"...picturePath:" + picturePath);

                Map<String, String> map = new HashMap<String, String>();
                map.put("name", "lucas");
                ImageUpdateUtil.getInstance().setOnUploadProcessListener(this);
                ImageUpdateUtil.getInstance().uploadFile(picturePath, "file", UserContents.imageGetUrl, map, handler);


//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImageUpdateUtil.upload(picturePath);
//                        Log.i(TAG, "...picturePath" + picturePath);
//                    }
//                }).start();
                cursor.close();
            } else if (requestCode == RESULT_CAMERA_IMAGE ) {
                //你妹！data根本什么东东都没有好不好
//                String path = data.getStringExtra("imagePath");
//                Log.i(TAG,"...path:" + path);

//                Bundle extras = data.getExtras();
//                Bitmap b = (Bitmap) extras.get("data");
//                img.setImageBitmap(b);
//                String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
//                String fileNmae = Environment.getExternalStorageDirectory().toString()+File.separator+"dong/image/"+name+".jpg";
//                String srcPath = fileNmae;
//                Log.i(TAG,"....srcPath:" + srcPath);
//                File myCaptureFile =new File(fileNmae);

                Map<String, String> map = new HashMap<String, String>();
                map.put("name", "lucas");
                ImageUpdateUtil.getInstance().setOnUploadProcessListener(this);
                ImageUpdateUtil.getInstance().uploadFile(mCurrentPhotoPath, "file", UserContents.imageGetUrl, map, handler);

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

    @Override
    public void onUploadDone(int responseCode, String message) {
        Log.i(TAG,"...responseCode:" + responseCode + "..message:" + message);
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        Log.i(TAG, "....uploadSize:" + uploadSize);
    }

    @Override
    public void initUpload(int fileSize) {
        Log.i(TAG,"..fileSize:" + fileSize);
    }
}
