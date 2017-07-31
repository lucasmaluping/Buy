package com.lucas.buy.fragments;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lucas.buy.R;

import java.io.IOException;

/**
 * Created by 111 on 2017/7/7.
 */

public class IndexFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "IndexFragment";
    private View view;

    ImageButton btnplay, btnstop, btnpause;
    SurfaceView surfaceView;
    MediaPlayer mediaPlayer;
    int position;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.index_layout,container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        btnplay=(ImageButton)view.findViewById(R.id.btnplay);
        btnstop=(ImageButton)view.findViewById(R.id.btnpause);
        btnpause=(ImageButton)view.findViewById(R.id.btnstop);

        btnstop.setOnClickListener(this);
        btnplay.setOnClickListener(this);
        btnpause.setOnClickListener(this);

//        mediaPlayer=new MediaPlayer();
//        mediaPlayer.set
        mediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.shipin);
        surfaceView=(SurfaceView) this.view.findViewById(R.id.surfaceView);

        //设置SurfaceView自己不管理的缓冲区
//        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        /*
         * void android.view.SurfaceHolder.setType(int type)

public abstract void setType (int type) Since: API Level 1

This method is deprecated. this is ignored, this value is set automatically when needed.

Sets the surface's type.
         */
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (position>0) {
                    try {
                        //开始播放
                        play();
                        //并直接从指定位置开始播放
                        mediaPlayer.seekTo(position);
                        position=0;
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }



            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {

            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) view.getParent()).removeView(view);
    }



    private void play() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path = path + "/DCIM/Camera";
        try {
//            mediaPlayer.reset();
//            mediaPlayer
//                    .setAudioStreamType(AudioManager.STREAM_MUSIC);
//            //设置需要播放的视频
////            mediaPlayer.setDataSource(path + "/VID_20170720_083230.mp4");
//            //把视频画面输出到SurfaceView
            mediaPlayer.setDisplay(surfaceView.getHolder());
//            mediaPlayer.prepare();
            //播放
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnplay:
                Log.i(TAG,"....start:");
                play();
                break;

            case R.id.btnpause:
                Log.i(TAG,"....pause:");
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }else{
                    mediaPlayer.start();
                }
                break;

            case R.id.btnstop:
                Log.i(TAG,"....stop:");
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                break;
            default:
                break;
        }
    }
}
